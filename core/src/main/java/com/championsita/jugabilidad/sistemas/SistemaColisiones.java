package com.championsita.jugabilidad.sistemas;

import com.badlogic.gdx.math.Rectangle;
import com.championsita.jugabilidad.modelo.Pelota;
import com.championsita.jugabilidad.modelo.Personaje;

public class SistemaColisiones {

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

        float empujon = 0.01f; // tunear a gusto
        a.setPosicion(a.getX() + dx * empujon, a.getY() + dy * empujon);
        b.setPosicion(b.getX() - dx * empujon, b.getY() - dy * empujon);
    }

    /** Maneja contacto jugador↔pelota: corrige penetración (MTV) y registra empuje/disparo. */
    public void procesarContactoPelotaConJugador(Pelota pelota, Personaje jugador) {
        Rectangle A = jugador.getHitbox();
        Rectangle B = pelota.getHitbox();
        if (!A.overlaps(B)) return;

        // 1) Corregir penetración (MTV AABB) moviendo SOLO la pelota
        corregirPenetracionPelotaContraJugador(pelota, jugador);

        // 2) Dirección de empuje “jugador → pelota”
        float centroPelotaX  = B.x + B.width  / 2f;
        float centroPelotaY  = B.y + B.height / 2f;
        float centroJugadorX = A.x + A.width  / 2f;
        float centroJugadorY = A.y + A.height / 2f;

        float dx = centroPelotaX - centroJugadorX;
        float dy = centroPelotaY - centroJugadorY;
        float len = (float) Math.sqrt(dx*dx + dy*dy);
        if (len != 0) { dx /= len; dy /= len; } else { dx = 0; dy = 1; } // fallback

        boolean esDisparo = jugador.estaEspacioPresionado();
        pelota.registrarContacto(dx, dy, esDisparo);
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
