package io.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.game.Main;

public class GameSingleScreen implements Screen {
    private final Main game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    public GameSingleScreen(Main game) {
        this.game = game;
        this.batch = game.getBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600); // Mismo tamaño que tu ventana
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Limpia la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualiza la cámara
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Dibuja el juego
        batch.begin();
        // Aquí iría la lógica de dibujado (jugador, enemigos, etc.)
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
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
        batch.dispose();
    }
}
