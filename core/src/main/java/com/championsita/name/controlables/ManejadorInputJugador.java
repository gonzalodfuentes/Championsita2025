package com.championsita.name.controlables;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.championsita.name.controlables.Personaje;

public class ManejadorInputJugador extends InputAdapter {

    private final Personaje personaje;
    private boolean arriba, abajo, izquierda, derecha;
    private boolean espacioPresionado;
    private boolean segundoJugador;

    public ManejadorInputJugador(Personaje personaje, boolean segundoJugador) {
        this.personaje = personaje;
        this.segundoJugador = segundoJugador;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!segundoJugador) {
            // Controles jugador 1
            switch (keycode) {
                case Input.Keys.W: arriba = true; break;
                case Input.Keys.S: abajo = true; break;
                case Input.Keys.A: izquierda = true; break;
                case Input.Keys.D: derecha = true; break;
                case Input.Keys.SPACE: espacioPresionado = true; break;
            }
        } else {
            // Controles jugador 2
            switch (keycode) {
                case Input.Keys.UP: arriba = true; break;
                case Input.Keys.DOWN: abajo = true; break;
                case Input.Keys.LEFT: izquierda = true; break;
                case Input.Keys.RIGHT: derecha = true; break;
                case Input.Keys.SHIFT_RIGHT: espacioPresionado = true; break;
            }
        }

        System.out.println("Tecla presionada: " + keycode + " para jugador: " + (segundoJugador ? 2 : 1));
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!segundoJugador) {
            switch (keycode) {
                case Input.Keys.W: arriba = false; break;
                case Input.Keys.S: abajo = false; break;
                case Input.Keys.A: izquierda = false; break;
                case Input.Keys.D: derecha = false; break;
                case Input.Keys.SPACE: espacioPresionado = false; break;
            }
        } else {
            switch (keycode) {
                case Input.Keys.UP: arriba = false; break;
                case Input.Keys.DOWN: abajo = false; break;
                case Input.Keys.LEFT: izquierda = false; break;
                case Input.Keys.RIGHT: derecha = false; break;
                case Input.Keys.SHIFT_RIGHT: espacioPresionado = false; break;
            }
        }
        return true;
    }


    // Llamá esto desde Principal.render()
    public void actualizar(float delta) {
        personaje.moverDesdeInput(arriba, abajo, izquierda, derecha, delta);
        personaje.setEspacioPresionado(espacioPresionado); // para disparo
    }
}