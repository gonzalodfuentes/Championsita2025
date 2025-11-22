package com.championsita.menus.menueleccion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.championsita.Principal;
import com.championsita.jugabilidad.modelo.Equipo;
import com.championsita.jugabilidad.modelo.HabilidadesEspeciales;
import com.championsita.menus.menucarga.Carga;
import com.championsita.menus.menuprincipal.GestorInputMenu;
import com.championsita.menus.menuprincipal.Inicial;
import com.championsita.menus.menuprincipal.Menu;
import com.championsita.menus.compartido.Assets;
import com.championsita.menus.menuprincipal.RenderizadorDeMenu;

public class Especial extends Menu {

    // Sprites de los personajes
    private Sprite spriteJ1;
    private Sprite spriteJ2;

    // Skins
    private JugadorUno[] skinsJ1;
    private JugadorDos[] skinsJ2;
    private int indiceSkinJ1;
    private int indiceSkinJ2;
    private Equipo equipoJ1 = Equipo.ROJO;
    private Equipo equipoJ2 = Equipo.AZUL;


    // Habilidades
    private HabilidadesEspeciales[] habilidades = HabilidadesEspeciales.values();
    private int indiceHabJ1 = 0;
    private int indiceHabJ2 = 0;

    // Texto y dibujado
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    // Gestores
    private GestorInputMenu gestorMenu;
    private RenderizadorDeMenu renderizador;

    private final String modoDestino = "especial";

    public Especial(Principal juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);

        // Fondo blanco (sin textura)
        Gdx.gl.glClearColor(1, 1, 1, 1);  // blanco total


        font = new BitmapFont();
        font.getData().setScale(1.8f);
        shapeRenderer = new ShapeRenderer();

        // ===============================
        // SKINS
        // ===============================
        skinsJ1 = JugadorUno.values();
        skinsJ2 = JugadorDos.values();
        indiceSkinJ1 = 0;
        indiceSkinJ2 = 0;


        spriteJ1 = crearSpriteJugador(skinsJ1[indiceSkinJ1].getNombre(), 130, 400);
        spriteJ2 = crearSpriteJugador(skinsJ2[indiceSkinJ2].getNombre(), 515, 400);

        gestorMenu = new GestorInputMenu(this);
        renderizador = new RenderizadorDeMenu(this);

        // ===============================
        // Flechas
        // 0-1 = skin J1
        // 2-3 = skin J2
        // 4-5 = habilidad J1
        // 6-7 = habilidad J2
        // ===============================
        renderizador.crearFlechas(8);

        int ySkin = 500;   // antes 166 → mucho más alto
        int yHab  = 120;   // antes 70

        // SKIN J1
        super.flechas[0].setPosition(120, ySkin);
        super.flechas[1].setPosition(470, ySkin);

        // SKIN J2
        super.flechas[2].setPosition(520, ySkin);
        super.flechas[3].setPosition(870, ySkin);

        // HABILIDAD J1
        super.flechas[4].setPosition(170, yHab);
        super.flechas[5].setPosition(420, yHab);

        // HABILIDAD J2
        super.flechas[6].setPosition(570, yHab);
        super.flechas[7].setPosition(830, yHab);

        // Sonido: 8 flechas + atrás + ok
        super.inicializarSonido(10);
    }

    private Sprite crearSpriteJugador(String nombre, float x, float y) {
        String path = "jugador/" + nombre.toLowerCase() + "/Jugador.png";
        Sprite sprite = new Sprite(Assets.tex(path));
        sprite.setPosition(x, y);
        sprite.setSize(400, 400);
        return sprite;
    }

    private void cambiarSkin(int i) {
        switch (i) {
            case 0 -> {
                indiceSkinJ1 = (indiceSkinJ1 - 1 + skinsJ1.length) % skinsJ1.length;
                spriteJ1.setTexture(Assets.tex(
                        "jugador/" + skinsJ1[indiceSkinJ1].getNombre().toLowerCase() + "/Jugador.png"));
            }
            case 1 -> {
                indiceSkinJ1 = (indiceSkinJ1 + 1) % skinsJ1.length;
                spriteJ1.setTexture(Assets.tex(
                        "jugador/" + skinsJ1[indiceSkinJ1].getNombre().toLowerCase() + "/Jugador.png"));
            }
            case 2 -> {
                indiceSkinJ2 = (indiceSkinJ2 - 1 + skinsJ2.length) % skinsJ2.length;
                spriteJ2.setTexture(Assets.tex(
                        "jugador/" + skinsJ2[indiceSkinJ2].getNombre().toLowerCase() + "/Jugador.png"));
            }
            case 3 -> {
                indiceSkinJ2 = (indiceSkinJ2 + 1) % skinsJ2.length;
                spriteJ2.setTexture(Assets.tex(
                        "jugador/" + skinsJ2[indiceSkinJ2].getNombre().toLowerCase() + "/Jugador.png"));
            }
        }
    }

    private void cambiarHabilidad(int i) {
        switch (i) {
            case 4 -> indiceHabJ1 = (indiceHabJ1 - 1 + habilidades.length) % habilidades.length;
            case 5 -> indiceHabJ1 = (indiceHabJ1 + 1) % habilidades.length;
            case 6 -> indiceHabJ2 = (indiceHabJ2 - 1 + habilidades.length) % habilidades.length;
            case 7 -> indiceHabJ2 = (indiceHabJ2 + 1) % habilidades.length;
        }
    }

    @Override
    public void render(float delta) {

        // Fondo blanco
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.batch.begin();

        // =========================
        // Dibujado común del menú
        // =========================
        renderizador.cargarAtrasSiguiente();
        for (Sprite f : super.flechas) f.draw(super.batch);
        spriteJ1.draw(super.batch);
        spriteJ2.draw(super.batch);

        // =========================
        // Dibujar texto (habilidad actual)
        // =========================
        font.setColor(0, 0, 0, 1);

        font.draw(super.batch, habilidades[indiceHabJ1].name(),
                250, 130);

        font.draw(super.batch, habilidades[indiceHabJ2].name(),
                640, 130);

        super.batch.end();
    }


    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        boolean hit = false;

        for (int i = 0; i < super.flechas.length; i++) {
            boolean dentro = gestorMenu.condicionFlechas(super.flechas[i], x, y);
            super.reproducirSonido(i, dentro);
            hit |= dentro;
        }

        boolean dentroAtras = gestorMenu.condicionDentro(x, y, super.atrasSprite);
        super.reproducirSonido(8, dentroAtras);

        boolean dentroOk = gestorMenu.condicionDentro(x, y, super.siguienteSprite);
        super.reproducirSonido(9, dentroOk);

        return hit || dentroAtras || dentroOk;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        y = Gdx.graphics.getHeight() - y;

        // Flechas
        for (int i = 0; i < super.flechas.length; i++) {
            if (gestorMenu.condicionFlechas(super.flechas[i], x, y)) {
                if (i < 4) cambiarSkin(i);
                else cambiarHabilidad(i);
                return true;
            }
        }

        // Atrás
        if (gestorMenu.condicionDentro(x, y, super.atrasSprite)) {
            super.cambiarMenu(true, new Inicial(super.juego));
            return true;
        }

        // OK
        if (gestorMenu.condicionDentro(x, y, super.siguienteSprite)) {
            String skin1 = skinsJ1[indiceSkinJ1].getNombre();
            String skin2 = skinsJ2[indiceSkinJ2].getNombre();

            super.juego.actualizarPantalla(
                    new Carga(super.juego, skin1, skin2, modoDestino, equipoJ1, equipoJ2)
            );
            return true;
        }

        return false;
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) shapeRenderer.dispose();
        shapeRenderer = null;
        font = null;
    }
}
