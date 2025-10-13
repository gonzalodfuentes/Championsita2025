package com.championsita.menus.menueleccion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.Principal;
import com.championsita.menus.Menu;
import com.championsita.menus.menucarga.Carga;
import com.championsita.menus.menuprincipal.Inicial;

public class Doble extends Menu {
    private Texture fondo;
    private JugadorUno[] skinsWASD;
    private JugadorDos[] skinsIJKL;
    private int indiceWASD;
    private int indiceIJKL;
    private Sprite spriteWASD;
    private Sprite spriteIJKL;

    public Doble(Principal juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);
        this.fondo = new Texture("menuDosJugadores/menuElegir2.png");
        super.fondoSprite.setTexture(this.fondo);

        this.skinsWASD = JugadorUno.values();
        this.skinsIJKL = JugadorDos.values();
        this.indiceWASD = 0;
        this.indiceIJKL = 0;

        this.spriteWASD = crearSpriteJugador(this.skinsWASD[this.indiceWASD].getNombre(), 130, 75);
        this.spriteIJKL = crearSpriteJugador(this.skinsIJKL[this.indiceIJKL].getNombre(), 515, 75);

        super.crearFlechas(4);
        int y = 166;
        super.flechas[0].setPosition(134, y);
        super.flechas[1].setPosition(455, y);
        super.flechas[2].setPosition(519, y);
        super.flechas[3].setPosition(839, y);

        int cantBotonesHabiles = 2;
        super.inicializarSonido(cantBotonesHabiles);
    }

    private Sprite crearSpriteJugador(String nombre, float x, float y) {
        Sprite sprite = new Sprite(new Texture("jugador/" + nombre.toLowerCase() + "/Jugador.png"));
        sprite.setPosition(x, y);
        sprite.setSize(400, 400);
        return sprite;
    }

    private void mostrarPersonaje(int i) {
        switch (i) {
            case 0:
                this.indiceWASD = (this.indiceWASD - 1 + this.skinsWASD.length) % this.skinsWASD.length;
                this.spriteWASD.setTexture(new Texture("jugador/" + this.skinsWASD[this.indiceWASD].getNombre().toLowerCase() + "/Jugador.png"));
                break;

            case 1:
                this.indiceWASD = (this.indiceWASD + 1) % this.skinsWASD.length;
                this.spriteWASD.setTexture(new Texture("jugador/" + this.skinsWASD[this.indiceWASD].getNombre().toLowerCase() + "/Jugador.png"));
                break;

            case 2:
                this.indiceIJKL = (this.indiceIJKL - 1 + this.skinsIJKL.length) % this.skinsIJKL.length;
                this.spriteIJKL.setTexture(new Texture("jugador/" + this.skinsIJKL[this.indiceIJKL].getNombre().toLowerCase() + "/Jugador.png"));
                break;

            case 3:
                this.indiceIJKL = (this.indiceIJKL + 1) % this.skinsIJKL.length;
                this.spriteIJKL.setTexture(new Texture("jugador/" + this.skinsIJKL[this.indiceIJKL].getNombre().toLowerCase() + "/Jugador.png"));
                break;
        }
    }

    @Override
    public void render(float delta) {
        super.batch.begin();
        super.render(delta);
        super.cargarAtrasSiguiente();

        for (int i = 0; i < super.flechas.length; i++) {
            this.flechas[i].draw(super.batch);
        }

        this.spriteWASD.draw(super.batch);
        this.spriteIJKL.draw(super.batch);
        super.batch.end();
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        boolean paso = false;
        y = Gdx.graphics.getHeight() - y;

        int i = 0;
        do {
            paso = super.condicionFlechas(super.flechas[i], x, y);
            i++;
        } while (i < this.flechas.length && !paso);

        paso = condicionDentro(x, y, super.atrasSprite);
        super.condicionColor(paso, super.atrasSprite);
        super.reproducirSonido(0, paso);

        paso = condicionDentro(x, y, super.siguienteSprite);
        super.condicionColor(paso, super.siguienteSprite);
        super.reproducirSonido(1, paso);

        return paso;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        boolean clickeado = false;
        int i = 0;
        y = Gdx.graphics.getHeight() - y;

        do {
            Sprite flecha = super.flechas[i];
            boolean dentro = super.condicionFlechas(flecha, x, y);
            if (dentro) {
                mostrarPersonaje(i);
                clickeado = true;
            } else {
                clickeado = false;
            }
            i++;
        } while (i < super.flechas.length && !clickeado);

        clickeado = condicionDentro(x, y, super.atrasSprite);
        super.cambiarMenu(clickeado, new Inicial(super.juego));
        clickeado = condicionDentro(x, y, super.siguienteSprite);
        super.cambiarMenu(clickeado, new Carga(super.juego, this.skinsWASD[this.indiceWASD].getNombre(), this.skinsIJKL[this.indiceIJKL].getNombre()));

        return clickeado;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.fondo.dispose();
        this.flecha.dispose();
        this.flechaCursor.dispose();
        this.spriteWASD.getTexture().dispose();
        this.spriteIJKL.getTexture().dispose();
    }
}
