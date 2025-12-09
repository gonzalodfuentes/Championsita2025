package com.championsita.partida.herramientas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.championsita.jugabilidad.constantes.Constantes;
import com.championsita.jugabilidad.sistemas.SistemaColisiones;
import com.championsita.jugabilidad.sistemas.SistemaFisico;
import com.championsita.jugabilidad.sistemas.SistemaPartido;
import com.championsita.jugabilidad.sistemas.SistemaSaqueLateral;
import com.championsita.partida.ControladorDePartida;
import com.championsita.partida.modosdejuego.implementaciones.*;
import com.championsita.partida.nucleo.ContextoPartida;

public class InicializadorPartida {

    public static ContextoPartida inicializar(Config config, ControladorDePartida pantallaPartida) {

        ContextoPartida base = new ContextoPartida();

        // 1. MODO DE JUEGO
        String modoTexto = (config.modo == null) ? "practica" : config.modo.toLowerCase();

        base.modo = switch (modoTexto) {
            case "1v1", "dosjug", "2jug", "1vs1" -> new UnoContraUno();
            case "futbol" -> new ModoFutbol()   ;
            case "especial" -> new ModoEspecial();
            case "futsal" -> new ModoFutsal();
            default -> new Practica();
        };

        // 2. SISTEMAS INTERNOS
        base.fisica = new SistemaFisico();
        base.colisiones = new SistemaColisiones();
        base.partido = new SistemaPartido(pantallaPartida);

        // 3. VISUALES BASE
        base.batch = new SpriteBatch();
        base.rendererFormas = new ShapeRenderer();
        base.viewport = new FitViewport(config.getAnchoMapa(), config.getAltoMapa());

        // 4. TEXTURA CANCHA
        base.texturaCancha = new Texture("campos/campo" + config.campo.getNombre() + ".png");

        return base;
    }
}
