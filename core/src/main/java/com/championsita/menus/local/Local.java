package com.championsita.menus.local;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.Principal;
import com.championsita.menus.menuprincipal.Menu;
import com.championsita.menus.menueleccion.Doble;   // ya existe
import com.championsita.menus.menuprincipal.Inicial;
import com.championsita.menus.compartido.Assets;

public class Local extends Menu {

    private Sprite[] botones;
    private float anchoBoton;
    private float altoBoton;

    public Local(Principal juego) { super(juego); }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);

        // Reusamos tus PNG del menú inicial para mantener estética
        botones = new Sprite[] {
                new Sprite(Assets.tex("menuInicial/2jugadoresBoton.png")), // idx 0
                new Sprite(Assets.tex("menuInicial/practicaBoton.png"))    // idx 1
        };

        // Mismo sizing/stacking que usás en Inicial
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

        // slots de sonido: 2 botones + atrás + ok = 4
        super.inicializarSonido(4);
    }

    @Override
    public void render(float delta) {
        super.batch.begin();
        super.render(delta);
        cargarAtrasSiguiente();
        for (Sprite b : botones) b.draw(super.batch);
        super.batch.end();
    }

    @Override
    protected void cargarAtrasSiguiente() {
        this.atrasSprite.draw(this.batch);
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        boolean hit = false;

        for (int i = 0; i < botones.length; i++) {
            boolean dentro = condicionDentro(x, y, botones[i]);
            condicionColor(dentro, botones[i]);
            super.reproducirSonido(i, dentro);
            hit |= dentro;
        }

        boolean dentroAtras = condicionDentro(x, y, super.atrasSprite);
        condicionColor(dentroAtras, super.atrasSprite);
        super.reproducirSonido(2, dentroAtras);

        boolean dentroOk = condicionDentro(x, y, super.siguienteSprite);
        condicionColor(dentroOk, super.siguienteSprite);
        super.reproducirSonido(3, dentroOk);

        return hit || dentroAtras || dentroOk;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        y = Gdx.graphics.getHeight() - y;

        // 0) 2 Jugadores → al flujo existente (tu pantalla Doble)
        if (condicionDentro(x, y, botones[0])) {
            super.juego.actualizarPantalla(new Doble(super.juego));
            return true;
        }

        // 1) Práctica → por ahora no funciona (no navega)
        if (condicionDentro(x, y, botones[1])) {
            // Placeholder: no hace nada. Si querés, mostrar un sonido o log.
            return true;
        }

        // Atrás → volver al menú inicial
        if (condicionDentro(x, y, super.atrasSprite)) {
            super.cambiarMenu(true, new Inicial(super.juego));
            return true;
        }

        // OK → sin acción específica acá
        if (condicionDentro(x, y, super.siguienteSprite)) {
            return true;
        }

        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
        // Nada que liberar: todo via Assets
    }
}
