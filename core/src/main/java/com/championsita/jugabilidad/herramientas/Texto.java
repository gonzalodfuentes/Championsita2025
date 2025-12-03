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

    public Texto(String rutaFuente, int dimension, Color color, float anchoBorde, Color colorBorde) {
        // Inicializa el generador con el archivo de la fuente
        generator = new FreeTypeFontGenerator(Gdx.files.internal(rutaFuente));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = dimension;
        parameter.color = color;

        // Parámetros para el Outline
        parameter.borderWidth = anchoBorde;
        parameter.borderColor = colorBorde;
        parameter.borderStraight = true;

        // Agregar padding para evitar que el borde se recorte
        int padding = (int)Math.ceil(anchoBorde);
        parameter.padTop = padding;
        parameter.padBottom = padding;
        parameter.padLeft = padding;
        parameter.padRight = padding;

        // Generar la fuente y el layout
        fuente = generator.generateFont(parameter);
        layout = new GlyphLayout();
    }

    public Texto(String rutaFuente, int dimension, Color color) {
        this(rutaFuente, dimension, color, 0f, Color.BLACK);
    }

    public void dibujar(SpriteBatch batch) {
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

    public void dispose() {
        if (fuente != null) {
            fuente.dispose();
        }
        if (generator != null) {
            generator.dispose();
        }
    }
}