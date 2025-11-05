package com.championsita.menus.menueleccion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.Principal;
import com.championsita.menus.menuprincipal.*;
import com.championsita.menus.compartido.Assets;
import com.championsita.menus.menucarga.Campo;
import com.championsita.partida.ControladorDePartida;

/**
 * Menú de selección para un solo jugador:
 * - Elige skin (JugadorUno)
 * - Elige campo (Campo)
 * Al confirmar, crea ControladorDePartida.Config y navega directo al juego
 * sin pasar por Carga.
 */
public class UnJugador extends Menu {

    // Selección de skin (usa el mismo enum que Doble)
    private JugadorUno[] skins;
    private int idxSkin;
    private Sprite spriteSkin;

    // Selección de campo (usa el enum Campo que ya tenés)
    private Campo[] campos;
    private int idxCampo;
    private Sprite previewCampo;

    private String skinP1; // se duplica para cumplir con el builder (jugador2Skin)
    private String skinP2;

    private final String modoDestino; // normalmente "practica"

    //Gestores y Herramientas
    GestorInputMenu gestorMenu;
    RenderizadorDeMenu renderizador;
    GestorSonidoMenu gestorSonido;

    public UnJugador(Principal juego, String modoDestino) {
        super(juego);
        this.modoDestino = (modoDestino == null ? "practica" : modoDestino);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);

        // Inicializar listas
        this.skins = JugadorUno.values();
        this.idxSkin = 0;
        this.skinP1 = skins[idxSkin].getNombre();
        this.skinP2 = skinP1;

        this.campos = Campo.values();
        this.idxCampo = 0;

        // Sprites
        this.spriteSkin = crearSpriteJugador(skinP1, 325, 75);

        // preview del campo (thumbnail)
        this.previewCampo = crearSpriteCampo(campos[idxCampo], 360, 390); // y aproximado bajo el primer bloque
        this.previewCampo.setSize(300, 170);

        // Sonidos: 4 flechas + atrás + ok => 6
        gestorSonido.inicializarSonido(6);

        //Inicializar Gestores/Herramientas
        this.gestorMenu = new GestorInputMenu(this);
        this.renderizador = new RenderizadorDeMenu(this);

        // Flechas: 4 en total (skin izq/der + campo izq/der)
        renderizador.crearFlechas(4);
        int ySkin = 166;
        super.flechas[0].setPosition(318, ySkin); // skin izquierda
        super.flechas[1].setPosition(639, ySkin); // skin derecha

        int yCampo = 390;
        super.flechas[2].setPosition(270, yCampo); // campo izquierda
        super.flechas[3].setPosition(700, yCampo); // campo derecha
    }

    private Sprite crearSpriteJugador(String nombre, float x, float y) {
        String path = "jugador/" + nombre.toLowerCase() + "/Jugador.png";
        Sprite s = new Sprite(Assets.tex(path));
        s.setPosition(x, y);
        s.setSize(400, 400);
        return s;
    }

    private Sprite crearSpriteCampo(Campo campo, float x, float y) {
        // Reutiliza el mismo patrón que usa ControladorDePartida para cargar la textura del campo
        String nombreCampo = campo.getNombre();
        String path = "campos/campo" + nombreCampo + ".png";
        Sprite s = new Sprite(Assets.tex(path));
        s.setPosition(x, y);
        return s;
    }

    private void cambiarSkin(boolean derecha) {
        if (derecha) {
            idxSkin = (idxSkin + 1) % skins.length;
        } else {
            idxSkin = (idxSkin - 1 + skins.length) % skins.length;
        }
        skinP1 = skins[idxSkin].getNombre();
        skinP2 = skinP1; // el builder exige dos skins
        spriteSkin.setTexture(Assets.tex("jugador/" + skinP1.toLowerCase() + "/Jugador.png"));
    }

    private void cambiarCampo(boolean derecha) {
        if (derecha) {
            idxCampo = (idxCampo + 1) % campos.length;
        } else {
            idxCampo = (idxCampo - 1 + campos.length) % campos.length;
        }
        Campo campo = campos[idxCampo];
        previewCampo.setTexture(Assets.tex("campos/campo" + campo.getNombre() + ".png"));
    }

    @Override
    public void render(float delta) {
        super.batch.begin();
        renderizador.renderFondo(delta);
        renderizador.cargarAtrasSiguiente();

        // Dibujar flechas
        for (Sprite f : super.flechas) f.draw(super.batch);

        // Bloque skin
        spriteSkin.draw(super.batch);

        // Bloque campo (preview)
        previewCampo.draw(super.batch);

        super.batch.end();
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        boolean hit = false;

        for (int i = 0; i < super.flechas.length; i++) {
            boolean dentro = gestorMenu.condicionFlechas(super.flechas[i], x, y);
            gestorSonido.reproducirSonido(i, dentro);
            hit |= dentro;
        }

        boolean dentroAtras = gestorMenu.condicionDentro(x, y, super.atrasSprite);
        gestorMenu.condicionColor(dentroAtras, super.atrasSprite);
        gestorSonido.reproducirSonido(4, dentroAtras);

        boolean dentroOk = gestorMenu.condicionDentro(x, y, super.siguienteSprite);
        gestorMenu.condicionColor(dentroOk, super.siguienteSprite);
        gestorSonido.reproducirSonido(5, dentroOk);

        return hit || dentroAtras || dentroOk;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        y = Gdx.graphics.getHeight() - y;

        // Flechas
        if (gestorMenu.condicionFlechas(super.flechas[0], x, y)) { cambiarSkin(false); return true; } // skin izq
        if (gestorMenu.condicionFlechas(super.flechas[1], x, y)) { cambiarSkin(true);  return true; } // skin der
        if (gestorMenu.condicionFlechas(super.flechas[2], x, y)) { cambiarCampo(false); return true; } // campo izq
        if (gestorMenu.condicionFlechas(super.flechas[3], x, y)) { cambiarCampo(true);  return true; } // campo der

        // Atrás → volver a Inicial
        if (gestorMenu.condicionDentro(x, y, super.atrasSprite)) {
            super.cambiarMenu(true, new Inicial(super.juego));
            return true;
        }

        // OK → construir config y entrar directo a la partida
        if (gestorMenu.condicionDentro(x, y, super.siguienteSprite)) {
            ControladorDePartida.Config cfg = new ControladorDePartida.Config.Builder()
                    .agregarSkin(skinP1)
                    .agregarSkin(skinP2)
                    .campo(campos[idxCampo])      // requerido
                    // goles y tiempo quedan con defaults del builder (UNO / CORTO)
                    .modo(modoDestino == null ? "practica" : modoDestino)
                    .build();

            super.juego.actualizarPantalla(new ControladorDePartida(cfg));
            return true;
        }

        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
