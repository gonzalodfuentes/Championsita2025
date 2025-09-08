package com.championsita.name;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class ManejadorInput implements InputProcessor {

    private final Personaje personaje;
    private final int arriba, abajo, izquierda, derecha, accion;

    private boolean arribaPresionada, abajoPresionada, izquierdaPresionada, derechaPresionada, accionPresionada;
    private boolean sprintPresionado;

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
        switch (keycode) {
            case Input.Keys.W: arriba = true; break;
            case Input.Keys.S: abajo = true; break;
            case Input.Keys.A: izquierda = true; break;
            case Input.Keys.D: derecha = true; break;
            case Input.Keys.SPACE: espacioPresionado = true; break;
            case Input.Keys.SHIFT_LEFT: sprintPresionado = true; break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W: arriba = false; break;
            case Input.Keys.S: abajo = false; break;
            case Input.Keys.A: izquierda = false; break;
            case Input.Keys.D: derecha = false; break;
            case Input.Keys.SPACE: espacioPresionado = false; break;
            case Input.Keys.SHIFT_LEFT: sprintPresionado = false; break;
        }
        return true;
    }

    // Llamá esto desde Principal.render()
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