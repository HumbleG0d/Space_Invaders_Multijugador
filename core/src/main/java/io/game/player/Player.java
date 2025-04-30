package io.game.player;

import io.game.gamesobject.nave.Nave;

public class Player {
    private int id;
    private Nave nave;
    private int lives;
    private int score;

    public Player(int id, Nave nave) {
        this.id = id;
        this.nave = nave;
        this.score = 0;
        this.lives = 3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Nave getNave() {
        return nave;
    }

    public void setNave(Nave nave) {
        this.nave = nave;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
