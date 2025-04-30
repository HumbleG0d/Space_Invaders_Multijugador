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
        Gdx.app.log("GAME_MULTIPLAYER", "Constructor called");
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);
        this.camera.update();

        // Inicializar fuente
        try {
            font = new BitmapFont(Gdx.files.internal("courier.fnt"));
            font.getData().setScale(0.5f);
            Gdx.app.log("GAME_MULTIPLAYER", "Fuente monoespaciada cargada");
        } catch (Exception e) {
            Gdx.app.error("FONT", "Error al cargar fuente: " + e.getMessage(), e);
            font = new BitmapFont();
            font.getData().setScale(0.5f);
        }

        // Conectar al servidor
        try {
            client = new GameClient("localhost", 5000);
            localPlayerId = client.getLocalPlayerId();
        } catch (IOException | ClassNotFoundException e) {
            Gdx.app.error("NETWORK", "Error al conectar al servidor: " + e.getMessage(), e);
            localPlayerId = -1;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        GameState gameState = client.getGameState();
        if (gameState != null && !gameState.getPlayers().isEmpty()) {
            // Actualizar EnemyBlock
            enemyBlock = new EnemyBlock(gameState.getEnemies(), 50);

            // Asignar font a naves y balas
            for (Player player : gameState.getPlayers().values()) {
                player.getNave().setFont(font);
                for (Bullet bullet : player.getNave().getBullets()) {
                    bullet.setFont(font);
                }
            }
            for (Bullet bullet : gameState.getBullets()) {
                bullet.setFont(font);
            }

            // Encontrar localPlayerId (respaldo si no se asignó)
            if (localPlayerId == -1) {
                for (Player player : gameState.getPlayers().values()) {
                    localPlayerId = player.getId();
                    break;
                }
            }

            // Renderizar
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            enemyBlock.drawPj(batch);
            for (Player player : gameState.getPlayers().values()) {
                player.getNave().drawPj(batch);
            }

            // Mostrar Score y Lives para cada jugador
            int y = 590; // Comenzar en la parte superior izquierda
            for (Player player : gameState.getPlayers().values()) {
                font.draw(batch, "Score Player " + player.getId() + ": " + player.getScore(), 10, y);
                y -= 20; // Separación entre líneas
                font.draw(batch, "Lives Nave Player " + player.getId() + ": " + player.getLives(), 10, y);
                y -= 20;
            }

            batch.end();
        }
    }

    private void update(float delta) {
        // Enviar comandos al servidor
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            client.sendCommand(new MoveCommand("left", delta));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            client.sendCommand(new MoveCommand("right", delta));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            client.sendCommand(new ShootCommand());
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
        batch.dispose();
        if (font != null) {
            font.dispose();
        }
        if (client != null) {
            client.close();
        }
    }
}
