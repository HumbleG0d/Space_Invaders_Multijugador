package io.game.servidor;


import io.game.comun.MensajeEstadoJuego;

import java.io.*;
import java.net.Socket;

public class ClienteConectado implements Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClienteConectado(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in  = new ObjectInputStream(socket.getInputStream());

            while (true) {
                String comando = (String) in.readObject();
                procesarComando(comando);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void procesarComando(String comando) {
        System.out.println("Comando recibido: " + comando);
        // actualizar aquí la nave de este cliente según comando ("MOVE_LEFT", etc)
    }

    public void enviarEstado(MensajeEstadoJuego estado) {
        try {
            out.writeObject(estado);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
