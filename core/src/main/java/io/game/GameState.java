package io.game;

import io.game.gamesobject.Bullet;
import io.game.gamesobject.enemigues.Enemy;
import io.game.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {
    private Map<Integer, Player> players;
    private List<Enemy> enemies;
    private List<Bullet> bullets;

    public GameState() {
        this.players = new ConcurrentHashMap<>();
        this.enemies = new ArrayList<>();
        this.bullets = new ArrayList<>();
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, Player> players) {
        this.players = players;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }
}
