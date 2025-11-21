package com.championsita.jugabilidad.sistemas;

import com.badlogic.gdx.math.Vector2;
import com.championsita.jugabilidad.modelo.Pelota;
import com.championsita.jugabilidad.modelo.Personaje;

public class SistemaFisico {

    /* Avanza animaciones/estado del personaje*/
    public void actualizarPersonaje(Personaje p, float delta) {
        p.actualizar(delta);
    }

    /* Mantiene al personaje dentro del área del mundo. */
    public void limitarPersonajeAlMundo(Personaje p, float anchoMundo, float altoMundo) {
        p.limitarMovimiento(anchoMundo, altoMundo);
    }
    /* Mantiene la pelota dentro del área del mundo. */
    public void rebotarLaPelotaEnLosBordes(Pelota pelota, float anchoMundo, float altoMundo) {

        // Tamaño efectivo de la pelota (la mitad, para chequear bordes)
        float pelotaHalfWidth  = pelota.getWidth()  / 2f;
        float pelotaHalfHeight = pelota.getHeight() / 2f;

        // Posición actual
        float pelotaX = pelota.getX();
        float pelotaY = pelota.getY();

        // --- EJE X: Rebote en paredes izquierda y derecha ---

        boolean hitLeftWall  = pelotaX < pelotaHalfWidth;
        boolean hitRightWall = pelotaX > anchoMundo - pelotaHalfWidth;

        if (hitLeftWall) {
            pelota.setX(pelotaHalfWidth);  // la apoyamos sobre la pared
            pelota.setVelocidadX(-pelota.getVelocidadX()); // invertimos velocidad
        }

        if (hitRightWall) {
            pelota.setX(anchoMundo - pelotaHalfWidth);
            pelota.setVelocidadX(-pelota.getVelocidadX());
        }

        // --- EJE Y: Rebote en paredes inferior y superior ---

        boolean hitBottomWall = pelotaY < pelotaHalfHeight;
        boolean hitTopWall    = pelotaY > altoMundo - pelotaHalfHeight;

        if (hitBottomWall) {
            pelota.setY(pelotaHalfHeight);
            pelota.setVelocidadY(-pelota.getVelocidadY());
        }

        if (hitTopWall) {
            pelota.setY(altoMundo - pelotaHalfHeight);
            pelota.setVelocidadY(-pelota.getVelocidadY());
        }
    }

    /* Avanza la física/animación de la pelota (sólo una vez por frame). */
    public void actualizarPelota(Pelota pelota, float delta) {
        pelota.actualizar(delta);
    }
}
