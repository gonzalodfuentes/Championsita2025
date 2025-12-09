package com.championsita.partida.nucleo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.jugabilidad.sistemas.SistemaColisiones;
import com.championsita.jugabilidad.sistemas.SistemaFisico;
import com.championsita.jugabilidad.sistemas.SistemaPartido;
import com.championsita.jugabilidad.sistemas.SistemaSaqueLateral;
import com.championsita.partida.modosdejuego.ModoDeJuego;

public class ContextoPartida {
    public ModoDeJuego modo;
    public SpriteBatch batch;
    public Texture texturaCancha;
    public FitViewport viewport;
    public ShapeRenderer rendererFormas;
    public SistemaFisico fisica;
    public SistemaColisiones colisiones;
    public SistemaPartido partido;
}
