package com.championsita.jugabilidad.sistemas;

import com.badlogic.gdx.math.Vector2;
import com.championsita.jugabilidad.modelo.*;

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
    /* Mantiene la pelota dentro del área del mundo. */

    public SalidaPelota detectarSalida(Pelota pelota, CanchaHitbox hb) {

        float x = pelota.getX();
        float y = pelota.getY();
        float w = pelota.getWidth();
        float h = pelota.getHeight();

        // ============================
        // 1. FUERA POR IZQUIERDA
        // ============================
        if (x + w < hb.lineaIzquierda.x) {
            return SalidaPelota.FUERA_IZQUIERDA;
        }

        // ============================
        // 2. FUERA POR DERECHA
        // ============================
        if (x > hb.lineaDerecha.x + hb.lineaDerecha.width) {
            return SalidaPelota.FUERA_DERECHA;
        }

        // ============================
        // 3. FUERA POR ABAJO
        // ============================
        if (y + h < hb.lineaInferior.y) {
            return SalidaPelota.FUERA_ABAJO;
        }

        // ============================
        // 4. FUERA POR ARRIBA
        // ============================
        if (y > hb.lineaSuperior.y + hb.lineaSuperior.height) {
            return SalidaPelota.FUERA_ARRIBA;
        }

        return SalidaPelota.DENTRO;
    }


    public void rebotarLaPelotaEnLosBordes(Pelota pelota, Cancha cancha) {

        CanchaHitbox hb = cancha.getHitbox();

        float x = pelota.getX();
        float y = pelota.getY();
        float w = pelota.getWidth();
        float h = pelota.getHeight();

        // ================================
        // IZQUIERDA (exceptuando el área del arco)
        // ================================
        Arco arcoIzquierdo = cancha.getArcoIzquierdo();

        boolean dentroArcoIzquierdo =
                y + h > arcoIzquierdo.getY() &&
                        y < arcoIzquierdo.getY() + arcoIzquierdo.getHeight();

        if (!dentroArcoIzquierdo) {
            if (x < hb.lineaIzquierda.x + hb.lineaIzquierda.width) {
                pelota.setX(hb.lineaIzquierda.x + hb.lineaIzquierda.width);
                pelota.setVelocidadX(-pelota.getVelocidadX() * 0.8f);
                pelota.limpiarContacto();
            }
        }

        // ================================
        // DERECHA (exceptuando área del arco derecho)
        // ================================
        Arco arcoDerecho = cancha.getArcoDerecho();

        boolean dentroArcoDerecho =
                y + h > arcoDerecho.getY() &&
                        y < arcoDerecho.getY() + arcoDerecho.getHeight();

        if (!dentroArcoDerecho) {
            if (x + w > hb.lineaDerecha.x) {
                pelota.setX(hb.lineaDerecha.x - w);
                pelota.setVelocidadX(-pelota.getVelocidadX() * 0.8f);
                pelota.limpiarContacto();
            }
        }

        // ================================
        // ABAJO
        // ================================
        if (y < hb.lineaInferior.y + hb.lineaInferior.height) {
            pelota.setY(hb.lineaInferior.y + hb.lineaInferior.height);
            pelota.setVelocidadY(-pelota.getVelocidadY() * 0.8f);
            pelota.limpiarContacto();
        }

        // ================================
        // ARRIBA
        // ================================
        if (y + h > hb.lineaSuperior.y) {
            pelota.setY(hb.lineaSuperior.y - h);
            pelota.setVelocidadY(-pelota.getVelocidadY() * 0.8f);
            pelota.limpiarContacto();
        }
    }




/*
    public void rebotarLaPelotaEnLosBordes(Pelota pelota, float anchoMundo, float altoMundo, Cancha cancha) {

        // Tamaño efectivo de la pelota (el ancho y alto completos de su hitbox)
        float pelotaWidth  = pelota.getWidth();
        float pelotaHeight = pelota.getHeight();

        // Posición actual (esquina inferior izquierda de la hitbox)
        float pelotaX = pelota.getX();
        float pelotaY = pelota.getY();

        // --- EJE X: Rebote en paredes izquierda y derecha ---

        // La pelota toca la pared izquierda si su X es menor que 0
        boolean tocaIzquierdaPared = pelotaX < 0f;
        // La pelota toca la pared derecha si su X + Ancho es mayor que el ancho del mundo
        boolean tocaDerechaPared = pelotaX + pelotaWidth > anchoMundo;


        Arco arcoIzquierdo = cancha.getArcoIzquierdo();
        boolean dentroArcoIzquierdo =
                pelotaY > arcoIzquierdo.getY() &&
                        pelotaY < arcoIzquierdo.getY() + arcoIzquierdo.getHeight();
        // Nota: Se asume que el arco ya está fuera del limite X=0, por lo que no hace falta la X

        // Lógica Arco Izquierdo
        if (!dentroArcoIzquierdo) {
            if (tocaIzquierdaPared) {
                // Fija la posición en X = 0
                pelota.setX(0f);
                pelota.setVelocidadX(-pelota.getVelocidadX() * 0.8f); // Usa la velocidad actual para el rebote
                pelota.limpiarContacto();
            }
        }


        // Lógica Pared Derecha
        // El arco derecho no tiene un bypass aquí, así que rebotará si está FUERA del área de gol.
        // Dado que el arco derecho está en X=8.0f (MUNDO_ANCHO), esta pared de rebote se ejecuta ANTES que la lógica de gol si no corriges esto.
        if (tocaDerechaPared) {
            // Fija la posición en X = anchoMundo - pelotaWidth
            pelota.setX(anchoMundo - pelotaWidth);
            pelota.setVelocidadX(-pelota.getVelocidadX() * 0.8f); // Usa la velocidad actual para el rebote
            pelota.limpiarContacto();
        }


        // --- EJE Y: Rebote en paredes inferior y superior ---

        // La pelota toca la pared inferior si su Y es menor que 0
        boolean tocaAbajoPared = pelotaY < 0f;
        // La pelota toca la pared superior si su Y + Alto es mayor que el alto del mundo
        boolean tocaArribaPared    = pelotaY + pelotaHeight > altoMundo;


        if (tocaAbajoPared) {
            // Fija la posición en Y = 0
            pelota.setY(0f);
            pelota.setVelocidadY(-pelota.getVelocidadY() * 0.8f);
            pelota.limpiarContacto();
        }

        if (tocaArribaPared) {
            // Fija la posición en Y = altoMundo - pelotaHeight
            pelota.setY(altoMundo - pelotaHeight);
            pelota.setVelocidadY(-pelota.getVelocidadY() * 0.8f);
            pelota.limpiarContacto();
        }
    }
*/


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
