package io.game.gamesobject.enemigues;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class EnemyBlock {
    private List<Enemy> enemies;
    private float blockSpeed; // Velocidad del bloque entero
    private BitmapFont font;

    public EnemyBlock(List<Enemy> enemies, float initialSpeed) {
        this.enemies = enemies;
        this.blockSpeed = initialSpeed;
        System.out.println("EnemyBlock creado con " + this.enemies.size() + " enemigos");

    }

    public void drawPj(SpriteBatch batch) {
        System.out.println("Dibujando EnemyBlock con " + enemies.size() + " enemigos");
        for (Enemy enemy : enemies) {
            try {
                enemy.drawPj(batch);
                System.out.println("Enemigo dibujado en x=" + enemy.getPosition().x + ", y=" + enemy.getPosition().y);
            } catch (Exception e) {
                System.err.println("Error dibujando enemigo en x=" + enemy.getPosition().x + ", y=" + enemy.getPosition().y + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void movePj(float delta) {
        // Calcular los límites del bloque
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        for (Enemy enemy : enemies) {
            minX = Math.min(minX, enemy.getPosition().x);
            maxX = Math.max(maxX, enemy.getPosition().x);
        }

        // Mover el bloque entero
        for (Enemy enemy : enemies) {
            enemy.moveBlockPj(delta, blockSpeed);
        }

        // Verificar límites y ajustar dirección
        if (maxX >= 780) {
            // Bajar el bloque y ajustar posiciones
            for (Enemy enemy : enemies) {
                enemy.getPosition().y -= 5;
                enemy.getPosition().x -= (maxX - 780);
            }
            blockSpeed = -Math.abs(blockSpeed);
            //Gdx.app.log("UPDATE", "Bloque baja y retrocede, minX: " + minX + ", maxX: " + maxX);
        } else if (minX <= 20) {
            // Bajar el bloque y ajustar posiciones
            for (Enemy enemy : enemies) {
                enemy.getPosition().y -= 10;
                enemy.getPosition().x -= (minX - 20);
            }
            blockSpeed = Math.abs(blockSpeed);
            //Gdx.app.log("UPDATE", "Bloque baja y avanza, minX: " + minX + ", maxX: " + maxX);
        }
    }

    public void shoot(float delta, float speed) {
        // Determinar qué enemigos están en la fila más baja de cada columna
        List<Enemy> shooters = new ArrayList<>();
        for (int col = 0; col < 8; col++) {
            Enemy lowestEnemy = null;
            float lowestY = Float.MAX_VALUE;
            for (Enemy enemy : enemies) {
                // Verificar si el enemigo está en la columna actual (x ≈ 70 + col * 50)
                if (Math.abs(enemy.getPosition().x - (70 + col * 50)) < 5) {
                    if (enemy.getPosition().y < lowestY) {
                        lowestY = enemy.getPosition().y;
                        lowestEnemy = enemy;
                    }
                }
            }
            if (lowestEnemy != null) {
                shooters.add(lowestEnemy);
            }
        }

        // Hacer que los enemigos en la fila más baja disparen
        for (Enemy shooter : shooters) {
            shooter.shoot(delta, speed);
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
        for (Enemy enemy : enemies) {
            enemy.setFont(font);
            System.out.println("Asignado font a enemigo en x=" + enemy.getPosition().x + ", y=" + enemy.getPosition().y);
        }
    }
}
