package io.game.gamesobject.enemigues;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.game.gamesobject.Bullet;
import io.game.gamesobject.Pj;

public class Enemy extends Pj {

    private int type;
    private BitmapFont font;
    private List<Bullet> bullets;
    private static final Random random = new Random();

    public Enemy(int type, float x, float y, float speed) {
        super(new Vector2(x, y), speed);
        this.type = type;
        this.bullets = new ArrayList<>();
    }

    public int getType(){
        return type;
    }

    private String typeEnemigue(int type) {
        return switch (type) {
            case 1 -> "  [o][o]\n   [][][]\n [= || =]";
            case 2 -> "   (^^^)\n  <|||||||>\n  (o   o)\n   \\_-_/";
            case 3 -> "   o^^o\n  /###\\ \n (#[o]#)";
            case 4 -> "  +-+-+\n<|O O|>\n|=====|\n  \\_|o|_/";
            default -> "  ----  \n -****- \n-******-\n -****- \n  ---- ";
        };
    }

    public int pointTypeEnemigue(int type) {
        return switch (type){
            case 1 -> 60;
            case 2 -> 50;
            case 3 -> 40;
            case 4 -> 30;
            default -> 0;
        };
    }

     @Override
    public void shoot(float delta, float speed) {
        // Disparar con 1% de probabilidad por frame
        if (random.nextFloat() < 0.01f) {
            float bulletX = getPosition().x + 25; // Centro del enemigo
            float bulletY = getPosition().y - 20; // Debajo del enemigo
            bullets.add(new Bullet(bulletX, bulletY, -speed, "|" , 0));
           // Gdx.app.log("ENEMY", "Enemigo tipo " + type + " dispara en x: " + bulletX + ", y: " + bulletY);
        }
    }

    public List<Bullet> getBullets() {
        return bullets;
    }



    @Override
    public void drawPj(SpriteBatch batch) {
        String[] lines = typeEnemigue(type).split("\n");
        float y = getPosition().y;
        for (String line : lines) {
            font.draw(batch, line, getPosition().x, y);
            y -= font.getLineHeight() * 1.2f;
        }
        for (Bullet bullet : bullets) {
            bullet.draw(batch);
        }
    }

    @Override
    public void moveBlockPj(float delta, float blockSpeed) {
        getPosition().x += blockSpeed * delta;
    }

}
