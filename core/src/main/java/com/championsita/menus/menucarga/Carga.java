package com.championsita.menus.menucarga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Sound;
import com.championsita.Principal;
import com.championsita.menus.Menu;
import com.championsita.menus.menueleccion.Doble;
import com.championsita.partida.ControladorDePartida;

import java.util.HashMap;
import java.util.Map;

/**
 * Menú de "carga"/confirmación: muestra jugadores, campo, permite cambiar campo,
 * goles y tiempo, y lanza la partida.
 *
 * NOTA: asume que existe un enum Campo con getNombre() que coincide con los archivos
 * "campos/campo{nombre}.png"
 */
public class Carga extends Menu {

    // =====================================================
    // 🧩 1. Referencias base
    // =====================================================
    private SpriteBatch batch;
    private final String pielJugador1;
    private final String pielJugador2;

    // =====================================================
    // 🖼️ 2. Recursos gráficos (texturas y sprites)
    // =====================================================
    private Texture fondoTex;
    private Texture botonJugarTex;

    private Texture controlJugador1Tex;
    private Texture controlJugador2Tex;
    private Sprite controlJugador1;
    private Sprite controlJugador2;

    private Texture cartelCampoTex;
    private Sprite cartelCampo;

    private Texture campoTexActual;
    private Sprite campoSprite;

    private Texture[] golesTex;   // variantes 1,3,5
    private Texture[] tiempoTex;  // variantes 1,2,3
    private Sprite cartelGoles;
    private Sprite cartelTiempo;

    private Texture flechaNormalTex;
    private Texture flechaHoverTex;
    private Sprite flechaIzq;
    private Sprite flechaDer;

    // =====================================================
    // 🎮 3. Estado / selección
    // =====================================================
    private Campo[] listaCampos;
    private int indiceCampo;

    private final int[] opcionesGoles = {1, 3, 5};
    private final int[] opcionesTiempo = {1, 2, 3}; // 1=breve, 2=medio, 3=largo (ajuste libre)
    private int indiceGoles;   // índice dentro de opcionesGoles
    private int indiceTiempo;  // índice dentro de opcionesTiempo

    private float[] golesXY;    // para mantener posición tras cambiar textura
    private float[] tiempoXY;

    // Cache de texturas de campos para no recrear/filtrar memory leaks
    private final Map<String, Texture> cacheCampos = new HashMap<>();

    // =====================================================
    // 🔊 4. Sonidos y feedback
    // =====================================================
    private Sound hoverSound;
    private Color colorNormal;
    private Color colorHover;

    // =====================================================
    // 📐 5. Layout (constantes)
    // =====================================================
    private static final float ALTURA_CONTROLES = 335f;
    private static final float CAMPOS_PANEL_Y   = 215f;
    private static final float CAMPOS_ALTURA    = 200f;
    private static final float FLECHA_OFFSET    = 5f;

    public Carga(Principal juego, String pielUno, String pielDos) {
        super(juego);
        this.pielJugador1 = pielUno;
        this.pielJugador2 = pielDos;
    }

    @Override
    public void show() {
        super.show();

        // === batch y colores base (usa los de Menu) ===
        this.batch = this.juego.getBatch();
        this.colorNormal = new Color(super.atrasSprite.getColor());
        this.colorHover  = new Color(0, 1, 0, 1);

        // === Fondo específico de esta pantalla ===
        this.fondoTex = new Texture("menuCreacion/menuDosJug.png");
        super.fondoSprite.setTexture(this.fondoTex);

        // === Botón jugar (reemplaza "siguiente") ===
        this.botonJugarTex = new Texture("menuDosJugadores/jugarBoton.png");
        super.siguienteSprite.setTexture(this.botonJugarTex);

        // === Jugadores (iconos/controles) ===
        this.controlJugador1Tex = new Texture("menuCreacion/primerJugador.png");
        this.controlJugador2Tex = new Texture("menuCreacion/segundoJugador.png");
        this.controlJugador1 = new Sprite(this.controlJugador1Tex);
        this.controlJugador2 = new Sprite(this.controlJugador2Tex);
        colocarControles(ALTURA_CONTROLES);

        // === Campos ===
        this.listaCampos = Campo.values();
        this.indiceCampo = 0;

        this.cartelCampoTex = new Texture("menuCreacion/campoCartel.png");
        this.cartelCampo = new Sprite(this.cartelCampoTex);
        this.cartelCampo.setPosition(
                Gdx.graphics.getWidth() / 2f - this.cartelCampo.getWidth() / 2f,
                CAMPOS_PANEL_Y
        );

        this.campoTexActual = getCampoTexture(listaCampos[indiceCampo].getNombre());
        this.campoSprite = new Sprite(this.campoTexActual);
        float anchoCampo = this.cartelCampo.getWidth();
        this.campoSprite.setSize(anchoCampo, CAMPOS_ALTURA);
        this.campoSprite.setPosition(this.cartelCampo.getX(), this.cartelCampo.getY() - CAMPOS_ALTURA);

        // === Flechas para cambiar campo ===
        this.flechaNormalTex = new Texture("menuDosJugadores/flechaNormal.png");
        this.flechaHoverTex  = new Texture("menuDosJugadores/flechaInvertida.png");
        this.flechaIzq = new Sprite(this.flechaNormalTex);
        this.flechaDer = new Sprite(this.flechaNormalTex);
        float yFlechas = campoSprite.getY() + campoSprite.getHeight() / 3.5f - 20f;
        this.flechaIzq.setPosition(this.campoSprite.getX() - this.flechaIzq.getWidth() - FLECHA_OFFSET, yFlechas);
        this.flechaDer.setPosition(this.campoSprite.getX() + campoSprite.getWidth() + FLECHA_OFFSET, yFlechas);
        // rotación de la derecha para apuntar hacia la derecha si la textura viene "izquierda"
        this.flechaDer.setRotation(180f);

        // === Carteles de goles/tiempo (precargados para no crear texturas en click) ===
        this.golesTex  = new Texture[] {
                new Texture("menuCreacion/golesCartel1.png"),
                new Texture("menuCreacion/golesCartel3.png"),
                new Texture("menuCreacion/golesCartel5.png")
        };
        this.tiempoTex = new Texture[] {
                new Texture("menuCreacion/tiempoCartel1.png"),
                new Texture("menuCreacion/tiempoCartel2.png"),
                new Texture("menuCreacion/tiempoCartel3.png")
        };
        this.indiceGoles  = 0; // arranca en 1 gol (pos 0)
        this.indiceTiempo = 0; // arranca en 1 (pos 0)
        this.cartelGoles  = new Sprite(golesTex[indiceGoles]);
        this.cartelTiempo = new Sprite(tiempoTex[indiceTiempo]);

        int ubiX = 30, ubiY = 70;
        this.cartelGoles.setPosition(ubiX, ubiY);
        this.cartelTiempo.setPosition(Gdx.graphics.getWidth() - ubiX - this.cartelTiempo.getWidth(), ubiY);
        this.golesXY  = new float[]{ this.cartelGoles.getX(), this.cartelGoles.getY() };
        this.tiempoXY = new float[]{ this.cartelTiempo.getX(), this.cartelTiempo.getY() };

        // === Sonido hover (usa el mismo de Menu) ===
        this.hoverSound = super.sonido;
        super.inicializarSonido(2); // 0: atrás, 1: jugar

        // === Input de esta pantalla ===
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        super.render(delta);           // dibuja el fondo
        super.cargarAtrasSiguiente();  // dibuja atrás y jugar

        // jugadores
        controlJugador1.draw(batch);
        controlJugador2.draw(batch);

        // campo
        cartelCampo.draw(batch);
        campoSprite.draw(batch);

        // flechas de campo
        flechaIzq.draw(batch);
        flechaDer.draw(batch);

        // goles / tiempo
        cartelGoles.draw(batch);
        cartelTiempo.draw(batch);

        batch.end();
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;

        // Hover de goles/tiempo: cambia color
        boolean dentroG = hit(cartelGoles, x, y);
        cartelGoles.setColor(dentroG ? colorHover : colorNormal);

        boolean dentroT = hit(cartelTiempo, x, y);
        cartelTiempo.setColor(dentroT ? colorHover : colorNormal);

        // Hover de botones globales
        boolean dentroAtras = hit(super.atrasSprite, x, y);
        super.condicionColor(dentroAtras, super.atrasSprite);
        super.reproducirSonido(0, dentroAtras);

        boolean dentroJugar = hit(super.siguienteSprite, x, y);
        super.condicionColor(dentroJugar, super.siguienteSprite);
        super.reproducirSonido(1, dentroJugar);

        // Hover flechas (cambia a textura invertida)
        updateFlechaHover(flechaIzq, x, y, true);
        updateFlechaHover(flechaDer, x, y, false);

        return dentroAtras || dentroJugar || dentroG || dentroT;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        y = Gdx.graphics.getHeight() - y;
        boolean clic = false;

        // Click en goles → ciclo 1 → 3 → 5 → 1
        if (hit(cartelGoles, x, y)) {
            indiceGoles = (indiceGoles + 1) % opcionesGoles.length;
            cartelGoles.setTexture(golesTex[indiceGoles]);
            cartelGoles.setSize(cartelGoles.getTexture().getWidth(), cartelGoles.getTexture().getHeight());
            cartelGoles.setPosition(golesXY[0], golesXY[1]);
            clic = true;
        }

        // Click en tiempo → ciclo 1 → 2 → 3 → 1
        if (hit(cartelTiempo, x, y)) {
            indiceTiempo = (indiceTiempo + 1) % opcionesTiempo.length;
            cartelTiempo.setTexture(tiempoTex[indiceTiempo]);
            cartelTiempo.setSize(cartelTiempo.getTexture().getWidth(), cartelTiempo.getTexture().getHeight());
            cartelTiempo.setPosition(tiempoXY[0], tiempoXY[1]);
            clic = true;
        }

        // Flechas campo
        if (hit(flechaIzq, x, y)) {
            cambiarCampo(-1);
            clic = true;
        }
        if (hit(flechaDer, x, y)) {
            cambiarCampo(+1);
            clic = true;
        }

        // Botón atrás
        if (hit(super.atrasSprite, x, y)) {
            super.cambiarMenu(true, new Doble(super.juego));
            return true;
        }

        // Botón jugar → lanza la partida (mantengo su firma existente)
        if (hit(super.siguienteSprite, x, y)) {
            String nombreCampo = listaCampos[indiceCampo].getNombre();
            super.juego.actualizarPantalla(
                    new ControladorDePartida(nombreCampo, this.pielJugador1, this.pielJugador2)
            );
            return true;
        }

        return clic;
    }

    @Override
    public void dispose() {
        // CUIDADITO :P: super.dispose() ya hace dispose de fondo y música creados en Menu.
        // Acá liberamos SOLO lo que creamos en esta clase.
        safeDispose(fondoTex);
        safeDispose(botonJugarTex);

        safeDispose(controlJugador1Tex);
        safeDispose(controlJugador2Tex);

        safeDispose(cartelCampoTex);

        if (golesTex != null) for (Texture t : golesTex) safeDispose(t);
        if (tiempoTex != null) for (Texture t : tiempoTex) safeDispose(t);

        safeDispose(flechaNormalTex);
        safeDispose(flechaHoverTex);

        // Cache de campos
        for (Texture t : cacheCampos.values()) safeDispose(t);

        super.dispose();
    }

    // ==========================
    // Helpers privados
    // ==========================

    private void cambiarCampo(int delta) {
        int len = listaCampos.length;
        indiceCampo = (indiceCampo + delta + len) % len;
        String nombre = listaCampos[indiceCampo].getNombre();
        Texture tex = getCampoTexture(nombre);
        campoSprite.setTexture(tex);
        campoSprite.setSize(campoSprite.getTexture().getWidth(), campoSprite.getTexture().getHeight());
        campoSprite.setSize(cartelCampo.getWidth(), CAMPOS_ALTURA);
        campoSprite.setPosition(cartelCampo.getX(), cartelCampo.getY() - CAMPOS_ALTURA);
    }

    private Texture getCampoTexture(String nombre) {
        return cacheCampos.computeIfAbsent(nombre, n -> new Texture("campos/campo" + n + ".png"));
    }

    private boolean hit(Sprite s, int x, int y) {
        float sx = s.getX(), sy = s.getY(), sw = s.getWidth(), sh = s.getHeight();
        return x >= sx && x <= sx + sw && y >= sy && y <= sy + sh;
    }

    private void updateFlechaHover(Sprite flecha, int x, int y, boolean izquierda) {
        boolean dentro = hit(flecha, x, y);
        flecha.setTexture(dentro ? flechaHoverTex : flechaNormalTex);
        // Mantener rotación de la derecha
        if (!izquierda) flecha.setRotation(180f);
    }

    private void colocarControles(float y) {
        float xUno = Gdx.graphics.getWidth() / 2f - 184f - controlJugador1Tex.getWidth() / 2f;
        float xDos = Gdx.graphics.getWidth() / 2f + 200f - controlJugador2Tex.getWidth() / 2f;
        this.controlJugador1 = new Sprite(controlJugador1Tex);
        this.controlJugador2 = new Sprite(controlJugador2Tex);
        this.controlJugador1.setPosition(xUno, y);
        this.controlJugador2.setPosition(xDos, y);
    }

    private void safeDispose(Texture t) {
        if (t != null) t.dispose();
    }
}
