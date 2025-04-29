package io.game.servidor;


import io.game.comun.MensajeEstadoJuego;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServidorJuego {

    private static final int PUERTO = 5000;
    private static CopyOnWriteArrayList<ClienteConectado> clientes = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        new Thread(ServidorJuego::aceptarClientes).start();
        new Thread(ServidorJuego::broadcastEstadoJuego).start();
    }

    private static void aceptarClientes() {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado en puerto " + PUERTO);

            while (true) {
                Socket socket = serverSocket.accept();
                ClienteConectado cliente = new ClienteConectado(socket);
                clientes.add(cliente);
                new Thread(cliente).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcastEstadoJuego() {
        while (true) {
            try {
                Thread.sleep(50); // 20 updates por segundo
                MensajeEstadoJuego estado = new MensajeEstadoJuego();

                // aquí deberías recopilar el estado real de las naves y disparos
                // (lo implementamos en ClienteConectado)

                for (ClienteConectado cliente : clientes) {
                    cliente.enviarEstado(estado);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
