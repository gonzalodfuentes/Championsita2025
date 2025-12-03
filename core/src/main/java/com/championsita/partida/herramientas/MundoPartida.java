package com.championsita.partida.herramientas;

import com.championsita.jugabilidad.modelo.Cancha;
import com.championsita.jugabilidad.modelo.Pelota;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.jugabilidad.visuales.DibujadorJugador;
import com.championsita.jugabilidad.visuales.DibujadorPelota;

import java.util.ArrayList;

public class MundoPartida {
    public Cancha cancha;
    public ArrayList<Personaje> jugadores;
    public Pelota pelota;
    public ArrayList<DibujadorJugador> dibujadoresJugadores;
    public DibujadorPelota dibPelota;
}

