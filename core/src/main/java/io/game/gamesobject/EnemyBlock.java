package io.game.gamesobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.List;

public class EnemyBlock {
    private List<Enemy> enemies;
    private float blockSpeed; // Velocidad del bloque entero

    public EnemyBlock(List<Enemy> enemies, float initialSpeed) {
        this.enemies = enemies;
        this.blockSpeed = initialSpeed;
    }

    public void drawPj(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            enemy.drawPj(batch);
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
            Gdx.app.log("UPDATE", "Bloque baja y retrocede, minX: " + minX + ", maxX: " + maxX);
        } else if (minX <= 20) {
            // Bajar el bloque y ajustar posiciones
            for (Enemy enemy : enemies) {
                enemy.getPosition().y -= 10;
                enemy.getPosition().x -= (minX - 20);
            }
            blockSpeed = Math.abs(blockSpeed);
            Gdx.app.log("UPDATE", "Bloque baja y avanza, minX: " + minX + ", maxX: " + maxX);
        }
    }
}