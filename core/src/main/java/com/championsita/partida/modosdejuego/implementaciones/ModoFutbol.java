package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.jugabilidad.constantes.Constantes;
import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.partida.modosdejuego.ControladorPosicionesIniciales;
import com.championsita.partida.modosdejuego.ModoDeJuego;
import com.championsita.partida.nucleo.ContextoModoDeJuego;


public class ModoFutbol extends ModoBase {

    private InputMultiplexer multiplexer;

    /*
    private Personaje jugadorConPosesion = null;
    private float tiempoPosesion = 0f;
    private boolean tiroCargado = false;

     */


    @Override
    protected void onIniciar() {


        multiplexer = new InputMultiplexer();
        ControladorPosicionesIniciales.PosicionarJugadoresYPelota(ctx, multiplexer);




    }

    @Override
    public void actualizar(float delta) {

        super.actualizar(delta);
    }

    @Override
    public InputProcessor getProcesadorEntrada() {
        return multiplexer;
    }

    @Override
    public int getCantidadDeJugadores() {
        return 2;
    }






    /*
    private void actualizarPosesion(float delta) {
        Personaje actual = ctx.pelota.getJugadorTocandoPelota();

        // CASO 1: Hay contacto físico explícito este frame
        if (actual != null) {
            if (actual != jugadorConPosesion) {
                // Nuevo dueño
                jugadorConPosesion = actual;
                tiempoPosesion = 0f;
                tiroCargado = false;
                System.out.println("Cambio de posesión a: " + actual.getNombre());
            } else {
                // Mismo dueño, seguimos cargando
                tiempoPosesion += delta;
            }
        }
        // CASO 2: No hay contacto físico (actual es null), pero ¿teníamos un dueño?
        else if (jugadorConPosesion != null) {

            // Verificamos distancia para ver si fue solo un "rebote de física"
            // Calculamos distancia entre centros
            float pCx = ctx.pelota.getX() + ctx.pelota.getWidth() / 2f;
            float pCy = ctx.pelota.getY() + ctx.pelota.getHeight() / 2f;

            float jCx = jugadorConPosesion.getX() + jugadorConPosesion.getHitbox().width / 2f;
            float jCy = jugadorConPosesion.getY() + jugadorConPosesion.getHitbox().height / 2f;

            float dist = (float) Math.hypot(pCx - jCx, pCy - jCy);

            // UMBRAL DE TOLERANCIA (ajustalo según el tamaño de tus sprites)
            // Por ejemplo: ancho del jugador + un margen (ej. 15 pixeles)
            float umbralDistancia = jugadorConPosesion.getHitbox().width + 15f;

            if (dist < umbralDistancia) {
                // Sigue cerca, asumimos que sigue conduciendo la pelota
                tiempoPosesion += delta;
            } else {
                // Se alejó demasiado, perdió la posesión
                jugadorConPosesion = null;
                tiempoPosesion = 0f;
                tiroCargado = false;
                System.out.println("Posesión perdida");
            }
        }

        // Lógica del tiro cargado
        if (!tiroCargado && tiempoPosesion >= 1f && jugadorConPosesion != null) {
            tiroCargado = true;
            System.out.println("¡TIRO CARGADO para " + jugadorConPosesion.getNombre() + "!");
            // Opcional: Feedback visual al jugador
        }
    }


     */







}


