package com.championsita.sistemas;

import com.championsita.modelo.Pelota;
import com.championsita.modelo.Personaje;

public class SistemaFisico {

    /** Avanza animaciones/estado del personaje*/
    public void actualizarPersonaje(Personaje p, float delta) {
        p.actualizar(delta);
    }

    /** Mantiene al personaje dentro del área del mundo. */
    public void limitarPersonajeAlMundo(Personaje p, float anchoMundo, float altoMundo) {
        p.limitarMovimiento(anchoMundo, altoMundo);
    }

    /** Avanza la física/animación de la pelota (sólo una vez por frame). */
    public void actualizarPelota(Pelota pelota, float delta) {
        pelota.actualizar(delta);
    }
}
