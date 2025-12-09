package com.championsita.partida.herramientas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.jugabilidad.constantes.Constantes;
import com.championsita.jugabilidad.modelo.Cancha;
import com.championsita.jugabilidad.modelo.CanchaHitbox;
import com.championsita.jugabilidad.modelo.Pelota;
import com.championsita.jugabilidad.sistemas.SistemaPartido;
import com.championsita.jugabilidad.visuales.DibujadorJugador;
import com.championsita.jugabilidad.visuales.DibujadorPelota;
import com.championsita.jugabilidad.visuales.HudPartido;
import com.championsita.partida.modosdejuego.ModoDeJuego;
import com.championsita.partida.modosdejuego.implementaciones.Practica;

import java.util.ArrayList;

public class RenderizadorPartida {

    public void renderFondo(SpriteBatch batch, FitViewport viewport, Cancha cancha) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        cancha.dibujarCancha(batch, viewport);
        batch.end();
    }

    public void renderEntidades(SpriteBatch batch,
                                FitViewport viewport,
                                ArrayList<DibujadorJugador> dibujadoresJugadores,
                                DibujadorPelota dibPelota, ModoDeJuego modoDeJuego) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        if(modoDeJuego.getClass() != Practica.class){
            for (DibujadorJugador dib : dibujadoresJugadores) {
                dib.dibujar(batch);
            }
        }else {
            dibujadoresJugadores.get(0).dibujar(batch);
        }

        dibPelota.dibujar(batch);

        batch.end();
    }

    public void renderHudModo(SpriteBatch batch,
                              ModoDeJuego modoJuego,
                              FitViewport viewport) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        modoJuego.renderizar(batch);
        batch.end();
    }

    public void renderHudPartido(SpriteBatch batch,
                                 HudPartido hud,
                                 SistemaPartido datosDelPartido,
                                 int pantallaAncho,
                                 int pantallaAlto) {

        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(
                0, 0,
                pantallaAncho,
                pantallaAlto
        ));

        batch.begin();
        hud.dibujarHud(batch, datosDelPartido);
        batch.end();
    }

    public void renderArcos(ShapeRenderer renderer, FitViewport viewport, Cancha cancha) {
        renderer.setProjectionMatrix(viewport.getCamera().combined);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        cancha.getArcoDerecho().dibujar(renderer);
        cancha.getArcoIzquierdo().dibujar(renderer);


        renderer.end();
    }

    public void renderHitbox(ShapeRenderer renderer,
                             FitViewport viewport,
                             Cancha cancha,
                             Pelota pelota) {

        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        // ============================
        // 1. Líneas de la cancha
        // ============================
        renderer.setColor(Color.BLUE);

        dibujarRect(renderer, cancha.getCanchaHitbox().lineaIzquierda);
        dibujarRect(renderer, cancha.getCanchaHitbox().lineaDerecha);
        dibujarRect(renderer, cancha.getCanchaHitbox().lineaSuperior);
        dibujarRect(renderer, cancha.getCanchaHitbox().lineaInferior);

        // ============================
        // 2. Pelota
        // ============================
        renderer.setColor(Color.YELLOW);
        Rectangle pelotaHitbox = pelota.getHitbox();
        renderer.rect(pelotaHitbox.x, pelotaHitbox.y, pelotaHitbox.width, pelotaHitbox.height);

        renderer.end();
    }


    private void dibujarRect(ShapeRenderer renderer, Rectangle r) {
        renderer.rect(r.x, r.y, r.width, r.height);
    }

}
