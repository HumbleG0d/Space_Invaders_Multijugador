package io.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.game.MainGame;

public class MenuScreen extends AbstractScreen {

    private Stage stage;
    private Texture background;
    private OrthographicCamera camera;

    public MenuScreen(MainGame game) {
        super(game);
        this.camera = new OrthographicCamera();
        this.stage = new Stage(new FitViewport(800, 600, camera));

        setupBackground();
        setupButtons();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    private void setupBackground() {
        Texture backgroundTexture = new Texture(Gdx.files.internal("space_invaders.png"));
        Image backgroundImage = new Image(new TextureRegionDrawable(backgroundTexture));
        backgroundImage.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        backgroundImage.setPosition(0, 0);
        stage.addActor(backgroundImage);
    }

    private void setupButtons() {
        //Creacion del boton play
        Texture singleTexture = new Texture(Gdx.files.internal("Play.png"));
        ImageButton buttonSinglePlayer = new ImageButton(new TextureRegionDrawable(singleTexture));
        buttonSinglePlayer.setPosition(240, 90);
        buttonSinglePlayer.setTransform(true);
        buttonSinglePlayer.setScale(1.3f,0.8f);

        //Listener boton
        buttonSinglePlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MENU", "Single Player button clicked");
                game.setScreen(new GameMultiplayerScreen(game));
            }
        });

        //Añadir el boton
        stage.addActor(buttonSinglePlayer);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose(){
        super.dispose();
        background.dispose();
    }


}
