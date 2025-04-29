package io.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import io.game.comun.MensajeEstadoJuego;
import io.game.comun.NaveEstado;

import java.util.Map;

public class GameScreenCliente extends ScreenAdapter {

    private SpriteBatch batch;
    private Texture naveTexture;

    public GameScreenCliente() {
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        actualizar();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //dibujarNaves();
        batch.end();
    }

    private void actualizar() {
        // Aquí puedes manejar inputs locales si quieres
    }

  /*  private void dibujarNaves() {
        MensajeEstadoJuego estado = ClienteJuego.get().getUltimoEstado();
        if (estado == null) return;

        for (Map.Entry<String, NaveEstado> entry : estado.naves.entrySet()) {
            NaveEstado nave = entry.getValue();
            batch.draw(naveTexture, nave.x, nave.y, 32, 32);
        }
    }*/

    @Override
    public void dispose() {
        batch.dispose();
        naveTexture.dispose();
    }
}
