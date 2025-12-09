package com.championsita.menus.local;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.Principal;
import com.championsita.menus.menucarga.Carga;
import com.championsita.menus.menueleccion.*;
import com.championsita.menus.menuprincipal.GestorInputMenu;
import com.championsita.menus.menuprincipal.Inicial;
import com.championsita.menus.menuprincipal.Menu;
import com.championsita.menus.compartido.Assets;
import com.championsita.menus.menuprincipal.RenderizadorDeMenu;

public class Local extends Menu {

    private Sprite[] botones; // [0]=2 Jugadores, [1]=Práctica
    private float anchoBoton;
    private float altoBoton;
    GestorInputMenu gestorMenu;
    RenderizadorDeMenu renderizador;

    public Local(Principal juego) { super(juego); }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);

        // Reutiliza imágenes existentes para mantener estética
        botones = new Sprite[] {
                new Sprite(Assets.tex("menuInicial/2jugadoresBoton.png")),
                new Sprite(Assets.tex("menuInicial/practicaBoton.png")),
                new Sprite(Assets.tex("Especial.png")),
                new Sprite(Assets.tex("menuInicial/futbolBoton.png")),
                new Sprite(Assets.tex("menuInicial/futsalBoton.png"))
        };

        // Tamaño y ubicación similares al menú inicial
        anchoBoton = botones[0].getWidth() - 100;
        altoBoton  = botones[0].getHeight() - 20;

        int apilar = 0;
        for (int i = 0; i < botones.length; i++) {
            botones[i].setSize(anchoBoton, altoBoton);
            float x = super.anchoPantalla / 2f - anchoBoton / 2f;
            float y = super.altoPantalla / 1.6f - altoBoton / 2f - apilar;
            botones[i].setPosition(x, y);
            apilar += 80;
        }

        // Sonidos: 2 botones + 1 atrás
        super.inicializarSonido(5);

        //Inicializacion Gestores-Herramientas
        gestorMenu = new GestorInputMenu(this);
        renderizador = new RenderizadorDeMenu(this);
    }

    @Override
    public void render(float delta) {
        super.batch.begin();
        super.render(delta); // fondo
        renderizador.cargarAtrasSiguiente(); // solo dibuja atrás en este menú
        for (Sprite b : botones) b.draw(super.batch);
        super.batch.end();
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        boolean hit = false;

        // Hover de botones
        for (int i = 0; i < botones.length; i++) {
            boolean dentro = gestorMenu.condicionDentro(x, y, botones[i]);
            gestorMenu.condicionColor(dentro, botones[i]);
            super.reproducirSonido(i, dentro);
            hit |= dentro;
        }

        // Hover de atrás (índice 2)
        boolean dentroAtras = gestorMenu.condicionDentro(x, y, super.atrasSprite);
        gestorMenu.condicionColor(dentroAtras, super.atrasSprite);
        super.reproducirSonido(2, dentroAtras);

        return hit || dentroAtras;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        y = Gdx.graphics.getHeight() - y;

        // 0) 2 Jugadores → flujo existente (pantalla Doble)
        if (gestorMenu.condicionDentro(x, y, botones[0])) {
            super.juego.actualizarPantalla(new Doble(super.juego, "1v1"));
            return true;
        }

        // 1) Práctica → placeholder; no navega aún
        if (gestorMenu.condicionDentro(x, y, botones[1])) {
            super.juego.actualizarPantalla(new UnJugador(super.juego, "practica"));
            return true;
        }

        if (gestorMenu.condicionDentro(x, y, botones[2])) {
            super.juego.actualizarPantalla(new Especial(super.juego));
            return true;
        }

        if(gestorMenu.condicionDentro(x, y, botones[3])) {
            super.juego.actualizarPantalla(new Futbol(super.juego, "futbol"));
            return true;
        }

        if(gestorMenu.condicionDentro(x, y, botones[4])) {
            super.juego.actualizarPantalla(new Futsal(super.juego, "futsal"));
        }

        // Atrás → volver al menú inicial
        if (gestorMenu.condicionDentro(x, y, super.atrasSprite)) {
            super.cambiarMenu(true, new Inicial(super.juego));
            return true;
        }

        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
        // Recursos bajo control de Assets
    }
}
