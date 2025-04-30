package io.game.gamesobject;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 position;
    private float speed; // Velocidad vertical (positiva para nave, negativa para enemigos)
    private String design;
    private BitmapFont font;
    private int ownerId;

    public Bullet(float x, float y, float speed, String design,int ownerId) {
        this.position = new Vector2(x, y);
        this.speed = speed;
        this.design = design;
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void update(float delta) {
        position.y += speed * delta;
    }

    public void draw(SpriteBatch batch) {
        font.draw(batch, design, position.x, position.y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isOutOfScreen() {
        return position.y > 600 || position.y < 0;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }
}
