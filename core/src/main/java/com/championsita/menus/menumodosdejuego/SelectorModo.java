package com.championsita.menus.menumodosdejuego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.Principal;
import com.championsita.menus.menuprincipal.Menu;
import com.championsita.menus.menucarga.Carga;
import com.championsita.menus.menueleccion.Doble;
import com.championsita.menus.menuprincipal.Inicial;
import com.championsita.menus.compartido.Assets;

public class SelectorModo extends Menu {

    private Sprite[] botones;
    private float anchoBoton;
    private float altoBoton;

    public SelectorModo(Principal juego) { super(juego); }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);

        // Reutilizo tus texturas de menú inicial (estética consistente)
        botones = new Sprite[] {
                new Sprite(Assets.tex("menuInicial/localBoton.png")),       // 0 = LOCAL
                new Sprite(Assets.tex("menuInicial/2jugadoresBoton.png")),  // 1 = 2 JUGADORES
                new Sprite(Assets.tex("menuInicial/practicaBoton.png"))     // 2 = PRÁCTICA
        };

        // Mismo sizing y layout que usás en Inicial
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

        // Sonidos: 3 botones propios + 2 globales (atrás/ok) → total 5 “slots”
        super.inicializarSonido(5);
    }

    @Override
    public void render(float delta) {
        super.batch.begin();
        super.render(delta);
        super.cargarAtrasSiguiente();
        for (Sprite b : botones) b.draw(super.batch);
        super.batch.end();
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        boolean hitAlgo = false;

        // Hover de los tres botones azules
        for (int i = 0; i < botones.length; i++) {
            boolean dentro = condicionDentro(x, y, botones[i]);
            condicionColor(dentro, botones[i]);
            super.reproducirSonido(i, dentro);
            hitAlgo |= dentro;
        }

        // Hover atrás/ok (índices 3 y 4)
        boolean dentroAtras = condicionDentro(x, y, super.atrasSprite);
        condicionColor(dentroAtras, super.atrasSprite);
        super.reproducirSonido(3, dentroAtras);

        boolean dentroOk = condicionDentro(x, y, super.siguienteSprite);
        condicionColor(dentroOk, super.siguienteSprite);
        super.reproducirSonido(4, dentroOk);

        return hitAlgo || dentroAtras || dentroOk;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        y = Gdx.graphics.getHeight() - y;

        // Click en botones azules
        for (int i = 0; i < botones.length; i++) {
            if (condicionDentro(x, y, botones[i])) {
                navegar(i);
                return true;
            }
        }

        // Atrás
        if (condicionDentro(x, y, super.atrasSprite)) {
            super.cambiarMenu(true, new Inicial(super.juego));
            return true;
        }

        // OK (opcional: podría no hacer nada aquí)
        if (condicionDentro(x, y, super.siguienteSprite)) {
            // No hace nada específico; mantenemos por consistencia visual
            return true;
        }

        return false;
    }

    private void navegar(int idx) {
        switch (idx) {
            case 0: // LOCAL (placeholder por ahora)
                // Podés conectar cuando implementes el modo local.
                super.juego.actualizarPantalla(new Inicial(super.juego));
                break;
            case 1: // 2 JUGADORES
                super.juego.actualizarPantalla(new Doble(super.juego));
                break;
            case 2: // PRÁCTICA
                // Por ahora, reuso Carga con skins por defecto
                super.juego.actualizarPantalla(new Carga(super.juego, "Amarillo", "Rojo"));
                break;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        // Nada que liberar: todo via Assets.
    }
}
