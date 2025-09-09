package com.championsita.name.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.name.Principal;

public class Doble extends Menu {

    private Texture fondo;
    private Sprite[] personajesWASD;
    private Sprite[] personajesAIAD;
    private int indiceWASD, indiceAIAD;


    public Doble(Principal juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);
        this.fondo = new Texture("menuDosJugadores/menuElegir2.png");
        super.fondoSprite.setTexture(this.fondo);

        this.personajesWASD = new Sprite[] {
                new Sprite(new Texture("jugador/amarillo/Jugador.png")),
                new Sprite(new Texture("jugador/celeste/Jugador.png"))
        };

        this.personajesAIAD = new Sprite[] {
                new Sprite(new Texture("jugador/rojo/Jugador.png")),
                new Sprite(new Texture("jugador/verde/Jugador.png"))
        };

        for(int i = 0; i < this.personajesWASD.length; i++) {
            this.personajesWASD[i].setPosition(130, 75);
            this.personajesWASD[i].setSize(400, 400);
            this.personajesAIAD[i].setPosition(515, 75);
            this.personajesAIAD[i].setSize(400, 400);
        }

        super.crearFlechas(4);

        int y = 166;

        super.flechas[0].setPosition(134, y);
        super.flechas[1].setPosition(455, y);
        super.flechas[2].setPosition(519, y);
        super.flechas[3].setPosition(839, y);

        int cantBotonesHabiles = 2;
        super.inicializarSonido(cantBotonesHabiles);
    }

    private void mostrarPersonaje(int i) {
        switch(i) {
            case 0: {
                if(this.indiceWASD - 1 != -1) {
                    this.indiceWASD--;
                }
                else {
                    this.indiceWASD = this.personajesWASD.length - 1;
                }
                break;
            }

            case 1: {
                if(this.indiceWASD + 1 != this.personajesWASD.length) {
                    this.indiceWASD++;
                }
                else {
                    this.indiceWASD = 0;
                }
                break;
            }

            case 2: {
                if(this.indiceAIAD - 1 != -1) {
                    this.indiceAIAD--;
                }
                else {
                    this.indiceAIAD = this.personajesAIAD.length - 1;
                }
                break;
            }

            case 3: {
                if(this.indiceAIAD + 1 != this.personajesWASD.length) {
                    this.indiceAIAD++;
                }
                else {
                    this.indiceAIAD = 0;
                }
                break;
            }
        }
    }

    @Override
    public void render(float delta) {
        super.batch.begin();
        super.render(delta);
        super.cargarAtrasSiguiente();
        for(int i = 0; i < super.flechas.length; i++) {
            this.flechas[i].draw(super.batch);
        }
        this.personajesWASD[this.indiceWASD].draw(super.batch);
        this.personajesAIAD[this.indiceAIAD].draw(super.batch);
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
        }
        while (i < this.flechas.length && !paso);

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

            if(dentro) {
                mostrarPersonaje(i);
                clickeado = true;
            }
            else {
                clickeado = false;
            }

            i++;
        }
        while (i < super.flechas.length && !clickeado);

        clickeado = condicionDentro(x, y, super.atrasSprite);
        super.cambiarMenu(clickeado, new Inicial(super.juego));

        clickeado = condicionDentro(x, y, super.siguienteSprite);
        super.cambiarMenu(clickeado, new Carga(super.juego, true));

        return clickeado;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.fondo.dispose();
        this.flecha.dispose();
        this.flechaCursor.dispose();
        for(int i = 0; i < this.personajesWASD.length; i++) {
            this.personajesWASD[i].getTexture().dispose();
        }
        for(int i = 0; i < this.personajesAIAD.length; i++) {
            this.personajesAIAD[i].getTexture().dispose();
        }
    }
}







