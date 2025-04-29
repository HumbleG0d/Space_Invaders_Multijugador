package io.game.cliente;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private final String host;
    private final int puerto;
    private final Socket conexion;

    public Cliente(String host, int puerto) throws Exception {
        this.host = host;
        this.puerto = puerto;
        conexion = new Socket(host, puerto);
    }

    public String getHost() {
        return host;
    }

    public int getPuerto() {
        return puerto;
    }

    private char mostrarMenu() {
        Scanner teclado = new Scanner(System.in);

        System.out.println("****************");
        System.out.println("Menú de opciones");
        System.out.println("****************");
        System.out.println("1- Enviar saludo");
        System.out.println("a- Mover a la izquierda");
        System.out.println("b- Mover a la derecha");
        System.out.print("Indique su opción: ");

        return teclado.next().charAt(0);

    }

    public void startClient() throws Exception {
        DataOutputStream salidaServidor;
        BufferedReader entradaServidor;
        String mensaje;
        boolean seguir = true;
        char operacion;

        // Se obtienen los flujos de entrada y salida al servidor
        salidaServidor = new DataOutputStream(conexion.getOutputStream());
        entradaServidor = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

        // Se envía mensaje al servidor
        salidaServidor.writeUTF("Iniciando transmisión al servidor\n");

        // Se recibe mensaje del servidor
        mensaje = entradaServidor.readLine();
        System.out.println("Mensaje servidor: " + mensaje);


        do {
            operacion = mostrarMenu();
            switch (operacion) {
                case 'a':
                case 'd':
                    salidaServidor.writeUTF(String.valueOf(operacion) + "\n");
                    break;
                case '1':
                    salidaServidor.writeUTF("1\n");
                    break;
                case '2':
                    seguir = false;
                    break;
                default:
                    System.out.println("Opción inexistente");
                    break;
            }
        } while (seguir);

        entradaServidor.close();
        salidaServidor.close();
        conexion.close();
    }
}
