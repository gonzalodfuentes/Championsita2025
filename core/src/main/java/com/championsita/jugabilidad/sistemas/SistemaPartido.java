//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.championsita.jugabilidad.sistemas;

import com.badlogic.gdx.math.Rectangle;
import com.championsita.jugabilidad.modelo.*;
import com.championsita.partida.ControladorDePartida;

import static com.championsita.jugabilidad.modelo.HabilidadesEspeciales.EXTREMISTA;

public class SistemaPartido {
    int notadorEquipo1 = 0;
    int notadorEquipo2 = 0;

    private ControladorDePartida controlador;

    public SistemaPartido(ControladorDePartida controlador) {
        this.controlador = controlador;
    }

    public boolean checkGol(Pelota pelota, Arco arco) {

        // Rectángulo del arco (zona que define el gol)
        Rectangle zonaArco = arco.getHitbox();

        // Rectángulo de la pelota
        Rectangle rectPelota = pelota.getHitbox();

        // Centro de la pelota
        float centroPelotaX = rectPelota.x + rectPelota.width / 2f;
        float centroPelotaY = rectPelota.y + rectPelota.height / 2f;

        // Límites del arco
        float arcoXMin = zonaArco.x;
        float arcoXMax = zonaArco.x + zonaArco.width;
        float arcoYMin = zonaArco.y;
        float arcoYMax = zonaArco.y + zonaArco.height;

        // Detecta si el centro de la pelota está dentro del área del arco
        boolean centroDentroDeArco =
                centroPelotaX >= arcoXMin &&
                        centroPelotaX <= arcoXMax &&
                        centroPelotaY >= arcoYMin &&
                        centroPelotaY <= arcoYMax;

        return centroDentroDeArco;
    }


    public void verificarSiHayGol(Pelota pelota, Cancha cancha) {
        if (this.checkGol(pelota, cancha.getArcoDerecho())) {
            System.out.println("Gol equipo 2!!");
            ++this.notadorEquipo1;
            this.reiniciarPelota(pelota);
        } else if (this.checkGol(pelota, cancha.getArcoIzquierdo())) {
            System.out.println("Gol equipo 1!!");
            ++this.notadorEquipo2;
            this.reiniciarPelota(pelota);
        }

        // ============================================
        // EXTREMISTA: aplicar buff/debuff por gol
        // ============================================

        Personaje personajeQueMetioGol = pelota.getUltimoJugadorQueToco();

        // Si nadie la tocó todavía → no aplicar EXTREMISTA
        if (personajeQueMetioGol == null) {
            return;
        }

        Equipo meteGol = personajeQueMetioGol.getEquipo();

        // Si por error el jugador no tiene equipo → no crashear
        if (meteGol == null) {
            return;
        }

        Equipo recibeGol = (meteGol == Equipo.ROJO) ? Equipo.AZUL : Equipo.ROJO;

        for (Personaje pj : controlador.getJugadoresDelEquipo(meteGol)){
            if (pj.getHabilidadActual() == EXTREMISTA) {
                pj.activarBuffVelocidad(10f);
            }
        }

        for (Personaje pj : controlador.getJugadoresDelEquipo(recibeGol)) {
            if (pj.getHabilidadActual() == EXTREMISTA) {
                pj.activarDebuffVelocidad(10f);
            }
        }


    }

    public void reiniciarPelota(Pelota pelota) {
        pelota.detenerPelota();
        pelota.setPosicion(4.0F, 2.5F);
    }

    public int getNotadorEquipo1() {
        return this.notadorEquipo1;
    }

    public int getNotadorEquipo2(){
        return this.notadorEquipo2;
    }

}
