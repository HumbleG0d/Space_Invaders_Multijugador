package io.game.lwjgl3;

import io.game.MainGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorJuego extends Thread{
    private final int puerto;
    private final ServerSocket serverSocket;
    private final boolean continuar;
    private int nClientes;
    private MainGame mainGame;
    private Lwjgl3Launcher motor;
    public ServidorJuego(MainGame mainGame,int puerto) throws Exception {
        this.puerto = puerto;
        this.serverSocket = new ServerSocket(puerto);
        this.continuar = true;
        this.nClientes = 0;
        this.mainGame = mainGame;

    }


    @Override
    public void run() {
        Socket clientSocket;

        try {

            while (continuar) {
                System.out.println("Esperando un nuevo cliente");

                // Esperar una conexión con un cliente
                clientSocket = serverSocket.accept();

                nClientes++;
                AtencionCliente cliente = new AtencionCliente(this,this.mainGame, clientSocket, nClientes);
                cliente.start();
            }

        } catch (IOException ex) {
            if (continuar) {
                System.out.println("Se ha producido un error inesperado: " + ex.getMessage());
            }
            return;
        }
    }

    private void manejarCliente(Socket clienteSocket) {
        try {
            boolean continuar = true;
            System.out.println("Manejando la comunicación con el cliente " + clienteSocket.getInetAddress());
            clienteSocket.getOutputStream().write("Bienvenido al servidor de juego!".getBytes());

            while(continuar){

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
