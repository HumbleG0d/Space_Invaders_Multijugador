package io.game.lwjgl3;

import io.game.MainGame;
import io.game.screens.GameSingleScreen;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class AtencionCliente extends Thread {

    private final ServidorJuego padre;
    private final Socket conexion;
    private final int nCliente;
    private DataOutputStream salidaCliente;
    private String nombre;
    private GameSingleScreen gameSingleScreen;
    private MainGame mainGame;

    public AtencionCliente(ServidorJuego padre,MainGame mainGame, Socket conexion, int nCliente) {
        this.padre = padre;
        this.conexion = conexion;
        this.nCliente = nCliente;
        this.salidaCliente = null;
        this.mainGame=mainGame;
    }

    @Override
    public void run() {
        DataOutputStream salidaCliente;
        BufferedReader entradaCliente;
        String mensaje;

        try {
            // Se obtienen los flujos de entrada y salida al cliente
            salidaCliente = new DataOutputStream(conexion.getOutputStream());
            entradaCliente = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

            // Se recibe el primer mensaje del cliente
            mensaje = entradaCliente.readLine();
            System.out.println("Mensaje cliente " + nCliente + ": " + mensaje);

            // Enviamos un mensaje al cliente
            salidaCliente.writeUTF("Conexión aceptada. Esperando instrucción\n");

            // Se ejecuta mientras haya mensajes del cliente
            while ((mensaje = entradaCliente.readLine()) != null) {
                nombre = mensaje.substring(2, mensaje.length());

                System.out.println("Mensaje cliente " + nCliente + ": " + nombre);
                switch (nombre){
                    case "1":
                        gameSingleScreen = mainGame.damePantalla().getSingleScreen();
                        com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                            gameSingleScreen.crearNave();
                        });
                        break;
                    case "a":
                        com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                            gameSingleScreen.moverNaveIzquierda(nCliente);
                        });
                        break;
                    case "d":
                        com.badlogic.gdx.Gdx.app.postRunnable(() -> {
                            gameSingleScreen.moverNaveDerecha(nCliente);
                        });
                        break;
                    default:
                        System.out.println("Comando desconocido: " + nombre);                }




            }

            System.out.println("Fin de conexión del cliente " + nCliente);

            salidaCliente.close();
            entradaCliente.close();
            conexion.close();
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
