package io.game.gamesobject.nave;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.game.gamesobject.Bullet;
import io.game.gamesobject.Pj;

public class Nave extends Pj {
    private int playerId;
    private BitmapFont font;
    private int type;
    private float shootCooldown; // Tiempo restante hasta el próximo disparo
    private static final float SHOOT_COOLDOWN = 0.5f; // 0.5 segundos entre disparos
    private List<Bullet> bullets;
    private int live = 3;
    private float speed = 200;

    public Nave(int playerId , int type, float x, float y, float speed) {
        super(new Vector2(x, y), speed);
        this.type = type;
        this.playerId = playerId;
        //this.font = new BitmapFont();
        this.bullets = new ArrayList<>();
        this.shootCooldown = 0; // Inicializa el tiempo de recarga
    }

    private String typeNave(int type) {
        return switch (type) {
            case 1 -> "      /  \\\n     /__\\\n  _/|    |\\_\n/__|__|__\\\n     /__\\";
            case 2 -> "   ";
            case 3 -> "  ";
            case 4 -> "  ";
            default -> "  ";
        };
    }

    @Override
    public void drawPj(SpriteBatch batch) {
        if(font != null){
            font.draw(batch, typeNave(type), getPosition().x, getPosition().y);

            for (Bullet bullet : bullets) {
                bullet.draw(batch);
            }
        }
    }

    @Override
    public void movePj(float delta) {
        // Mover a la izquierda si se presiona la flecha izquierda
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            getPosition().x -= getSpeed() * delta;
        }
        // Mover a la derecha si se presiona la flecha derecha
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            getPosition().x += getSpeed() * delta;
        }
        // Limitar la posición x para que la nave no salga de la pantalla
        getPosition().x = MathUtils.clamp(getPosition().x, 0, 800 - 50); // 50 es el ancho aproximado del diseño
    }

    @Override
    public void shoot(float delta,boolean shootTriggered) {
        shootCooldown -= delta;
        if (shootTriggered && shootCooldown <= 0) {
            // Disparar una bala desde el centro de la nave
            float bulletX = getPosition().x + 15; // Aproximadamente el centro del diseño
            float bulletY = getPosition().y + 15; // Justo encima de la nave
            bullets.add(new Bullet(bulletX, bulletY, speed, "|" , playerId));
            shootCooldown = SHOOT_COOLDOWN;
           // Gdx.app.log("NAVE", "Disparo en x: " + bulletX + ", y: " + bulletY);
        }
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setLive(int live) {
        this.live -= live;
    }

    public int getLive() {
        return live;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
        for (Bullet bullet : bullets) {
            bullet.setFont(font);
        }
    }
}
