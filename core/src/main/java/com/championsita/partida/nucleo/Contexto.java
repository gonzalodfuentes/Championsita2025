package com.championsita.partida.nucleo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.jugabilidad.modelo.Cancha;
import com.championsita.jugabilidad.modelo.Pelota;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.jugabilidad.sistemas.SistemaColisiones;
import com.championsita.jugabilidad.sistemas.SistemaFisico;
import com.championsita.jugabilidad.sistemas.SistemaPartido;

import java.util.ArrayList;

/**
 * Contiene las referencias principales de una partida.
 * Se usa para compartir datos entre distintos modos de juego.
 *
 * No libera recursos. El Screen que lo crea es el responsable.
 */
public class Contexto {

    // Renderizado y cámara
    public final FitViewport viewport;
    public final SpriteBatch batch;
    public final Cancha cancha;

    // Sistemas de lógica
    public final SistemaFisico fisica;
    public final SistemaColisiones colisiones;
    public final SistemaPartido partido;

    // Entidades
    public ArrayList<Personaje> jugadores;
    public ArrayList<EntradaJugador> controles = new ArrayList<>();
    public Pelota pelota;

    public Contexto(FitViewport viewport,
                    SpriteBatch batch,
                    Cancha cancha,
                    SistemaFisico fisica,
                    SistemaColisiones colisiones,
                    SistemaPartido partido,
                    ArrayList<Personaje> jugadores) {
        this.viewport = viewport;
        this.batch = batch;
        this.cancha = cancha;
        this.fisica = fisica;
        this.colisiones = colisiones;
        this.partido = partido;
        this.jugadores = jugadores;
    }
}