package io.game.comun;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MensajeEstadoJuego implements Serializable {
    public Map<String, NaveEstado> naves = new HashMap<>();
    public Map<Integer, DisparoEstado> disparos = new HashMap<>();
}
