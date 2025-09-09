package com.championsita.name.menus;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.name.Principal;

public class Carga extends Menu {

    private boolean modoDosJugadores;
    private Texture controlUno;
    private Texture controlDos;
    private Sprite unoSprite;
    private Sprite dosSprite;
    private Texture cartelCampo;
    private Sprite cartelCampoSprite;
    private Texture campo;
    private Sprite campoSprite;
    private int flechasUsadas;
    private int numCampo;
    private Sprite goles;
    private Sprite tiempo;
    private int numGoles, numTiempo;
    private float[] golesXY, tiempoXY;
    private Campo[] campos;

    public Carga(Principal juego, boolean modoDosJugadores) {
        super(juego);
        this.modoDosJugadores = modoDosJugadores;
    }

    @Override
    public void show() {
        super.show();
        super.crearFlechas(8);

        super.siguiente = new Texture("menuDosJugadores/jugarBoton.png");
        super.siguienteSprite.setTexture(super.siguiente);

        this.controlUno = new Texture("menuCreacion/primerJugador.png");
        this.controlDos = new Texture("menuCreacion/segundoJugador.png");
        if(this.modoDosJugadores) {
            super.fondoSprite.setTexture(new Texture("menuCreacion/menuDosJug.png"));
            this.dosSprite = new Sprite(this.controlDos);
        }
        this.unoSprite = new Sprite(this.controlUno);

        float y = 335;

        if(this.modoDosJugadores) {
            controlesDosJugadores(y);
            super.flechas[0].setPosition(this.unoSprite.getX() - super.flechas[0].getWidth() - 5, y - 20);
            super.flechas[1].setPosition(this.unoSprite.getX() + unoSprite.getWidth() + 5, y - 20);
            super.flechas[2].setPosition(this.dosSprite.getX() - super.flechas[2].getWidth() - 5, y - 20);
            super.flechas[3].setPosition(this.dosSprite.getX() + dosSprite.getWidth() + 5, y - 20);
            this.flechasUsadas = 4;
        }
        else {
            controlUnJugador(0, y);
            super.flechas[0].setPosition(this.unoSprite.getX() - super.flechas[0].getWidth() - 5, y - 20);
            super.flechas[1].setPosition(this.unoSprite.getX() + unoSprite.getWidth() + 5, y - 20);
            this.flechasUsadas = 2;
        }

        this.campos = Campo.values();
        this.numCampo = 0;
        this.cartelCampo = new Texture("menuCreacion/campoCartel.png");
        this.cartelCampoSprite = new Sprite(this.cartelCampo);
        this.cartelCampoSprite.setPosition(Gdx.graphics.getWidth() / 2 - this.cartelCampo.getWidth() / 2, 215);
        this.campo = new Texture("campos/campo" + this.campos[this.numCampo].getNombre() + ".png");
        this.campoSprite = new Sprite(this.campo);
        float anchoCampo = this.cartelCampoSprite.getWidth(), altoCampo = 200;
        this.campoSprite.setSize(anchoCampo, altoCampo);
        this.campoSprite.setPosition(this.cartelCampoSprite.getX(),
                                        this.cartelCampoSprite.getY() - altoCampo);
        y = campoSprite.getY() + campoSprite.getHeight() / 3.5f;
        super.flechas[this.flechasUsadas].setPosition(this.campoSprite.getX() - super.flechas[this.flechasUsadas].getWidth() - 5, y - 20);
        super.flechas[this.flechasUsadas + 1].setPosition(this.campoSprite.getX() + campoSprite.getWidth() + 5, y - 20);
        this.flechasUsadas += 2;

        int ubiX = 30, ubiY = 70;
        this.goles = new Sprite(new Texture("menuCreacion/golesCartel1.png"));
        this.goles.setPosition(ubiX, ubiY);
        this.tiempo = new Sprite(new Texture("menuCreacion/tiempoCartel1.png"));
        ubiX += this.tiempo.getWidth();
        this.tiempo.setPosition(Gdx.graphics.getWidth() - ubiX, ubiY);
        this.numGoles = 1;
        this.numTiempo = 1;
        this.golesXY = new float[] {
                this.goles.getX(),
                this.goles.getY()
        };
        this.tiempoXY = new float[] {
                this.tiempo.getX(),
                this.tiempo.getY()
        };

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
        if(this.modoDosJugadores) {
            this.dosSprite.draw(super.batch);
        }
        for(int i = 0; i < this.flechasUsadas; i++) {
            super.flechas[i].draw(super.batch);
        }
        this.cartelCampoSprite.draw(super.batch);
        this.campoSprite.draw(super.batch);
        super.flechas[4].draw(super.batch);
        super.flechas[5].draw(super.batch);
        super.cargarAtrasSiguiente();
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
        int i = 0;

        do {
            paso = condicionFlechas(super.flechas[i], x, y);
            i++;
        } while (i < this.flechasUsadas && !paso);

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
        boolean dentro = x >= xS && x <= xS + anS && y >= yS && y <= yS + alS;

        return dentro;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int mouse) {
        boolean clickeado = false;
        int i = 0;
        y = Gdx.graphics.getHeight() - y;

        clickeado = definirLimitesTiempoGoles(this.goles, x, y);
        if(clickeado) {
            this.goles.setSize(this.goles.getTexture().getWidth() - 12, this.goles.getTexture().getHeight() - 6);
            this.goles.setPosition(this.golesXY[0] + 6, this.golesXY[1] + 3);
        }

        clickeado = definirLimitesTiempoGoles(this.tiempo, x, y);
        if(clickeado) {
            this.tiempo.setSize(this.tiempo.getTexture().getWidth() - 12, this.tiempo.getTexture().getHeight() - 6);
            this.tiempo.setPosition(this.tiempoXY[0] + 6, this.tiempoXY[1] + 3);
        }

        return clickeado;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int mouse) {
        boolean clickeado = false;
        int i = 0;
        y = Gdx.graphics.getHeight() - y;

        do {
            boolean dentro = super.condicionFlechas(super.flechas[i], x, y);
            if(dentro) {
                if((this.modoDosJugadores && i < 4) || (!this.modoDosJugadores && i < 2)) {
                    elegirControles(i);
                }
                else {
                    elegirCampo(i);
                }
                clickeado = true;
            }
            else {
                clickeado = false;
            }

            i++;
        }
        while (i < this.flechasUsadas && !clickeado);

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

        clickeado = condicionDentro(x, y, this.atrasSprite);
        super.cambiarMenu(clickeado, new Doble(super.juego));

        /* logica iniciar juego */

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

    private void controlUnJugador(float acomodarX, float y) {
        float x = Gdx.graphics.getWidth() / 2 - acomodarX - this.controlUno.getWidth() / 2;
        this.unoSprite.setPosition(x, y);
    }

    private void controlesDosJugadores(float y) {
        float xDos = Gdx.graphics.getWidth() / 2 + 200 - this.controlDos.getWidth() / 2;
        controlUnJugador(184, y);
        this.dosSprite.setPosition(xDos, y);
    }

    private void elegirControles(int i) {
        if(i < 2) {
            if(this.unoSprite.getTexture() == this.controlUno) {
                this.unoSprite.setTexture(this.controlDos);
            }
            else {
                this.unoSprite.setTexture(this.controlUno);
            }
        }

        if(this.modoDosJugadores && i > 1) {
            if(this.dosSprite.getTexture() == this.controlUno) {
                this.dosSprite.setTexture(this.controlDos);
            }
            else {
                this.dosSprite.setTexture(this.controlUno);
            }
        }
    }

    private void elegirCampo(int i) {
        if(i % 2 == 0) {
            this.numCampo = (this.numCampo - 1 + this.campos.length) % this.campos.length;
        }
        else {
            this.numCampo = (this.numCampo + 1) % this.campos.length;
        }

        Texture nueva = new Texture("campos/campo" + this.campos[this.numCampo].getNombre() + ".png");
        this.campoSprite.setTexture(nueva);
    }
}









