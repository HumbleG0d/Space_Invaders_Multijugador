package io.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.game.MainGame;
import io.game.gamesobject.Bullet;
import io.game.gamesobject.CollisionManager;
import io.game.gamesobject.enemigues.Enemy;
import io.game.gamesobject.enemigues.EnemyBlock;
import io.game.gamesobject.nave.Nave;

import java.util.ArrayList;
import java.util.List;

public class GameSingleScreen extends AbstractScreen {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private EnemyBlock enemyBlock;
    private Nave nave;
    private int score = 0;

    public GameSingleScreen(MainGame game) {
        super(game);
        Gdx.app.log("GAME_SINGLE", "Constructor called");
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);
        this.camera.update();
        initializeResources();
    }

    private void initializeResources() {

        font = new BitmapFont();
        font.getData().setScale(2f);

        // Inicializar enemigos (4 filas x 8 columnas)
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            enemies.add(new Enemy(1, 70 + i * 50, 480, 50));
            enemies.add(new Enemy(2, 70 + i * 50, 440, 50));
            enemies.add(new Enemy(3, 70 + i * 50, 390, 50));
            enemies.add(new Enemy(4, 70 + i * 50, 340, 50));
        }
        this.enemyBlock = new EnemyBlock(enemies, 50);

        // Inicializar nave
        this.nave = new Nave(1, 365, 100, 50);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        enemyBlock.drawPj(batch);
        nave.drawPj(batch);

        viewScore();
        batch.end();
    }

    private void update(float delta) {
        enemyBlock.movePj(delta);
        nave.movePj(delta);

        // Actualizar disparos
        enemyBlock.shoot(delta, 100); // Velocidad de las balas enemigas
        nave.shoot(delta, 200); // Velocidad de las balas de la nave

        // Actualizar balas y detectar colisiones
        updateBulletsAndCollisions(delta);
        //actualiceScore();
    }

    private void updateBulletsAndCollisions(float delta) {
        // Actualizar balas de la nave
        List<Bullet> naveBullets = new ArrayList<>(nave.getBullets());
        for (Bullet bullet : naveBullets) {
            bullet.update(delta);
            if (bullet.isOutOfScreen()) {
                nave.getBullets().remove(bullet);
                continue;
            }
            // Verificar colisiones con enemigos
            for (Enemy enemy : new ArrayList<>(enemyBlock.getEnemies())) {
                if (CollisionManager.isCollision(bullet, enemy)) {
                    enemyBlock.getEnemies().remove(enemy);
                    nave.getBullets().remove(bullet);
                    score += enemy.pointTypeEnemigue(enemy.getType());
                    Gdx.app.log("COLLISION",
                            "Enemigo eliminado en x: " + enemy.getPosition().x + ", y: " + enemy.getPosition().y);
                    break;
                }
            }
        }

        // Actualizar balas de los enemigos
        for (Enemy enemy : new ArrayList<>(enemyBlock.getEnemies())) {
            List<Bullet> enemyBullets = new ArrayList<>(enemy.getBullets());
            for (Bullet bullet : enemyBullets) {
                bullet.update(delta);
                if (bullet.isOutOfScreen()) {
                    enemy.getBullets().remove(bullet);
                }
                // Verificar colisión con la nave
                if (nave.getIsAlive() && CollisionManager.isCollision(bullet, nave)) {
                    nave.setIsAlive(false);
                    enemy.getBullets().remove(bullet);
                    Gdx.app.log("COLLISION", "Nave destruida en x: " + nave.getPosition().x + ", y: " + nave.getPosition().y);
                    //Mostrar interfaz de fin de juego
                    game.setScreen(new EndGameScreen(game));
                    break;
                }
            }
        }
    }

    private void viewScore(){
        font.draw(batch , "YOU" , 100 , 580);
        font.draw(batch , String.valueOf(score), 120 , 550);
        font.draw(batch , "WINNER" , 300 , 580);
        font.draw(batch , "OPPONENT" , 500 , 580);
        //Agregar la actualizacion de los puntajes
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
        font.dispose();
    }
}
