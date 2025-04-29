package io.game.cliente;

public class MainCliente {
    public static void main(String[] args) {
        System.out.println("Iniciado el cliente");

        try {
            Cliente cliente = new Cliente("127.0.0.1", 5000);
            cliente.startClient();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
