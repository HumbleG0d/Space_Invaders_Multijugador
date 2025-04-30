package io.game.server;

import io.game.GameState;
import io.game.gamesobject.Bullet;
import io.game.gamesobject.enemigues.Enemy;
import io.game.gamesobject.enemigues.EnemyBlock;
import io.game.gamesobject.nave.Nave;
import io.game.player.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.game.gamesobject.CollisionManager.isCollision;

public class GameServer implements Runnable {
    private GameState gameState;
    private EnemyBlock enemyBlock;
    private ServerSocket servidor;
    private final int PUERTO = 5000;
    private List<Enemy> enemies;
    private Map<Integer, Player> players;
    private Map<Integer, ClientHandler> clientHandlers;
    private int numClients;
    private volatile boolean running;

    public GameServer() {
        this.gameState = new GameState();
        this.enemies = new ArrayList<>();
        this.players = new ConcurrentHashMap<>();
        this.clientHandlers = new ConcurrentHashMap<>();
        this.numClients = 0;
        this.running = true;
    }

    @Override
    public void run() {
        try {
            servidor = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado en puerto " + PUERTO);
            initialEnemigues();

            new Thread(this::updateGameStateLoop).start();

            while (running) {
                Socket cliente = servidor.accept();
                handleClient(cliente);
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeServer();
        }
    }

    private void initialEnemigues() {
        for (int i = 0; i < 8; i++) {
            enemies.add(new Enemy(1, 70 + i * 50, 480, 50));
            enemies.add(new Enemy(2, 70 + i * 50, 440, 50));
            enemies.add(new Enemy(3, 70 + i * 50, 390, 50));
            enemies.add(new Enemy(4, 70 + i * 50, 340, 50));
        }
        gameState.setEnemies(enemies);
        enemyBlock = new EnemyBlock(enemies, 50);
    }

    private void updateGameStateLoop() {
        while (running) {
            synchronized (gameState) {
                updateGameState(1/60f);
            }
            sendGameStateToClients();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGameState(float delta) {
        enemyBlock.movePj(delta);
        enemyBlock.shoot(delta, 100);

        List<Bullet> bullets = gameState.getBullets();
        if (bullets != null) {
            for (Bullet bullet : new ArrayList<>(bullets)) {
                bullet.update(delta);
                if (bullet.isOutOfScreen()) {
                    bullets.remove(bullet);
                }
            }
        }

        if (bullets != null) {
            for (Bullet bullet : new ArrayList<>(bullets)) {
                for (Enemy enemy : new ArrayList<>(gameState.getEnemies())) {
                    if (isCollision(bullet, enemy)) {
                        gameState.getEnemies().remove(enemy);
                        bullets.remove(bullet);
                        if (bullet.getOwnerId() != -1) {
                            Player player = players.get(bullet.getOwnerId());
                            if (player != null) {
                                player.setScore(player.getScore() + enemy.pointTypeEnemigue(enemy.getType()));
                                System.out.println("Jugador " + player.getId() + " eliminó enemigo tipo " + enemy.getType() + ", puntaje: " + player.getScore());
                            }
                        }
                        break;
                    }
                }
                for (Player player : new ArrayList<>(players.values())) {
                    if (player.getLives() > 0 && isCollision(bullet, player.getNave())) {
                        player.setLives(player.getLives() - 1);
                        bullets.remove(bullet);
                        System.out.println("Jugador " + player.getId() + " dañado, vidas: " + player.getLives());
                        break;
                    }
                }
            }
        }
    }

    private void handleClient(Socket client) {
        try {
            numClients++;
            int playerId = numClients;
            System.out.println("Creando nave para playerId " + playerId);
            Nave nave = new Nave(playerId, 1, 365 + (playerId - 1) * 50, 100, 50);
            Player player = new Player(playerId, nave);
            players.put(playerId, player);
            gameState.setPlayers(players);
            System.out.println("Jugador " + playerId + " se ha unido al juego");

            ClientHandler clientHandler = new ClientHandler(client, playerId);
            clientHandlers.put(playerId, clientHandler);
            new Thread(clientHandler).start();

            clientHandler.sendGameState(gameState);
        } catch (IOException e) {
            System.err.println("Error manejando cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendGameStateToClients() {
        synchronized (gameState) {
            List<Integer> disconnectedClients = new ArrayList<>();
            for (Map.Entry<Integer, ClientHandler> entry : clientHandlers.entrySet()) {
                int playerId = entry.getKey();
                ClientHandler handler = entry.getValue();
                if (handler.isActive()) {
                    try {
                        handler.sendGameState(gameState);
                    } catch (Exception e) {
                        System.err.println("Fallo al enviar GameState a cliente " + playerId + ": " + e.getMessage());
                        disconnectedClients.add(playerId);
                    }
                } else {
                    disconnectedClients.add(playerId);
                }
            }
            for (int playerId : disconnectedClients) {
                ClientHandler handler = clientHandlers.remove(playerId);
                if (handler != null) {
                    handler.close();
                }
                players.remove(playerId);
            }
        }
    }

    private void closeServer() {
        running = false;
        try {
            for (ClientHandler handler : clientHandlers.values()) {
                handler.close();
            }
            if (servidor != null && !servidor.isClosed()) {
                servidor.close();
            }
        } catch (IOException e) {
            System.err.println("Error cerrando servidor: " + e.getMessage());
        }
    }

    private String toJSON(GameState state) {
        try {
            JSONObject json = new JSONObject();
            JSONArray playersArray = new JSONArray();
            for (Player player : state.getPlayers().values()) {
                JSONObject playerJson = new JSONObject();
                playerJson.put("id", player.getId());
                playerJson.put("x", player.getNave().getPosition().x);
                playerJson.put("y", player.getNave().getPosition().y);
                playerJson.put("lives", player.getLives());
                playerJson.put("score", player.getScore());
                playersArray.put(playerJson);
            }
            json.put("players", playersArray);

            JSONArray enemiesArray = new JSONArray();
            for (Enemy enemy : state.getEnemies()) {
                JSONObject enemyJson = new JSONObject();
                enemyJson.put("x", enemy.getPosition().x);
                enemyJson.put("y", enemy.getPosition().y);
                enemyJson.put("type", enemy.getType());
                enemiesArray.put(enemyJson);
            }
            json.put("enemies", enemiesArray);

            JSONArray bulletsArray = new JSONArray();
            List<Bullet> bullets = state.getBullets();
            if (bullets != null) {
                for (Bullet bullet : bullets) {
                    JSONObject bulletJson = new JSONObject();
                    bulletJson.put("x", bullet.getPosition().x);
                    bulletJson.put("y", bullet.getPosition().y);
                    bulletJson.put("speed", bullet.getSpeed());
                    bulletJson.put("ownerId", bullet.getOwnerId());
                    bulletJson.put("design", bullet.getDesign());
                    bulletsArray.put(bulletJson);
                }
            }
            json.put("bullets", bulletsArray);

            String jsonString = json.toString();
            System.out.println("JSON generado: " + jsonString);
            return jsonString;
        } catch (Exception e) {
            System.err.println("Error generando JSON: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Fallo al generar JSON", e);
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private int playerId;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private volatile boolean active;

        public ClientHandler(Socket socket, int playerId) throws IOException {
            this.socket = socket;
            this.playerId = playerId;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.active = true;
            out.writeObject(playerId);
            out.flush();
            System.out.println("Enviado playerId " + playerId + " al cliente");
        }

        public boolean isActive() {
            return active;
        }

        @Override
        public void run() {
            try {
                while (active) {
                    Object command = in.readObject();
                    synchronized (gameState) {
                        Player player = players.get(playerId);
                        if (player == null || player.getLives() <= 0) continue;

                        if (command instanceof MoveCommand) {
                            MoveCommand cmd = (MoveCommand) command;
                            if (cmd.direction.equals("left")) {
                                player.getNave().getPosition().x -= player.getNave().getSpeed() * cmd.delta;
                            } else if (cmd.direction.equals("right")) {
                                player.getNave().getPosition().x += player.getNave().getSpeed() * cmd.delta;
                            }
                            player.getNave().getPosition().x = Math.min(Math.max(0, player.getNave().getPosition().x), 800 - 50);
                        } else if (command instanceof ShootCommand) {
                            player.getNave().shoot(1/60f, 200);
                            for (Bullet bullet : player.getNave().getBullets()) {
                                bullet.setOwnerId(playerId);
                            }
                            gameState.getBullets().addAll(player.getNave().getBullets());
                            player.getNave().getBullets().clear();
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Cliente " + playerId + " desconectado: " + e.getMessage());
            } finally {
                close();
            }
        }

        public void sendGameState(GameState state) throws IOException {
            synchronized (out) {
                String jsonString = toJSON(state);
                System.out.println("Enviando GameState a cliente " + playerId + ": " + jsonString);
                out.writeObject(jsonString);
                out.flush();
            }
        }

        public void close() {
            if (active) {
                active = false;
                try {
                    players.remove(playerId);
                    clientHandlers.remove(playerId);
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socket != null && !socket.isClosed()) socket.close();
                    System.out.println("Jugador " + playerId + " desconectado");
                } catch (IOException e) {
                    System.err.println("Error cerrando cliente " + playerId + ": " + e.getMessage());
                }
            }
        }
    }

    public static class MoveCommand implements Serializable {
        public String direction;
        public float delta;

        public MoveCommand() {}
        public MoveCommand(String direction, float delta) {
            this.direction = direction;
            this.delta = delta;
        }
    }

    public static class ShootCommand implements Serializable {
        public ShootCommand() {}
    }

    public static void main(String[] args) {
        new Thread(new GameServer()).start();
    }
}
