package com.championsita.jugabilidad.sistemas;

import com.badlogic.gdx.math.Vector2;
import com.championsita.jugabilidad.modelo.Arco;
import com.championsita.jugabilidad.modelo.Cancha;
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
    public void rebotarLaPelotaEnLosBordes(Pelota pelota, float anchoMundo, float altoMundo, Cancha cancha) {

        // Tamaño efectivo de la pelota (la mitad, para chequear bordes)
        float pelotaHalfWidth  = pelota.getWidth();
        float pelotaHalfHeight = pelota.getHeight();

        // Posición actual
        float pelotaX = pelota.getX();
        float pelotaY = pelota.getY();

        // --- EJE X: Rebote en paredes izquierda y derecha ---

        boolean tocaIzquierdaPared  = pelotaX < pelotaHalfWidth;
        boolean tocaDerechaPared = pelotaX > anchoMundo - pelotaHalfWidth;

        Arco arcoIzquierdo = cancha.getArcoIzquierdo();
        boolean dentroArcoIzquierdo =
                pelotaY > arcoIzquierdo.getY() &&
                        pelotaY < arcoIzquierdo.getY() + arcoIzquierdo.getHeight() &&
                        pelotaX < arcoIzquierdo.getX() + arcoIzquierdo.getWidth();

        if (!dentroArcoIzquierdo) {
            if (tocaIzquierdaPared) {
                pelota.setX(pelotaHalfWidth);
                pelota.setVelocidadX(-Pelota.getFuerzaDisparo());
                pelota.limpiarContacto();
            }
        }


        if (tocaDerechaPared) {
            pelota.setX(anchoMundo - pelotaHalfWidth);
            pelota.setVelocidadX(-Pelota.getFuerzaDisparo());
            pelota.limpiarContacto();
        }

        // --- EJE Y: Rebote en paredes inferior y superior ---

        boolean tocaAbajoPared = pelotaY < pelotaHalfHeight;
        boolean tocaArribaPared    = pelotaY > altoMundo - pelotaHalfHeight;

        if (tocaAbajoPared) {
            pelota.setY(pelotaHalfHeight);
            pelota.setVelocidadY(-Pelota.getFuerzaDisparo());
            pelota.limpiarContacto();
        }

        if (tocaArribaPared) {
            pelota.setY(altoMundo - pelotaHalfHeight);
            pelota.setVelocidadY(-Pelota.getFuerzaDisparo());
            pelota.limpiarContacto();
        }
    }

    /* Avanza la física/animación de la pelota (sólo una vez por frame). */
    public void actualizarPelota(Pelota pelota, float delta) {

        pelota.actualizar(delta);

        float vx = pelota.getVelocidadX();
        float vy = pelota.getVelocidadY();

        // === Apagar comba cuando la pelota está lenta ===
        float vel = (float)Math.sqrt(vx*vx + vy*vy);
        if (vel < 1.0f) {
            pelota.setCurvaActiva(false, 0);
        }

        // === 1) Aplicar comba SI está activa ===
        if (pelota.curvaActiva) {

            float curvaBase = 0.045f;
            float curva = curvaBase * pelota.curvaSigno;

            float nvx = vx - vy * curva;
            float nvy = vy + vx * curva;

            vx = nvx;
            vy = nvy;
        }


        // === 2) Aplicar FRENADO (fricción) ===
        float friccion = 0.98f;  // ajustable
        vx *= friccion;
        vy *= friccion;

        // === 3) Guardar velocidades finales ===
        pelota.setVelocidad(vx, vy);
    }
}
