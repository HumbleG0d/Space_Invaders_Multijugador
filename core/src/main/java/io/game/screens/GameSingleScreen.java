package io.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.game.MainGame;
import io.game.gamesobject.Bullet;
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
    private List<Nave> lasNaves = new ArrayList<>();

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
        //this.nave = new Nave(1, 365, 100, 50);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        enemyBlock.drawPj(batch);
        if(lasNaves.isEmpty()){

        }else {
            for (Nave nave : lasNaves) {
                nave.drawPj(batch);
            }
        }
        batch.end();
    }

    private void update(float delta) {
        enemyBlock.movePj(delta);

        if (lasNaves.isEmpty()){

        }
        else {

            for (Nave nave : lasNaves) {
                nave.movePj(delta);
                nave.shoot(delta, 200); // Cada nave dispara con velocidad 200
            }

            updateBulletsAndCollisions(delta);
            enemyBlock.shoot(delta, 100); // Velocidad de las balas enemigas

        }
        // Actualizar disparos

        // Actualizar balas y detectar colisiones
    }

    private void updateBulletsAndCollisions(float delta) {
        // Actualizar balas de la nave
        for (Nave nave : lasNaves) {
            List<Bullet> naveBullets = new ArrayList<>(nave.getBullets());
            for (Bullet bullet : naveBullets) {
                bullet.update(delta);
                if (bullet.isOutOfScreen()) {
                    nave.getBullets().remove(bullet);
                    continue;
                }
                // Verificar colisiones con enemigos
                for (Enemy enemy : new ArrayList<>(enemyBlock.getEnemies())) {
                    if (isCollision(bullet, enemy)) {
                        enemyBlock.getEnemies().remove(enemy);
                        nave.getBullets().remove(bullet);
                        Gdx.app.log("COLLISION",
                            "Enemigo eliminado en x: " + enemy.getPosition().x + ", y: " + enemy.getPosition().y);
                        break;
                    }
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
            }
        }
    }

    private boolean isCollision(Bullet bullet, Enemy enemy) {
        // Detectar colisión si la bala está dentro de un rango de 20 píxeles del enemigo
        float dx = bullet.getPosition().x - enemy.getPosition().x;
        float dy = bullet.getPosition().y - enemy.getPosition().y;
        return Math.sqrt(dx * dx + dy * dy) < 20;
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 800;
        camera.viewportHeight = 600;
        camera.update();
    }

    public void crearNave(){
        Nave nave=new Nave(1, 365, 100, 50);
        lasNaves.add(nave);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }


    public void moverNaveIzquierda(int i) {
        if (!lasNaves.isEmpty()) {
            lasNaves.get(i-1).translateX(-10); // O el valor que desees
        }
    }

    public void moverNaveDerecha(int i) {
        if (!lasNaves.isEmpty()) {
            lasNaves.get(i-1).translateX(10); // O el valor que desees
        }
    }

}
