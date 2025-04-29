package io.game.comun;

import java.io.Serializable;

public class NaveEstado implements Serializable {
    public float x, y;
    public String playerId;

    public NaveEstado(String playerId, float x, float y) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
    }
}
