package io.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.game.MainGame;

public class EndGameScreen extends AbstractScreen{

    private Stage stage;

    public EndGameScreen(MainGame game) {
        super(game);
        this.stage = new Stage(new FitViewport(800, 600));
        setUpBackground();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0 , 0 , 0 ,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    private void setUpBackground() {
        Texture backgroundTexture = new Texture(Gdx.files.internal("game_over.jpg"));
        Image backgroundImage = new Image(new TextureRegionDrawable(backgroundTexture));
        backgroundImage.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        backgroundImage.setPosition(0, 0);
        stage.addActor(backgroundImage);
    }

    @Override
    public void dispose(){
        stage.dispose();
    }

}
