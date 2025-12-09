package com.championsita.menus.menucarga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Sound;
import com.championsita.Principal;
import com.championsita.jugabilidad.constantes.Constantes;
import com.championsita.jugabilidad.modelo.Equipo;
import com.championsita.jugabilidad.modelo.HabilidadesEspeciales;
import com.championsita.menus.menuprincipal.GestorInputMenu;
import com.championsita.menus.menuprincipal.Menu;
import com.championsita.menus.menueleccion.Doble;
import com.championsita.menus.compartido.Assets;
import com.championsita.menus.compartido.OpcionDeGoles;
import com.championsita.menus.compartido.OpcionDeTiempo;
import com.championsita.menus.menuprincipal.RenderizadorDeMenu;
import com.championsita.partida.herramientas.Config;
import com.championsita.partida.ControladorDePartida;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Carga extends Menu {

    // Ahora soporta equipos especiales
    private Equipo equipoJ1;
    private Equipo equipoJ2;

    private SpriteBatch batch;
    private final String pielJugador1;
    private final String pielJugador2;
    private ArrayList<HabilidadesEspeciales> habilidades = new ArrayList<>();
    private final String modo; // "1v1", "practica", "especial"

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

    private Texture[] golesTex;
    private Texture[] tiempoTex;
    private Sprite cartelGoles;
    private Sprite cartelTiempo;

    private Texture flechaNormalTex;
    private Texture flechaHoverTex;
    private Sprite flechaIzq;
    private Sprite flechaDer;

    private Campo[] listaCampos;
    private int indiceCampo;

    private final int[] opcionesGoles = {1, 3, 5};
    private final int[] opcionesTiempo = {1, 2, 3};
    private int indiceGoles;
    private int indiceTiempo;

    private float[] golesXY;
    private float[] tiempoXY;

    private final Map<String, Texture> cacheCampos = new HashMap<>();

    private Sound hoverSound;
    private Color colorNormal;
    private Color colorHover;

    private static final float ALTURA_CONTROLES = 335f;
    private static final float CAMPOS_PANEL_Y   = 215f;
    private static final float CAMPOS_ALTURA    = 200f;
    private static final float FLECHA_OFFSET    = 5f;

    // Gestores
    private GestorInputMenu gestorMenu;
    private RenderizadorDeMenu renderizador;

    // Constructor normal (1v1 / practica)
    public Carga(Principal juego, String pielUno, String pielDos, String modo) {
        super(juego);
        this.pielJugador1 = pielUno;
        this.pielJugador2 = pielDos;
        this.modo = (modo == null ? "1v1" : modo);

        // En modos normales no hay equipo
        this.equipoJ1 = null;
        this.equipoJ2 = null;
    }


    // Constructor especial (2J especial con equipos)
    public Carga(Principal juego,
                 String skinJ1,
                 String skinJ2,
                 String modoDestino,
                 Equipo equipoJ1,
                 Equipo equipoJ2) {

        this(juego, skinJ1, skinJ2, modoDestino);
        this.equipoJ1 = equipoJ1;
        this.equipoJ2 = equipoJ2;
    }

    public Carga(Principal juego,
                 String skinJ1,
                 String skinJ2,
                 String modoDestino,
                 Equipo equipoJ1,
                 Equipo equipoJ2,
                 ArrayList<HabilidadesEspeciales> habilidades) {
        this(juego, skinJ1, skinJ2, modoDestino);

        this.equipoJ1 = equipoJ1;
        this.equipoJ2 = equipoJ2;

        this.habilidades.addAll(habilidades);
    }

    @Override
    public void show() {
        super.show();

        this.batch = this.juego.getBatch();
        this.colorNormal = new Color(super.atrasSprite.getColor());
        this.colorHover  = new Color(0, 1, 0, 1);

        this.fondoTex = Assets.tex("menuCreacion/menuDosJug.png");
        super.fondoSprite.setTexture(this.fondoTex);

        this.botonJugarTex = Assets.tex("menuDosJugadores/jugarBoton.png");
        super.siguienteSprite.setTexture(this.botonJugarTex);

        this.controlJugador1Tex = Assets.tex("menuCreacion/primerJugador.png");
        this.controlJugador2Tex = Assets.tex("menuCreacion/segundoJugador.png");
        this.controlJugador1 = new Sprite(this.controlJugador1Tex);
        this.controlJugador2 = new Sprite(this.controlJugador2Tex);
        colocarControles(ALTURA_CONTROLES);

        this.listaCampos = Campo.values();
        this.indiceCampo = 0;

        this.cartelCampoTex = Assets.tex("menuCreacion/campoCartel.png");
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

        this.flechaNormalTex = Assets.tex("menuDosJugadores/flechaNormal.png");
        this.flechaHoverTex  = Assets.tex("menuDosJugadores/flechaInvertida.png");
        this.flechaIzq = new Sprite(this.flechaNormalTex);
        this.flechaDer = new Sprite(this.flechaNormalTex);
        float yFlechas = campoSprite.getY() + campoSprite.getHeight() / 3.5f - 20f;
        this.flechaIzq.setPosition(this.campoSprite.getX() - this.flechaIzq.getWidth() - FLECHA_OFFSET, yFlechas);
        this.flechaDer.setPosition(this.campoSprite.getX() + campoSprite.getWidth() + FLECHA_OFFSET, yFlechas);
        this.flechaDer.setRotation(180f);

        this.golesTex  = new Texture[] {
                Assets.tex("menuCreacion/golesCartel1.png"),
                Assets.tex("menuCreacion/golesCartel3.png"),
                Assets.tex("menuCreacion/golesCartel5.png")
        };
        this.tiempoTex = new Texture[] {
                Assets.tex("menuCreacion/tiempoCartel1.png"),
                Assets.tex("menuCreacion/tiempoCartel2.png"),
                Assets.tex("menuCreacion/tiempoCartel3.png")
        };
        this.indiceGoles  = 0;
        this.indiceTiempo = 0;
        this.cartelGoles  = new Sprite(golesTex[indiceGoles]);
        this.cartelTiempo = new Sprite(tiempoTex[indiceTiempo]);

        int ubiX = 30, ubiY = 70;
        this.cartelGoles.setPosition(ubiX, ubiY);
        this.cartelTiempo.setPosition(Gdx.graphics.getWidth() - ubiX - this.cartelTiempo.getWidth(), ubiY);
        this.golesXY  = new float[]{ this.cartelGoles.getX(), this.cartelGoles.getY() };
        this.tiempoXY = new float[]{ this.cartelTiempo.getX(), this.cartelTiempo.getY() };

        this.hoverSound = super.sonido;
        super.inicializarSonido(2);

        Gdx.input.setInputProcessor(this);

        // Gestores
        gestorMenu = new GestorInputMenu(this);
        renderizador = new RenderizadorDeMenu(this);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        renderizador.renderFondo(delta);
        renderizador.cargarAtrasSiguiente();

        controlJugador1.draw(batch);
        controlJugador2.draw(batch);
        cartelCampo.draw(batch);
        campoSprite.draw(batch);
        flechaIzq.draw(batch);
        flechaDer.draw(batch);
        cartelGoles.draw(batch);
        cartelTiempo.draw(batch);

        batch.end();
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;

        boolean dentroG = hit(cartelGoles, x, y);
        cartelGoles.setColor(dentroG ? colorHover : colorNormal);

        boolean dentroT = hit(cartelTiempo, x, y);
        cartelTiempo.setColor(dentroT ? colorHover : colorNormal);

        boolean dentroAtras = hit(super.atrasSprite, x, y);
        gestorMenu.condicionColor(dentroAtras, super.atrasSprite);
        super.reproducirSonido(0, dentroAtras);

        boolean dentroJugar = hit(super.siguienteSprite, x, y);
        gestorMenu.condicionColor(dentroJugar, super.siguienteSprite);
        super.reproducirSonido(1, dentroJugar);

        updateFlechaHover(flechaIzq, x, y, true);
        updateFlechaHover(flechaDer, x, y, false);

        return dentroAtras || dentroJugar || dentroG || dentroT;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        y = Gdx.graphics.getHeight() - y;
        boolean clic = false;

        if (hit(cartelGoles, x, y)) {
            indiceGoles = (indiceGoles + 1) % opcionesGoles.length;
            cartelGoles.setTexture(golesTex[indiceGoles]);
            cartelGoles.setPosition(golesXY[0], golesXY[1]);
            clic = true;
        }

        if (hit(cartelTiempo, x, y)) {
            indiceTiempo = (indiceTiempo + 1) % opcionesTiempo.length;
            cartelTiempo.setTexture(tiempoTex[indiceTiempo]);
            cartelTiempo.setPosition(tiempoXY[0], tiempoXY[1]);
            clic = true;
        }

        if (hit(flechaIzq, x, y)) { cambiarCampo(-1); clic = true; }
        if (hit(flechaDer, x, y)) { cambiarCampo(+1); clic = true; }

        if (hit(super.atrasSprite, x, y)) {
            super.cambiarMenu(true, new Doble(super.juego, this.modo));
            return true;
        }

        // ============================
        // BOTÓN JUGAR — ARMAMOS CONFIG
        // ============================
        if (hit(super.siguienteSprite, x, y)) {

            Config.Builder builder =
                    new Config.Builder()
                            .agregarSkin(pielJugador1)
                            .agregarSkin(pielJugador2)
                            .campo(listaCampos[indiceCampo])
                            .goles(mapGoles(opcionesGoles[indiceGoles]))
                            .tiempo(mapTiempo(opcionesTiempo[indiceTiempo]))
                            .modo(this.modo);

            // SOLO si viene de Modo Especial: agrega equipos
            if(this.modo.equals("especial")){
                if (equipoJ1 != null && equipoJ2 != null) {
                    builder.agregarEquipo(equipoJ1);
                    builder.agregarEquipo(equipoJ2);
                }
                builder.agregarHabilidades(habilidades);
            }

            if(this.modo.equals("futbol")){

                builder.AltoMapa(Constantes.MUNDO_ALTO_MODO_FUTBOL);
                builder.AnchoMapa(Constantes.MUNDO_ANCHO_MODO_FUTBOL);
                builder.EscalaPelota(Constantes.ESCALA_PELOTA_FUTBOL);
            }

            if(this.modo.equals("futsal")){

                builder.EscalaPelota(Constantes.ESCALA_PELOTA_FUTSAL);
                builder.AltoMapa(Constantes.MUNDO_ALTO_MODO_FUTSAL);
                builder.AnchoMapa(Constantes.MUNDO_ANCHO_MODO_FUTSAL);


            }


            Config config = builder.build();
            super.juego.actualizarPantalla(new ControladorDePartida(config));
            return true;
        }

        return clic;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void cambiarCampo(int delta) {
        int len = listaCampos.length;
        indiceCampo = (indiceCampo + delta + len) % len;
        String nombre = listaCampos[indiceCampo].getNombre();
        Texture tex = getCampoTexture(nombre);
        campoSprite.setTexture(tex);
        campoSprite.setSize(cartelCampo.getWidth(), CAMPOS_ALTURA);
        campoSprite.setPosition(cartelCampo.getX(), cartelCampo.getY() - CAMPOS_ALTURA);
    }

    private Texture getCampoTexture(String nombre) {
        return cacheCampos.computeIfAbsent(nombre, n -> Assets.tex("campos/campo" + n + ".png"));
    }

    private boolean hit(Sprite s, int x, int y) {
        float sx = s.getX(), sy = s.getY(), sw = s.getWidth(), sh = s.getHeight();
        return x >= sx && x <= sx + sw && y >= sy && y <= sy + sh;
    }

    private void updateFlechaHover(Sprite flecha, int x, int y, boolean izquierda) {
        boolean dentro = hit(flecha, x, y);
        flecha.setTexture(dentro ? flechaHoverTex : flechaNormalTex);
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

    private OpcionDeGoles mapGoles(int valor) {
        return switch (valor) {
            case 1 -> OpcionDeGoles.UNO;
            case 3 -> OpcionDeGoles.TRES;
            case 5 -> OpcionDeGoles.CINCO;
            default -> OpcionDeGoles.UNO;
        };
    }

    private OpcionDeTiempo mapTiempo(int cod) {
        return switch (cod) {
            case 1 -> OpcionDeTiempo.CORTO;
            case 2 -> OpcionDeTiempo.MEDIO;
            case 3 -> OpcionDeTiempo.LARGO;
            default -> OpcionDeTiempo.CORTO;
        };
    }
}
