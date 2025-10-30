package com.championsita;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.menus.menuprincipal.Inicial;
import com.championsita.menus.menuprincipal.Menu;

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

    public void actualizarPantalla(Screen futuraPantalla) {
        Screen anterior = getScreen();
        if (futuraPantalla == anterior) return;

        setScreen(futuraPantalla);

        // Esperar al siguiente frame para liberar la anterior
        if (anterior != null && anterior != futuraPantalla) {
            Gdx.app.postRunnable(anterior::dispose);
        }
    }

    @Override
    public void dispose() {
        this.batch.dispose();
    }
}
