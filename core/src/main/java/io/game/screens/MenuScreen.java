package io.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.game.Main;

public class MenuScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Texture background;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    public MenuScreen(Main game) {
        this.game = game;
        this.batch = game.getBatch();
        this.camera = new OrthographicCamera();
        this.stage = new Stage(new FitViewport(800, 600, camera));

        setupBackground();
        setupButtons();

        Gdx.input.setInputProcessor(stage);
    }
    private void setupBackground() {
        Texture backgroundTexture = new Texture(Gdx.files.internal("assets/space_invaders.png"));
        Image backgroundImage = new Image(new TextureRegionDrawable(backgroundTexture));
        backgroundImage.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        backgroundImage.setPosition(0, 0);
        stage.addActor(backgroundImage);
    }

    private void setupButtons() {
        //Creacion del boton single player
        Texture singleTexture = new Texture(Gdx.files.internal("assets/singleplayer.png"));
        ImageButton buttonSinglePlayer = new ImageButton(new TextureRegionDrawable(singleTexture));
        buttonSinglePlayer.setPosition(200, 100);
        buttonSinglePlayer.setTransform(true);
        buttonSinglePlayer.setScale(1.3f,0.8f);

        //Creacion del boton multiplayer player
        Texture multyTexture = new Texture(Gdx.files.internal("assets/multyplayer.png"));
        ImageButton buttonMultyPlayer = new ImageButton(new TextureRegionDrawable(multyTexture));
        buttonMultyPlayer.setPosition(450, 100);
        buttonMultyPlayer.setTransform(true);
        buttonMultyPlayer.setScale(1.3f , 0.8f);

        //Listener los botones
        buttonSinglePlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameSingleScreen(game));
            }
        });

        //TODO: Agregar la parte de multiplayer

        //Añadir los botones al stage
        stage.addActor(buttonSinglePlayer);
        stage.addActor(buttonMultyPlayer);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Dibujar fondo

        //Dibujar los botones
        stage.act(v);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update( width, height, true );
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }
}
