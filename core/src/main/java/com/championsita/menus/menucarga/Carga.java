package com.championsita.menus.menucarga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.Principal;
import com.championsita.jugabilidad.Jugabilidad;
import com.championsita.menus.Menu;
import com.championsita.menus.menueleccion.Doble;

public class Carga extends Menu {

    private Texture controlUno;
    private Texture controlDos;
    private Sprite unoSprite;
    private Sprite dosSprite;
    private Texture cartelCampo;
    private Sprite cartelCampoSprite;
    private Texture campo;
    private Sprite campoSprite;
    private int numCampo;
    private Sprite goles;
    private Sprite tiempo;
    private int numGoles, numTiempo;
    private float[] golesXY, tiempoXY;
    private Campo[] campos;
    private String pielUno, pielDos;

    public Carga(Principal juego, String pielUno, String pielDos) {
        super(juego);
        this.pielUno = pielUno;
        this.pielDos = pielDos;
    }

    @Override
    public void show() {
        super.show();

        super.siguiente = new Texture("menuDosJugadores/jugarBoton.png");
        super.siguienteSprite.setTexture(super.siguiente);

        this.controlUno = new Texture("menuCreacion/primerJugador.png");
        this.controlDos = new Texture("menuCreacion/segundoJugador.png");
        super.fondoSprite.setTexture(new Texture("menuCreacion/menuDosJug.png"));
        this.unoSprite = new Sprite(this.controlUno);
        this.dosSprite = new Sprite(this.controlDos);

        float y = 335;
        colocarControles(y);

        this.campos = Campo.values();
        this.numCampo = 0;
        this.cartelCampo = new Texture("menuCreacion/campoCartel.png");
        this.cartelCampoSprite = new Sprite(this.cartelCampo);
        this.cartelCampoSprite.setPosition(Gdx.graphics.getWidth() / 2 - this.cartelCampo.getWidth() / 2, 215);
        this.campo = new Texture("campos/campo" + this.campos[this.numCampo].getNombre() + ".png");
        this.campoSprite = new Sprite(this.campo);
        float anchoCampo = this.cartelCampoSprite.getWidth(), altoCampo = 200;
        this.campoSprite.setSize(anchoCampo, altoCampo);
        this.campoSprite.setPosition(this.cartelCampoSprite.getX(), this.cartelCampoSprite.getY() - altoCampo);

        int ubiX = 30, ubiY = 70;
        this.goles = new Sprite(new Texture("menuCreacion/golesCartel1.png"));
        this.goles.setPosition(ubiX, ubiY);
        this.tiempo = new Sprite(new Texture("menuCreacion/tiempoCartel1.png"));
        ubiX += this.tiempo.getWidth();
        this.tiempo.setPosition(Gdx.graphics.getWidth() - ubiX, ubiY);
        this.numGoles = 1;
        this.numTiempo = 1;
        this.golesXY = new float[]{ this.goles.getX(), this.goles.getY() };
        this.tiempoXY = new float[]{ this.tiempo.getX(), this.tiempo.getY() };

        // Inicializar flechas de campo (solo estas, no las de controles)
        super.crearFlechas(2); // dos flechas para cambiar campo
        float yFlechas = campoSprite.getY() + campoSprite.getHeight() / 3.5f;
        super.flechas[0].setPosition(this.campoSprite.getX() - super.flechas[0].getWidth() - 5, yFlechas - 20);
        super.flechas[1].setPosition(this.campoSprite.getX() + campoSprite.getWidth() + 5, yFlechas - 20);

        Gdx.input.setInputProcessor(this);

        int cantBotonesHabiles = 2;
        super.inicializarSonido(cantBotonesHabiles);
    }

    @Override
    public void render(float delta) {
        super.batch.begin();
        super.render(delta);
        super.cargarAtrasSiguiente();

        this.unoSprite.draw(super.batch);
        this.dosSprite.draw(super.batch);
        this.cartelCampoSprite.draw(super.batch);
        this.campoSprite.draw(super.batch);

        // Dibujar solo flechas de campo
        super.flechas[0].draw(super.batch);
        super.flechas[1].draw(super.batch);

        this.goles.draw(super.batch);
        this.tiempo.draw(super.batch);

        super.batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        boolean paso = false;

        paso = definirLimitesTiempoGoles(this.goles, x, y);
        this.goles.setColor(paso ? super.colorAccion : super.colorBoton);

        paso = definirLimitesTiempoGoles(this.tiempo, x, y);
        this.tiempo.setColor(paso ? super.colorAccion : super.colorBoton);

        paso = super.condicionDentro(x, y, this.atrasSprite);
        super.condicionColor(paso, this.atrasSprite);
        super.reproducirSonido(0, paso);

        paso = super.condicionDentro(x, y, this.siguienteSprite);
        super.condicionColor(paso, this.siguienteSprite);
        super.reproducirSonido(1, paso);

        return paso;
    }

    private boolean definirLimitesTiempoGoles(Sprite sprite, int x, int y) {
        float xS = sprite.getX();
        float yS = sprite.getY();
        float anS = sprite.getWidth();
        float alS = sprite.getHeight();
        return x >= xS && x <= xS + anS && y >= yS && y <= yS + alS;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int mouse) {
        y = Gdx.graphics.getHeight() - y;
        boolean clickeado = false;

        // Goles
        clickeado = definirLimitesTiempoGoles(this.goles, x, y);
        if(clickeado) {
            int num = cambiarCantiadGolesTiempo(true, this.numGoles);
            if(this.numGoles != num) {
                this.numGoles = num;
                this.goles.setTexture(new Texture("menuCreacion/golesCartel" + this.numGoles + ".png"));
            }
        }
        this.goles.setSize(this.goles.getTexture().getWidth(), this.goles.getTexture().getHeight());
        this.goles.setPosition(this.golesXY[0], this.golesXY[1]);

        // Tiempo
        clickeado = definirLimitesTiempoGoles(this.tiempo, x, y);
        if(clickeado) {
            int num = cambiarCantiadGolesTiempo(false, this.numTiempo);
            if(this.numTiempo != num) {
                this.numTiempo = num;
                this.tiempo.setTexture(new Texture("menuCreacion/tiempoCartel" + this.numTiempo + ".png"));
            }
        }
        this.tiempo.setSize(this.tiempo.getTexture().getWidth(), this.tiempo.getTexture().getHeight());
        this.tiempo.setPosition(this.tiempoXY[0], this.tiempoXY[1]);

        // Flechas de campo
        if(super.condicionFlechas(super.flechas[0], x, y)) {
            numCampo = (numCampo - 1 + campos.length) % campos.length;
            campoSprite.setTexture(new Texture("campos/campo" + campos[numCampo].getNombre() + ".png"));
        }
        if(super.condicionFlechas(super.flechas[1], x, y)) {
            numCampo = (numCampo + 1) % campos.length;
            campoSprite.setTexture(new Texture("campos/campo" + campos[numCampo].getNombre() + ".png"));
        }

        // Botones atras y siguiente
        clickeado = super.condicionDentro(x, y, this.atrasSprite);
        super.cambiarMenu(clickeado, new Doble(super.juego));

        clickeado = super.condicionDentro(x, y, this.siguienteSprite);
        if(clickeado) {
            super.juego.actualizarPantalla(new Jugabilidad(this.campos[this.numCampo].getNombre(), this.pielUno, this.pielDos));
        }

        return clickeado;
    }

    private int cambiarCantiadGolesTiempo(boolean goles, int num) {
        switch(num) {
            case 1: num = goles ? 3 : 2; break;
            case 2: num = 3; break;
            case 3: num = goles ? 5 : 1; break;
            case 5: num = 1;
        }
        return num;
    }

    private void colocarControles(float y) {
        float xUno = Gdx.graphics.getWidth() / 2 - 184 - this.controlUno.getWidth() / 2;
        float xDos = Gdx.graphics.getWidth() / 2 + 200 - this.controlDos.getWidth() / 2;
        this.unoSprite.setPosition(xUno, y);
        this.dosSprite.setPosition(xDos, y);
    }
}
