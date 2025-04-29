package io.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.game.screens.MenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MainGame extends Game {

    private SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;
    public MenuScreen menuScreen;
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        setScreen(menuScreen=new MenuScreen(this));
    }
    public MenuScreen damePantalla(){
        return this.menuScreen;
    }

    public void render() {
        super.render();
    }

    public SpriteBatch getBatch(){
        return batch;
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
