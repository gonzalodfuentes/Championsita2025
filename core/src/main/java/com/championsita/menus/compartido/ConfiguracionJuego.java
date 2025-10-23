package com.championsita.menus.compartido;

import com.championsita.menus.menucarga.Campo;

    public final class ConfiguracionJuego {
        private final String jugador1Skin;
        private final String jugador2Skin;
        private final Campo campo;
        private final OpcionDeGoles goles;
        private final OpcionDeTiempo tiempo;

        private ConfiguracionJuego(Builder b) {
            this.jugador1Skin = b.jugador1Skin;
            this.jugador2Skin = b.jugador2Skin;
            this.campo = b.campo;
            this.goles = b.goles;
            this.tiempo = b.tiempo;
        }

        public String getJugador1Skin() { return jugador1Skin; }
        public String getJugador2Skin() { return jugador2Skin; }
        public Campo getCampo() { return campo; }
        public OpcionDeGoles getGoles() { return goles; }
        public OpcionDeTiempo getTiempo() { return tiempo; }

        public static class Builder {
            private String jugador1Skin;
            private String jugador2Skin;
            private Campo campo;
            private OpcionDeGoles goles = OpcionDeGoles.UNO;
            private OpcionDeTiempo tiempo = OpcionDeTiempo.CORTO;

            public Builder jugador1Skin(String v) { this.jugador1Skin = v; return this; }
            public Builder jugador2Skin(String v) { this.jugador2Skin = v; return this; }
            public Builder campo(Campo v) { this.campo = v; return this; }
            public Builder goles(OpcionDeGoles v) { this.goles = v; return this; }
            public Builder tiempo(OpcionDeTiempo v) { this.tiempo = v; return this; }

            public ConfiguracionJuego build() {
                if (jugador1Skin == null || jugador2Skin == null || campo == null) {
                    throw new IllegalStateException("Faltan datos obligatorios en GameConfig");
                }
                return new ConfiguracionJuego(this);
            }
        }
    }
