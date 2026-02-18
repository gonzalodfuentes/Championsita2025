package com.championsita.menus.menuopcion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.Principal;
import com.championsita.menus.compartido.Assets;
import com.championsita.menus.menuprincipal.GestorInputMenu;
import com.championsita.menus.menuprincipal.Inicial;
import com.championsita.menus.menuprincipal.Menu;
import com.championsita.menus.menuprincipal.RenderizadorDeMenu;

public class Opcion extends Menu {

    private Sprite[] opciones;
    private Sprite colorSprite;
    private Nivel niveles;
    private Hover hover;
    private int indiceVolumen;
    private int indiceHover;
    private GestorInputMenu gestorMenu;
    private RenderizadorDeMenu renderizador;

    public Opcion(Principal juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();
        this.indiceVolumen = super.juego.getIndiceMusica();
        this.opciones = new Sprite[this.niveles.values().length];
        for(int i = 0; i < this.niveles.values().length; i++) {
            this.opciones[i] = new Sprite(this.niveles.values()[i].getTextura());
            this.opciones[i].setSize(500, 300);
            this.opciones[i].setPosition((super.anchoPantalla / 2f - this.opciones[i].getWidth() / 2f) + 250,
                    (super.altoPantalla / 2f - this.opciones[i].getHeight() / 2f) - 40);
        }
        this.colorSprite = new Sprite(Assets.tex("opcion/color.png"));
        this.colorSprite.setSize(280, 95);
        this.colorSprite.setPosition((super.anchoPantalla / 2f - this.colorSprite.getWidth() / 2f) - 250,
                (super.altoPantalla / 2f - this.colorSprite.getHeight() / 2f) - 40);
        this.colorSprite.setColor(super.juego.getAccionColor());

        Gdx.input.setInputProcessor(this);

        this.gestorMenu = new GestorInputMenu(this);
        this.renderizador = new RenderizadorDeMenu(this);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        super.batch.begin();
        this.renderizador.renderFondo(delta);
        this.opciones[this.indiceVolumen].draw(super.batch);
        super.atrasSprite.draw(super.batch);
        this.colorSprite.draw(super.batch);
        super.batch.end();
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        boolean dentro = this.gestorMenu.condicionDentro(x, y, super.atrasSprite);
        this.gestorMenu.condicionColor(dentro, super.atrasSprite);
        super.reproducirSonido(0, dentro);

        return dentro;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        y = Gdx.graphics.getHeight() - y;
        boolean dentro = this.gestorMenu.condicionDentro(x, y, this.opciones[this.indiceVolumen]);

        if(dentro) {
            if(button == Input.Buttons.LEFT) {
                this.indiceVolumen = this.indiceVolumen == 0 ? this.opciones.length - 1 : this.indiceVolumen - 1;
            }
            else if(button == Input.Buttons.RIGHT) {
                this.indiceVolumen = this.indiceVolumen == this.opciones.length - 1 ? 0 : this.indiceVolumen + 1;
            }
            float nuevoVolumen = Nivel.values()[this.indiceVolumen].getVolumen();
            super.juego.setIndiceMusica(this.indiceVolumen);
            super.demostracionTemporalMusica(nuevoVolumen);
            super.juego.setVolumenMusica(nuevoVolumen);
        }

        boolean atras = this.gestorMenu.condicionDentro(x, y, super.atrasSprite);
        if(atras) {
            super.juego.actualizarPantalla(new Inicial(super.juego));
        }

        int cantHovers = this.hover.values().length;
        boolean hover = this.gestorMenu.condicionDentro(x, y, this.colorSprite);
        if(hover) {
            if(button == Input.Buttons.RIGHT) {
                this.indiceHover = this.indiceHover == 0 ? cantHovers - 1 : this.indiceHover - 1;
            }
            else if(button == Input.Buttons.LEFT) {
                this.indiceHover = this.indiceHover == cantHovers - 1 ? 0 : this.indiceHover + 1;
            }
            this.colorSprite.setColor(this.hover.values()[this.indiceHover].getColor());
            super.juego.setAccionColor(this.hover.values()[this.indiceHover].getColor());
        }

        return dentro || atras || hover;
    }

    @Override
    public void dispose() {
        this.renderizador = null;
        this.gestorMenu = null;
    }
}











