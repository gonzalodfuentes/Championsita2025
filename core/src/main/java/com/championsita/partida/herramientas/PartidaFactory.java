package com.championsita.partida.herramientas;

import com.badlogic.gdx.graphics.Texture;
import com.championsita.partida.herramientas.Config;
import com.championsita.jugabilidad.constantes.Constantes;
import com.championsita.jugabilidad.modelo.*;
import com.championsita.jugabilidad.personajes.Normal;
import com.championsita.jugabilidad.visuales.*;
import com.championsita.partida.herramientas.MundoPartida;

import java.util.ArrayList;

public class PartidaFactory {


    public static MundoPartida crearDesdeConfig(Config config) {

        MundoPartida mundo = new MundoPartida();



        // Cancha
        Texture canchaTexture = new Texture("campos/campo" + config.campo.getNombre() + ".png");
        mundo.cancha = new Cancha(0.5f, 0.8f,config.getAltoMapa(), config.getAnchoMapa(), canchaTexture);

        // Jugadores
        mundo.jugadores = new ArrayList<>();
        mundo.dibujadoresJugadores = new ArrayList<>();

        for (int i = 0; i < config.skinsJugadores.size(); i++) {
            Personaje p = new Normal(
                    "Jugador" + (i + 1),
                    ConfiguracionPersonaje.porDefecto(config.skinsJugadores.get(i)),
                    Constantes.ESCALA_PERSONAJE
            );

            Equipo equipo = config.equiposJugadores.isEmpty()
                    ? (i == 0 ? Equipo.ROJO : Equipo.AZUL)
                    : config.equiposJugadores.get(i);

            p.setEquipo(equipo);

            mundo.jugadores.add(p);
            mundo.dibujadoresJugadores.add(new DibujadorJugador(p));
        }

        // Pelota
        mundo.pelota = new Pelota(
                config.getAnchoMapa() / 2f,
                config.getAltoMapa() / 2f,
                config.getEscalaPelota()
        );
        mundo.dibPelota = new DibujadorPelota(mundo.pelota);

        return mundo;
    }








}
