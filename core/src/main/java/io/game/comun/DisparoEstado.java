package io.game.comun;

import java.io.Serializable;

public class DisparoEstado implements Serializable {
    public float x, y;
    public int id;

    public DisparoEstado(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}
