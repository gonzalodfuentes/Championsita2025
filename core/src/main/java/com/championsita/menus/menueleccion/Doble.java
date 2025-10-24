package com.championsita.menus.menueleccion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.Principal;
import com.championsita.menus.menucarga.Carga;
import com.championsita.menus.menuprincipal.Inicial;
import com.championsita.menus.menuprincipal.Menu;
import com.championsita.menus.compartido.Assets;

/**
 * Menú de selección de skins para el modo de 2 jugadores.
 * Controla dos sets de personajes (WASD / IJKL) y navega al menú de Carga.
 */
public class Doble extends Menu {

    private Sprite spriteWASD;
    private Sprite spriteIJKL;
    private JugadorUno[] skinsWASD;
    private JugadorDos[] skinsIJKL;
    private int indiceWASD;
    private int indiceIJKL;

    private final String modoDestino; // "1v1" o "practica" según origen

    public Doble(Principal juego, String modoDestino) {
        super(juego);
        this.modoDestino = modoDestino == null ? "1v1" : modoDestino;
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);

        super.fondoSprite.setTexture(Assets.tex("menuDosJugadores/menuElegir2.png"));

        this.skinsWASD = JugadorUno.values();
        this.skinsIJKL = JugadorDos.values();
        this.indiceWASD = 0;
        this.indiceIJKL = 0;

        this.spriteWASD = crearSpriteJugador(skinsWASD[indiceWASD].getNombre(), 130, 75);
        this.spriteIJKL = crearSpriteJugador(skinsIJKL[indiceIJKL].getNombre(), 515, 75);

        super.crearFlechas(4);
        int y = 166;
        super.flechas[0].setPosition(134, y); // WASD izquierda
        super.flechas[1].setPosition(455, y); // WASD derecha
        super.flechas[2].setPosition(519, y); // IJKL izquierda
        super.flechas[3].setPosition(839, y); // IJKL derecha

        // Sonidos: 4 flechas + atrás + ok → 6 slots
        super.inicializarSonido(6);
    }

    private Sprite crearSpriteJugador(String nombre, float x, float y) {
        String path = "jugador/" + nombre.toLowerCase() + "/Jugador.png";
        Sprite sprite = new Sprite(Assets.tex(path));
        sprite.setPosition(x, y);
        sprite.setSize(400, 400);
        return sprite;
    }

    private void cambiarSkin(int i) {
        switch (i) {
            case 0 -> {
                indiceWASD = (indiceWASD - 1 + skinsWASD.length) % skinsWASD.length;
                spriteWASD.setTexture(Assets.tex("jugador/" + skinsWASD[indiceWASD].getNombre().toLowerCase() + "/Jugador.png"));
            }
            case 1 -> {
                indiceWASD = (indiceWASD + 1) % skinsWASD.length;
                spriteWASD.setTexture(Assets.tex("jugador/" + skinsWASD[indiceWASD].getNombre().toLowerCase() + "/Jugador.png"));
            }
            case 2 -> {
                indiceIJKL = (indiceIJKL - 1 + skinsIJKL.length) % skinsIJKL.length;
                spriteIJKL.setTexture(Assets.tex("jugador/" + skinsIJKL[indiceIJKL].getNombre().toLowerCase() + "/Jugador.png"));
            }
            case 3 -> {
                indiceIJKL = (indiceIJKL + 1) % skinsIJKL.length;
                spriteIJKL.setTexture(Assets.tex("jugador/" + skinsIJKL[indiceIJKL].getNombre().toLowerCase() + "/Jugador.png"));
            }
        }
    }

    @Override
    public void render(float delta) {
        super.batch.begin();
        super.render(delta);
        super.cargarAtrasSiguiente();
        for (Sprite f : super.flechas) f.draw(super.batch);
        spriteWASD.draw(super.batch);
        spriteIJKL.draw(super.batch);
        super.batch.end();
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        boolean hit = false;

        for (int i = 0; i < super.flechas.length; i++) {
            boolean dentro = super.condicionFlechas(super.flechas[i], x, y);
            super.reproducirSonido(i, dentro);
            hit |= dentro;
        }

        boolean dentroAtras = condicionDentro(x, y, super.atrasSprite);
        condicionColor(dentroAtras, super.atrasSprite);
        super.reproducirSonido(4, dentroAtras);

        boolean dentroOk = condicionDentro(x, y, super.siguienteSprite);
        condicionColor(dentroOk, super.siguienteSprite);
        super.reproducirSonido(5, dentroOk);

        return hit || dentroAtras || dentroOk;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        y = Gdx.graphics.getHeight() - y;

        // Flechas de selección
        for (int i = 0; i < super.flechas.length; i++) {
            if (super.condicionFlechas(super.flechas[i], x, y)) {
                cambiarSkin(i);
                return true;
            }
        }

        // Atrás → volver a Inicial
        if (condicionDentro(x, y, super.atrasSprite)) {
            super.cambiarMenu(true, new Inicial(super.juego));
            return true;
        }

        // OK → continuar a Carga con las skins elegidas y el modo de destino
        if (condicionDentro(x, y, super.siguienteSprite)) {
            super.juego.actualizarPantalla(new Carga(
                    super.juego,
                    skinsWASD[indiceWASD].getNombre(),
                    skinsIJKL[indiceIJKL].getNombre(),
                    this.modoDestino
            ));
            return true;
        }

        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
