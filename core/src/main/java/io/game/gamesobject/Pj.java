package io.game.gamesobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Pj implements IPj {
    private Vector2 position;
    float speed;

    public Vector2 getPosition() {
        return position;
    }
    
    public float getSpeed() {
        return speed;
    }

    public Pj(Vector2 position, float speed) {
        this.position = position;
        this.speed = speed;
    }

    @Override
    public void drawPj(SpriteBatch batch) {
    }

    @Override
    public void moveBlockPj(float delta, float blockSpeed) {

    }

    @Override
    public void movePj(float delta) {
    }

    @Override
    public void shoot(float delta, float speed) {
    }
    
    
}
