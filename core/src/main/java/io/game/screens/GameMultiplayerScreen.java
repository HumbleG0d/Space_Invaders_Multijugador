package io.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.game.GameState;
import io.game.MainGame;
import io.game.client.GameClient;
import io.game.gamesobject.Bullet;
import io.game.gamesobject.enemigues.EnemyBlock;
import io.game.player.Player;
import io.game.server.GameServer.MoveCommand;
import io.game.server.GameServer.ShootCommand;

import java.io.IOException;

public class GameMultiplayerScreen extends AbstractScreen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private EnemyBlock enemyBlock;
    private GameClient client;
    private int localPlayerId;

    public GameMultiplayerScreen(MainGame game) {
        super(game);
        Gdx.app.log("GAME_MULTIPLAYER", "Constructor iniciado");
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);
        this.camera.update();

        // Inicializar fuente
        try {
            font = new BitmapFont(Gdx.files.internal("courier.fnt"));
            font.getData().setScale(0.5f);
            Gdx.app.log("GAME_MULTIPLAYER", "Fuente courier.fnt cargada");
        } catch (Exception e) {
            Gdx.app.error("FONT", "Error al cargar courier.fnt: " + e.getMessage(), e);
            font = new BitmapFont(); // Fuente por defecto como respaldo
            font.getData().setScale(0.5f);
            Gdx.app.log("GAME_MULTIPLAYER", "Usando fuente por defecto");
        }

        // Conectar al servidor
        try {
            client = new GameClient("localhost", 5000);
            localPlayerId = client.getLocalPlayerId();
            Gdx.app.log("GAME_MULTIPLAYER", "Conectado al servidor, localPlayerId: " + localPlayerId);
        } catch (Exception e) {
            Gdx.app.error("NETWORK", "Error al conectar al servidor: " + e.getMessage(), e);
            localPlayerId = -1;
            client = null;
        }
        Gdx.app.log("GAME_MULTIPLAYER", "Constructor finalizado");
    }

    @Override
    public void render(float delta) {
        try {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            update(delta);

            GameState gameState = client != null ? client.getGameState() : null;
            if (gameState != null && gameState.getPlayers() != null && !gameState.getPlayers().isEmpty()) {
                Gdx.app.log("RENDER", "Renderizando GameState con " + gameState.getPlayers().size() + " jugadores, " +
                    (gameState.getEnemies() != null ? gameState.getEnemies().size() : 0) + " enemigos, " +
                    (gameState.getBullets() != null ? gameState.getBullets().size() : 0) + " balas");

                // Actualizar EnemyBlock
                enemyBlock = new EnemyBlock(gameState.getEnemies(), 50);
                enemyBlock.setFont(font);

                // Asignar font a naves y balas
                for (Player player : gameState.getPlayers().values()) {
                    if (player.getNave() != null) {
                        player.getNave().setFont(font);
                        for (Bullet bullet : player.getNave().getBullets()) {
                            bullet.setFont(font);
                        }
                    }
                }
                if (gameState.getBullets() != null) {
                    for (Bullet bullet : gameState.getBullets()) {
                        bullet.setFont(font);
                    }
                }

                // Encontrar localPlayerId (respaldo)
                if (localPlayerId == -1) {
                    for (Player player : gameState.getPlayers().values()) {
                        localPlayerId = player.getId();
                        Gdx.app.log("RENDER", "Asignado localPlayerId de respaldo: " + localPlayerId);
                        break;
                    }
                }

                // Renderizar
                batch.setProjectionMatrix(camera.combined);
                try {
                    batch.begin();
                    Gdx.app.log("RENDER", "batch.begin llamado");
                    if (enemyBlock != null) {
                        try {
                            enemyBlock.drawPj(batch);
                            Gdx.app.log("RENDER", "enemyBlock.drawPj completado");
                        } catch (Exception e) {
                            Gdx.app.error("RENDER", "Error en enemyBlock.drawPj: " + e.getMessage(), e);
                        }
                    }
                    for (Player player : gameState.getPlayers().values()) {
                        if (player.getNave() != null) {
                            try {
                                player.getNave().drawPj(batch);
                                Gdx.app.log("RENDER", "nave.drawPj completado para jugador " + player.getId());
                            } catch (Exception e) {
                                Gdx.app.error("RENDER", "Error en nave.drawPj para jugador " + player.getId() + ": " + e.getMessage(), e);
                            }
                        }
                    }

                    // Mostrar Score y Lives
                    int y = 590;
                    for (Player player : gameState.getPlayers().values()) {
                        try {
                            font.draw(batch, "Score Player " + player.getId() + ": " + player.getScore(), 10, y);
                            y -= 20;
                            font.draw(batch, "Lives Nave Player " + player.getId() + ": " + player.getLives(), 10, y);
                            y -= 20;
                            Gdx.app.log("RENDER", "Texto dibujado para jugador " + player.getId());
                        } catch (Exception e) {
                            Gdx.app.error("RENDER", "Error dibujando texto para jugador " + player.getId() + ": " + e.getMessage(), e);
                        }
                    }
                } catch (Exception e) {
                    Gdx.app.error("RENDER", "Error en bloque de renderizado: " + e.getMessage(), e);
                } finally {
                    try {
                        batch.end();
                        Gdx.app.log("RENDER", "batch.end llamado");
                    } catch (Exception e) {
                        Gdx.app.error("RENDER", "Error en batch.end: " + e.getMessage(), e);
                    }
                }
            } else {
                Gdx.app.log("RENDER", "No se renderiza: gameState es null o players vacío");
            }
        } catch (Exception e) {
            Gdx.app.error("RENDER", "Error general en render: " + e.getMessage(), e);
        }
    }

    private void update(float delta) {
        if (client == null) return;
        try {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                client.sendCommand(new MoveCommand("left", delta));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                client.sendCommand(new MoveCommand("right", delta));
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                client.sendCommand(new ShootCommand());
            }
        } catch (Exception e) {
            Gdx.app.error("UPDATE", "Error en update: " + e.getMessage(), e);
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 800;
        camera.viewportHeight = 600;
        camera.update();
    }

    @Override
    public void dispose() {
        try {
            if (batch != null) batch.dispose();
            if (font != null) font.dispose();
            if (client != null) client.close();
        } catch (Exception e) {
            Gdx.app.error("DISPOSE", "Error al liberar recursos: " + e.getMessage(), e);
        }
    }
}
