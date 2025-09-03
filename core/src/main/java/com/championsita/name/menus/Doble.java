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
    private Texture jugar;
    private Sprite jugarSprite;


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

        super.crearAtras(140, 70, 15, 670);
        super.crearFlechas(4);

        int y = 166;

        super.flechas[0].setPosition(134, y);
        super.flechas[1].setPosition(455, y);
        super.flechas[2].setPosition(519, y);
        super.flechas[3].setPosition(839, y);

        this.jugar = new Texture("menuDosJugadores/okBoton.png");
        this.jugarSprite = new Sprite(this.jugar);
        this.jugarSprite.setSize(140, 70);
        this.jugarSprite.setPosition(Gdx.graphics.getWidth() - this.jugarSprite.getWidth() - 15, 670);
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
        super.cargarAtras();
        for(int i = 0; i < super.flechas.length; i++) {
            this.flechas[i].draw(super.batch);
        }
        this.jugarSprite.draw(super.batch);
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
            paso = condicionFlechas(super.flechas[i], x, y);
            i++;
        }
        while (i < this.flechas.length && !paso);

        paso = super.condicionAtras(x, y, super.atrasSprite, false);

        paso = condicionJugar(x, y);
        if(paso) {
            this.jugarSprite.setColor(0, 1, 0, 1);
        }
        else {
            this.jugarSprite.setColor(super.colorBoton);
        }

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

        clickeado = super.condicionAtras(x, y, this.atrasSprite, true);
        if(clickeado) {
            Menu inicial = new Inicial(super.juego);
            super.juego.actualizarPantalla(inicial);
        }

        clickeado = condicionJugar(x, y);
        if(clickeado) {
            Carga carga = new Carga(super.juego, true);
            super.juego.actualizarPantalla(carga);
        }

        return clickeado;
    }

    private boolean condicionJugar(int x, int y) {
        boolean dentro = true ? x >= this.jugarSprite.getX() && x <= this.jugarSprite.getX() + this.jugarSprite.getWidth() &&
                y >= this.jugarSprite.getY() && y <= this.jugarSprite.getY() + this.jugarSprite.getHeight() : false;

        return dentro;
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







