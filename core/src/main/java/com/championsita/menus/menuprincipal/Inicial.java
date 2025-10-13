package com.championsita.menus.menuprincipal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.Principal;
import com.championsita.menus.menueleccion.Doble;

public class Inicial extends com.championsita.menus.Menu {

    private Sprite[] botones;
    private float anchoBotones;
    private float altoBotones;

    public Inicial(Principal juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();

        this.botones = new Sprite[] {
                new Sprite(new Texture("menuInicial/onlineBoton.png")),
                new Sprite(new Texture("menuInicial/localBoton.png")),
                new Sprite(new Texture("menuInicial/2jugadoresBoton.png")),
                new Sprite(new Texture("menuInicial/practicaBoton.png")),
                new Sprite(new Texture("menuInicial/opcionesBoton.png")),
                new Sprite(new Texture("menuInicial/salirBoton.png"))
        };

        this.anchoBotones = botones[0].getWidth() - 100;
        this.altoBotones = botones[0].getHeight() - 20;

        int apilar = 0;
        for(int i = 0; i < botones.length; i++) {
            botones[i].setSize(anchoBotones, altoBotones);
            float xBoton = super.anchoPantalla / 2f - anchoBotones / 2f;
            float yBoton = super.altoPantalla / 1.6f - altoBotones / 2f - apilar;
            this.botones[i].setPosition(xBoton, yBoton);
            apilar += 80;
        }

        int cantBotones = 6;
        super.inicializarSonido(cantBotones);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        super.batch.begin();
        super.render(delta);

        for(int i = 0; i < this.botones.length; i++) {
            this.botones[i].draw(batch);
        }

        super.batch.end();
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        y = Gdx.graphics.getHeight() - y;

        int i = 0;
        boolean clickeado = false;

        do {
            Sprite boton = botones[i];

            float bx = boton.getX();
            float by = boton.getY();

            if (x >= bx && x <= bx + anchoBotones &&
                    y >= by && y <= by + altoBotones) {
                cambiarMenu(i);
                clickeado = true;
            }

            i++;
        } while (i < botones.length && !clickeado);

        return clickeado;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        boolean paso = false;
        int i = 0;

        do {
            Sprite boton = botones[i];

            boolean dentro = super.condicionDentro(x, y, boton);
            super.condicionColor(dentro, boton);

            super.reproducirSonido(i, dentro);

            i++;
        } while (i < botones.length);

        return paso;
    }

    private void cambiarMenu(int i) {
        switch (i) {
            case 0: break;

            case 1: break;

            case 2: {
                Doble doble = new Doble(super.juego);
                super.juego.actualizarPantalla(doble);
                break;
            }

            case 3: break;

            case 4: break;

            case 5: {
                Gdx.app.exit();
                break;
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
