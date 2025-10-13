package com.championsita.jugabilidad.entrada;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.championsita.Principal;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.menus.menucarga.Carga;
import com.championsita.menus.menueleccion.Doble;

public class EntradaJugador implements InputProcessor {

    private final Personaje personaje;

    private final int keyArriba, keyAbajo, keyIzquierda, keyDerecha, keyAccion;
    private final int keySprint = Input.Keys.SHIFT_LEFT;

    private boolean arriba, abajo, izquierda, derecha, espacioPresionado, sprintPresionado;

    public EntradaJugador(Personaje personaje, int arriba, int abajo, int izquierda, int derecha, int accion) {
        this.personaje = personaje;
        this.keyArriba = arriba;
        this.keyAbajo = abajo;
        this.keyIzquierda = izquierda;
        this.keyDerecha = derecha;
        this.keyAccion = accion;
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean handled = false;
        if (keycode == keyArriba)    { arriba = true; handled = true; }
        if (keycode == keyAbajo)     { abajo = true; handled = true; }
        if (keycode == keyIzquierda) { izquierda = true; handled = true; }
        if (keycode == keyDerecha)   { derecha = true; handled = true; }
        if (keycode == keyAccion)    { espacioPresionado = true; handled = true; }
        if (keycode == keySprint)    { sprintPresionado = true; handled = true; }
        return handled; // <-- SOLO true si esta instancia efectivamente manejó la tecla
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean handled = false;
        if (keycode == keyArriba)    { arriba = false; handled = true; }
        if (keycode == keyAbajo)     { abajo = false; handled = true; }
        if (keycode == keyIzquierda) { izquierda = false; handled = true; }
        if (keycode == keyDerecha)   { derecha = false; handled = true; }
        if (keycode == keyAccion)    { espacioPresionado = false; handled = true; }
        if (keycode == keySprint)    { sprintPresionado = false; handled = true; }
        return handled; // <-- idem
    }

    public void actualizar(float delta) {
        personaje.actualizarEstadoJugador(arriba, abajo, izquierda, derecha, sprintPresionado, delta);
        personaje.setEspacioPresionado(espacioPresionado);
    }

    // Métodos no usados
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
}
