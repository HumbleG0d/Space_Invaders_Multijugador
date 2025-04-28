package io.game.gamesobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IPj {
    void drawPj(SpriteBatch batch);

    void moveBlockPj(float delta, float blockSpeed);

    void movePj(float delta);

    void shoot(float delta, float speed);
}
