package com.championsita.partida.herramientas;

import com.championsita.jugabilidad.modelo.Equipo;
import com.championsita.jugabilidad.modelo.HabilidadesEspeciales;
import com.championsita.menus.compartido.OpcionDeGoles;
import com.championsita.menus.compartido.OpcionDeTiempo;
import com.championsita.menus.menucarga.Campo;

import java.util.ArrayList;

public class Config {
    public final ArrayList<String> skinsJugadores;
    public final Campo campo;
    public final OpcionDeGoles goles;
    public final OpcionDeTiempo tiempo;
    public final String modo; // "practica", "1v1", etc.
    public final ArrayList<Equipo> equiposJugadores; // ← NUEVO
    public ArrayList<HabilidadesEspeciales> habilidadesEspeciales;

    private Config(Builder b) {
        this.skinsJugadores = b.skinsJugadores;
        this.campo = b.campo;
        this.goles = b.goles;
        this.tiempo = b.tiempo;
        this.modo = b.modo;
        this.equiposJugadores = b.equiposJugadores; // ← NUEVO
        if(modo.equals("especial")){
            this.habilidadesEspeciales = b.habilidadesEspeciales;
        }
    }

    public static class Builder {
        public ArrayList<String> skinsJugadores = new ArrayList<>();
        public ArrayList<Equipo> equiposJugadores = new ArrayList<>(); // ← NUEVO
        private Campo campo;
        private OpcionDeGoles goles = OpcionDeGoles.UNO;
        private OpcionDeTiempo tiempo = OpcionDeTiempo.CORTO;
        private String modo = "practica";
        public ArrayList<HabilidadesEspeciales> habilidadesEspeciales = new ArrayList<>();

        public Builder agregarSkin(String skin) {
            this.skinsJugadores.add(skin);
            return this;
        }

        // NUEVO: agregar equipo por jugador en el mismo orden que las skins
        public Builder agregarEquipo(Equipo equipo) {
            this.equiposJugadores.add(equipo);
            return this;
        }

        public Builder agregarHabilidades(ArrayList<HabilidadesEspeciales> habilidades) {
            this.habilidadesEspeciales.addAll(habilidades);
            return this;
        }

        public Builder campo(Campo v) { this.campo = v; return this; }
        public Builder goles(OpcionDeGoles v) { this.goles = v; return this; }
        public Builder tiempo(OpcionDeTiempo v) { this.tiempo = v; return this; }
        public Builder modo(String v) { this.modo = v; return this; }

        public Config build() {
            if (skinsJugadores.isEmpty() || campo == null) {
                throw new IllegalStateException("Faltan datos obligatorios en Config (skins/campo)");
            }

            // opcional: validar que, si se pasaron equipos, coincida la cantidad
            if (!equiposJugadores.isEmpty() && equiposJugadores.size() != skinsJugadores.size()) {
                throw new IllegalStateException("La cantidad de equipos no coincide con la de skins");
            }

            return new Config(this);
        }
    }
}
