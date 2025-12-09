package com.championsita.jugabilidad.sistemas;

import com.badlogic.gdx.math.Rectangle;
import com.championsita.jugabilidad.modelo.HabilidadesEspeciales;
import com.championsita.jugabilidad.modelo.Pelota;
import com.championsita.jugabilidad.modelo.Personaje;

public class SistemaColisiones {

    //Para saber quien la toca para los saques




    /** Empuja a los jugadores si se superponen (pequeño empujón de separación). */
    public void separarJugadoresSiChocan(Personaje a, Personaje b) {
        if (!a.getHitbox().overlaps(b.getHitbox())) return;

        float aCx = a.getX() + a.getHitbox().width  / 2f;
        float aCy = a.getY() + a.getHitbox().height / 2f;
        float bCx = b.getX() + b.getHitbox().width  / 2f;
        float bCy = b.getY() + b.getHitbox().height / 2f;

        float dx = aCx - bCx;
        float dy = aCy - bCy;
        if (dx == 0 && dy == 0) dy = 0.01f;

        float len = (float) Math.sqrt(dx*dx + dy*dy);
        dx /= len; dy /= len;

        // Empuje base
        float fuerzaA = 0.01f;
        float fuerzaB = 0.01f;

        // --- Si A es EMPUJÓN ---
        if (a.getHabilidadActual() == HabilidadesEspeciales.EMPUJON) {

            if (a.getStaminaActual() > 0) {
                // EMPUJÓN fuerte -> él no se mueve, el otro vuela
                fuerzaA *= 0.3f;    // casi inmóvil
                fuerzaB *= 4.0f;    // vuela fuerte
            } else {
                // EMPUJÓN sin stamina -> NO empuja, él sale volando
                fuerzaA *= 3.5f;    // EMPUJÓN rebota
                fuerzaB *= 0.2f;    // el otro apenas se mueve
            }
        }

        // --- Si B es EMPUJÓN ---
        if (b.getHabilidadActual() == HabilidadesEspeciales.EMPUJON) {

            if (b.getStaminaActual() > 0) {
                fuerzaB *= 0.3f;
                fuerzaA *= 4.0f;
            } else {
                fuerzaB *= 3.5f;
                fuerzaA *= 0.2f;
            }
        }

        // GRANDOTE — mayor masa si estamina llena
        if (a.getHabilidadActual() == HabilidadesEspeciales.GRANDOTE &&
                a.getStaminaActual() >= a.getStaminaMaxima())
            fuerzaB *= 1.7f;

        if (b.getHabilidadActual() == HabilidadesEspeciales.GRANDOTE &&
                b.getStaminaActual() >= b.getStaminaMaxima())
            fuerzaA *= 1.7f;

        // PEQUEÑÍN — liviano
        if (a.getHabilidadActual() == HabilidadesEspeciales.PEQUEÑIN)
            fuerzaA *= 1.5f;

        if (b.getHabilidadActual() == HabilidadesEspeciales.PEQUEÑIN)
            fuerzaB *= 1.5f;

        a.setPosicion(a.getX() + dx * fuerzaA, a.getY() + dy * fuerzaA);
        b.setPosicion(b.getX() - dx * fuerzaB, b.getY() - dy * fuerzaB);
    }
    


    /** Maneja contacto jugador↔pelota: corrige penetración (MTV) y registra empuje/disparo. */
    public void procesarContactoPelotaConJugador(Pelota pelota, Personaje jugador) {
        Rectangle A = jugador.getHitbox();
        Rectangle B = pelota.getHitbox();
        if (!A.overlaps(B)) return;

        // 1) Corregir penetración (MTV AABB) moviendo SOLO la pelota
        corregirPenetracionPelotaContraJugador(pelota, jugador);

        pelota.setJugadorTocandoPelota(jugador);
         // Para verificar para saque de pelota

        // 2) Dirección de empuje “jugador → pelota”
        float centroPelotaX  = B.x + B.width  / 2f;
        float centroPelotaY  = B.y + B.height / 2f;
        float centroJugadorX = A.x + A.width  / 2f;
        float centroJugadorY = A.y + A.height / 2f;

        float dx = centroPelotaX - centroJugadorX;
        float dy = centroPelotaY - centroJugadorY;
        float len = (float) Math.sqrt(dx*dx + dy*dy);
        if (len != 0) { dx /= len; dy /= len; } else { dx = 0; dy = 1; } // fallback

        // ===============================================
        // MODIFICADORES DE HABILIDAD SOBRE LA PELOTA
        // ===============================================

        // Comba del disparo (ZURDO / DIESTRO)
        if (jugador.estaEspacioPresionado()) {

            if (jugador.getHabilidadActual() == HabilidadesEspeciales.ZURDO) {
                pelota.setCurvaActiva(true, +1); // curva antihorario
            }

            if (jugador.getHabilidadActual() == HabilidadesEspeciales.DIESTRO) {
                pelota.setCurvaActiva(true, -1); // curva horario
            }

        }

        // GRANDOTE – disparo más fuerte con estamina llena
        if (jugador.getHabilidadActual() == HabilidadesEspeciales.GRANDOTE &&
                jugador.getStaminaActual() >= jugador.getStaminaMaxima()) {

            dx *= 1.3f;
            dy *= 1.3f;
        }



        // PEQUEÑIN – NO arrastra pelota (solo rebota)
        if (jugador.getHabilidadActual() == HabilidadesEspeciales.PEQUEÑIN) {
            // “cortar” el empuje continuo del contacto
            // Cero dx/dy si NO es disparo
            if (!jugador.estaEspacioPresionado()) {
                dx *= 0.6f;   // rebote suave
                dy *= 0.6f;
            }
        }

        // ATLETA – empuje mayor cuando está esprintando
        if (jugador.getHabilidadActual() == HabilidadesEspeciales.ATLETA) {
            // ¿Está esprintando?
            if (jugador.estaSprintPresionado() && jugador.getStaminaActual() > 0) {
                dx *= 1.25f;
                dy *= 1.25f;
            }
        }


        boolean esDisparo = jugador.estaEspacioPresionado();
        pelota.setUltimoJugadorQueLaToco(jugador);
        pelota.registrarContacto(jugador, dx, dy, esDisparo);
    }



    /** AABB vs AABB: empuja la pelota hacia afuera por el eje de menor solape (MTV). */
    private void corregirPenetracionPelotaContraJugador(Pelota pelota, Personaje jugador) {
        Rectangle A = jugador.getHitbox();
        Rectangle B = pelota.getHitbox();

        float aIzq = A.x, aDer = A.x + A.width,  aAbajo = A.y, aArriba = A.y + A.height;
        float bIzq = B.x, bDer = B.x + B.width,  bAbajo = B.y, bArriba = B.y + B.height;

        float solapeX = Math.min(aDer, bDer) - Math.max(aIzq, bIzq);
        float solapeY = Math.min(aArriba, bArriba) - Math.max(aAbajo, bAbajo);
        if (solapeX <= 0 || solapeY <= 0) return; // sin penetración

        float centroJX = A.x + A.width  / 2f;
        float centroJY = A.y + A.height / 2f;
        float centroBX = B.x + B.width  / 2f;
        float centroBY = B.y + B.height / 2f;

        float nuevaBX = B.x, nuevaBY = B.y;
        float EPS = 0.001f;

        if (solapeX < solapeY) {
            // Empujar en X
            if (centroBX >= centroJX) nuevaBX += solapeX + EPS;
            else                      nuevaBX -= solapeX + EPS;
        } else {
            // Empujar en Y
            if (centroBY >= centroJY) nuevaBY += solapeY + EPS;
            else                      nuevaBY -= solapeY + EPS;
        }

        pelota.setPosicion(nuevaBX, nuevaBY);
    }




}
