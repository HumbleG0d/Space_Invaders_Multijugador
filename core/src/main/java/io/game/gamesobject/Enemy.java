package io.game.gamesobject;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Pj {
    
    private int type;
    private BitmapFont font;
   
    public Enemy(int type, float x, float y, float speed) {
        super(new Vector2(x, y), speed);
        this.type = type;
        font = new BitmapFont(); // Inicializa la fuente aquí o pásala como parámetro
        font.getData().setScale(0.5f);
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

    @Override
    public void drawPj(SpriteBatch batch) {
        String[] lines = typeEnemigue(type).split("\n");
        float y = getPosition().y;
        for (String line : lines) {
            font.draw(batch, line, getPosition().x, y);
            y -= font.getLineHeight() * 1.2f;
        }
       
    }

    @Override
    public void moveBlockPj(float delta, float blockSpeed) {
        getPosition().x += blockSpeed * delta;
    }

}