package com.championsita.name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.name.menus.Inicial;
import com.championsita.name.menus.Menu;

public class Principal extends Game {

    private SpriteBatch batch;
    private Menu menu;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.menu = new Inicial(this);
        this.actualizarPantalla(this.menu);
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }

    public void actualizarPantalla(Object futuraPantalla) {
        setScreen((Screen) futuraPantalla);
    }

    @Override
    public void dispose() {
        this.batch.dispose();
    }
}
