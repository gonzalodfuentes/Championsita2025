package com.championsita.menus.menuprincipal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.Principal;
import com.championsita.menus.local.Local;
import com.championsita.menus.compartido.Assets;

public class Inicial extends Menu {

    private Sprite[] botones;
    private float anchoBotones;
    private float altoBotones;
    private GestorInputMenu gestorMenu;
    private RenderizadorDeMenu renderizador;
    private GestorSonidoMenu gestorSonido;

    public Inicial(Principal juego) { super(juego); }

    @Override
    public void show() {
        super.show();

        // Cargar botones usando Assets para mantener el ciclo de vida unificado
        this.botones = new Sprite[] {
                new Sprite(Assets.tex("menuInicial/onlineBoton.png")),
                new Sprite(Assets.tex("menuInicial/localBoton.png")),
                new Sprite(Assets.tex("menuInicial/opcionesBoton.png")),
                new Sprite(Assets.tex("menuInicial/salirBoton.png"))
        };

        this.anchoBotones = botones[0].getWidth() - 100;
        this.altoBotones  = botones[0].getHeight() - 20;

        int apilar = 0;
        for (int i = 0; i < botones.length; i++) {
            botones[i].setSize(anchoBotones, altoBotones);
            float x = super.anchoPantalla / 2f - anchoBotones / 2f;
            float y = super.altoPantalla / 1.6f - altoBotones / 2f - apilar;
            botones[i].setPosition(x, y);
            apilar += 80;
        }

        // Registrar input en este menú
        Gdx.input.setInputProcessor(this);

        //Inicializar Gestores-Herramientas
        gestorMenu = new GestorInputMenu(this);
        renderizador = new RenderizadorDeMenu(this);
        gestorSonido.inicializarSonido(6);
    }

    @Override
    public void render(float delta) {
        super.batch.begin();
        renderizador.renderFondo(delta); // dibuja el fondo
        for (Sprite b : this.botones) b.draw(super.batch);
        super.batch.end();
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        y = Gdx.graphics.getHeight() - y;

        for (int i = 0; i < botones.length; i++) {
            Sprite boton = botones[i];
            float bx = boton.getX();
            float by = boton.getY();
            if (x >= bx && x <= bx + anchoBotones && y >= by && y <= by + altoBotones) {
                cambiarMenu(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        boolean hitAlgo = false;

        for (int i = 0; i < botones.length; i++) {
            Sprite boton = botones[i];
            boolean dentro = gestorMenu.condicionDentro(x, y, boton);
            gestorMenu.condicionColor(dentro, boton);
            gestorSonido.reproducirSonido(i, dentro);
            hitAlgo |= dentro;
        }
        return hitAlgo;
    }

    private void cambiarMenu(int i) {
        switch (i) {
            case 0: {
                // ONLINE (pendiente)
                break;
            }
            case 1: {
                // LOCAL => submenú propio con 2 Jugadores y Práctica
                super.juego.actualizarPantalla(new Local(super.juego));
                break;
            }
            case 2: {
                // OPCIONES (pendiente)
                break;
            }
            case 3: {
                // SALIR
                Gdx.app.exit();
                break;
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        // Texturas y sonidos quedan bajo control de Assets
    }
}
