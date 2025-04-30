package io.game.client;

import io.game.GameState;
import io.game.gamesobject.Bullet;
import io.game.gamesobject.enemigues.Enemy;
import io.game.gamesobject.nave.Nave;
import io.game.player.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private GameState gameState;
    private int localPlayerId;

    public GameClient(String host, int port) throws IOException, ClassNotFoundException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        localPlayerId = (Integer) in.readObject();
        System.out.println("Recibido playerId: " + localPlayerId);
        gameState = new GameState();
        new Thread(this::receiveGameState).start();
    }

    private void receiveGameState() {
        try {
            while (socket.isConnected()) {
                try {
                    Object received = in.readObject();
                    if (received instanceof String) {
                        String jsonString = (String) received;
                        System.out.println("Recibido JSON: " + jsonString);
                        JSONObject json = new JSONObject(jsonString);
                        synchronized (gameState) {
                            gameState = fromJSON(json);
                        }
                    } else {
                        System.err.println("Objeto recibido no es String: " + received);
                    }
                } catch (Exception e) {
                    System.err.println("Error procesando GameState: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Error en receiveGameState: " + e.getMessage());
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void sendCommand(Object command) {
        try {
            synchronized (out) {
                out.writeObject(command);
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Error enviando comando: " + e.getMessage());
            close();
        }
    }

    public GameState getGameState() {
        synchronized (gameState) {
            return gameState;
        }
    }

    public int getLocalPlayerId() {
        return localPlayerId;
    }

    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error cerrando cliente: " + e.getMessage());
        }
    }

    private GameState fromJSON(JSONObject json) {
        try {
            GameState state = new GameState();
            Map<Integer, Player> players = new ConcurrentHashMap<>();
            JSONArray playersArray = json.getJSONArray("players");
            for (int i = 0; i < playersArray.length(); i++) {
                JSONObject playerJson = playersArray.getJSONObject(i);
                int id = playerJson.getInt("id");
                float x = (float) playerJson.getDouble("x");
                float y = (float) playerJson.getDouble("y");
                int lives = playerJson.getInt("lives");
                int score = playerJson.getInt("score");
                Nave nave = new Nave(id, 1, x, y, 50);
                Player player = new Player(id, nave);
                player.setLives(lives);
                player.setScore(score);
                players.put(id, player);
            }
            state.setPlayers(players);

            List<Enemy> enemies = new ArrayList<>();
            JSONArray enemiesArray = json.getJSONArray("enemies");
            for (int i = 0; i < enemiesArray.length(); i++) {
                JSONObject enemyJson = enemiesArray.getJSONObject(i);
                float x = (float) enemyJson.getDouble("x");
                float y = (float) enemyJson.getDouble("y");
                int type = enemyJson.getInt("type");
                enemies.add(new Enemy(type, x, y, 50));
            }
            state.setEnemies(enemies);

            List<Bullet> bullets = new ArrayList<>();
            JSONArray bulletsArray = json.getJSONArray("bullets");
            for (int i = 0; i < bulletsArray.length(); i++) {
                JSONObject bulletJson = bulletsArray.getJSONObject(i);
                float x = (float) bulletJson.getDouble("x");
                float y = (float) bulletJson.getDouble("y");
                float speed = (float) bulletJson.getDouble("speed");
                int ownerId = bulletJson.getInt("ownerId");
                String design = bulletJson.getString("design");
                bullets.add(new Bullet(x, y, speed, design, ownerId));
            }
            state.setBullets(bullets);

            System.out.println("GameState deserializado: " + players.size() + " jugadores, " + enemies.size() + " enemigos, " + bullets.size() + " balas");
            return state;
        } catch (Exception e) {
            System.err.println("Error en fromJSON: " + e.getMessage());
            e.printStackTrace();
            return new GameState(); // Retornar GameState vacío para evitar null
        }
    }
}
