package io.game.gamesobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Nave extends Pj {
    
    private BitmapFont font;
    private int type;
    

    public Nave(int type, float x, float y, float speed) {
        super(new Vector2(x, y), speed);
        this.type = type;
        this.font = new BitmapFont(); // Inicializa la fuente aquí o pásala como parámetro
        font.getData().setScale(0.5f);
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
        font.draw(batch, typeNave(type), getPosition().x, getPosition().y);
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

     
}