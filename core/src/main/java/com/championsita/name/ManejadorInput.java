package com.championsita.name;

import com.badlogic.gdx.InputProcessor;

public class ManejadorInput implements InputProcessor {

    private final Personaje personaje;
    private final int arriba, abajo, izquierda, derecha, accion;

    private boolean arribaPresionada, abajoPresionada, izquierdaPresionada, derechaPresionada, accionPresionada;

    public ManejadorInput(Personaje personaje, int arriba, int abajo, int izquierda, int derecha, int accion) {
        this.personaje = personaje;
        this.arriba = arriba;
        this.abajo = abajo;
        this.izquierda = izquierda;
        this.derecha = derecha;
        this.accion = accion;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == arriba) arribaPresionada = true;
        if(keycode == abajo) abajoPresionada = true;
        if(keycode == izquierda) izquierdaPresionada = true;
        if(keycode == derecha) derechaPresionada = true;
        if(keycode == accion) accionPresionada = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == arriba) arribaPresionada = false;
        if(keycode == abajo) abajoPresionada = false;
        if(keycode == izquierda) izquierdaPresionada = false;
        if(keycode == derecha) derechaPresionada = false;
        if(keycode == accion) accionPresionada = false;
        return false;
    }

    public void actualizar(float delta) {
        personaje.moverDesdeInput(arribaPresionada, abajoPresionada, izquierdaPresionada, derechaPresionada, delta);
        personaje.setEspacioPresionado(accionPresionada);
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