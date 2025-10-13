package com.championsita.jugabilidad.herramientas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

public class Texto {
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    BitmapFont fuente;
    private int x=0, y=0;
    private String texto = "";
    GlyphLayout layout;

    public Texto(String rutaFuente, int dimension,Color color) {
        generator = new FreeTypeFontGenerator(Gdx.files.internal(rutaFuente));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = dimension;
        parameter.color = color;

        fuente = generator.generateFont(parameter);
        layout = new GlyphLayout();
    }

    public void dibujar(SpriteBatch batch) {
        System.out.println("DIBUJA");
        fuente.draw(batch, texto, x, y);
    }
    public void setColor(Color color){
        fuente.setColor(color);
    }
    public void setPosition(int x, int y){
        this.x=x;
        this.y=y;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
        layout.setText(fuente, texto);
    }
    public float getAncho(){
        return layout.width;
    }
    public float getAlto(){
        return layout.height;
    }
    public Vector2 getDimension(){
        return new Vector2(layout.width, layout.height);
    }
}