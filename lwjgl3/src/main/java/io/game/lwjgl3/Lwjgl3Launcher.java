package io.game.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.game.MainGame;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;

        try {
            MainGame juego = new MainGame(); // Crear instancia del juego

            // Iniciar el servidor con acceso al juego
            ServidorJuego servidor = new ServidorJuego(juego, 5000);
            servidor.start();

            // Ejecutar la aplicación gráfica
            createApplication(juego);

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static Lwjgl3Application createApplication(MainGame juego) {
        return new Lwjgl3Application(juego, getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Space_Invaders");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowedMode(640, 427);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}
