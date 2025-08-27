package com.championsita.name;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Map;
import java.util.HashMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Personaje {

    private float x, y, width, height, stateTime;
    private boolean estaMoviendo;
    private boolean espacioPresionado = false;
    private Direccion direccionActual = Direccion.ABAJO;
    private Rectangle hitbox;

    private Texture textureQuieto;
    private TextureRegion frameQuieto;
    private Map<Direccion, Animation<TextureRegion>> animaciones;

    private static final float VELOCIDAD = 1f;

    public Personaje(PersonajeConfig config, float escala) {
        // Textura quieto
        textureQuieto = new Texture(config.texturaQuieto);
        frameQuieto = new TextureRegion(textureQuieto);

        // Animaciones
        animaciones = new HashMap<>();
        animaciones.put(Direccion.DERECHA, crearAnimacion(config.sheetDerecha, 7, 1));
        animaciones.put(Direccion.IZQUIERDA, crearAnimacion(config.sheetIzquierda, 7, 1));
        animaciones.put(Direccion.ARRIBA, crearAnimacion(config.sheetArriba, 6, 1));
        animaciones.put(Direccion.ABAJO, crearAnimacion(config.sheetAbajo, 6, 1));
        animaciones.put(Direccion.ARRIBA_DERECHA, crearAnimacion(config.sheetArribaDerecha, 6, 1));
        animaciones.put(Direccion.ARRIBA_IZQUIERDA, crearAnimacion(config.sheetArribaIzquierda, 6, 1));
        animaciones.put(Direccion.ABAJO_DERECHA, crearAnimacion(config.sheetAbajoDerecha, 6, 1));
        animaciones.put(Direccion.ABAJO_IZQUIERDA, crearAnimacion(config.sheetAbajoIzquierda, 6, 1));

        // Tamaño basado en animación derecha
        TextureRegion frame = animaciones.get(Direccion.DERECHA).getKeyFrame(0);
        width = frame.getRegionWidth() * escala;
        height = frame.getRegionHeight() * escala;

        // Posición inicial
        x = 1;
        y = 1;
        stateTime = 0f;
        float hitboxWidth = width * 0.6f;  // 60% del ancho del sprite
        float hitboxHeight = height * 0.8f; // 80% de la altura del sprite
        float hitboxOffsetX = (width - hitboxWidth) / 2;
        float hitboxOffsetY = 0;
        hitbox = new Rectangle(x + hitboxOffsetX, y + hitboxOffsetY, hitboxWidth, hitboxHeight);

    }

    private Animation<TextureRegion> crearAnimacion(String path, int columnas, int filas) {
        Texture sheet = new Texture(path);
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth()/columnas, sheet.getHeight()/filas);
        TextureRegion[] frames = new TextureRegion[columnas*filas];
        int index = 0;
        for(int i=0;i<filas;i++){
            for(int j=0;j<columnas;j++){
                frames[index++] = tmp[i][j];
            }
        }
        return new Animation<>(0.1f, frames);
    }

    public void update(float delta) {
        if (estaMoviendo) stateTime += delta;
    }

    public void moverDesdeInput(boolean arriba, boolean abajo, boolean izquierda, boolean derecha, float delta){
        float move = VELOCIDAD * delta;
        estaMoviendo = arriba || abajo || izquierda || derecha;
        if(!estaMoviendo) return;

        // calcular dirección
        if(izquierda && arriba) direccionActual = Direccion.ARRIBA_IZQUIERDA;
        else if(derecha && arriba) direccionActual = Direccion.ARRIBA_DERECHA;
        else if(izquierda && abajo) direccionActual = Direccion.ABAJO_IZQUIERDA;
        else if(derecha && abajo) direccionActual = Direccion.ABAJO_DERECHA;
        else if(izquierda) direccionActual = Direccion.IZQUIERDA;
        else if(derecha) direccionActual = Direccion.DERECHA;
        else if(arriba) direccionActual = Direccion.ARRIBA;
        else if(abajo) direccionActual = Direccion.ABAJO;

        // mover
        float dx = 0, dy = 0;
        if(izquierda) dx -= 1;
        if(derecha) dx += 1;
        if(arriba) dy += 1;
        if(abajo) dy -= 1;

        float len = (float)Math.sqrt(dx*dx+dy*dy);
        if(len != 0){ dx/=len; dy/=len; }

        x += dx*move;
        y += dy*move;
        hitbox.setPosition(x,y);
    }

    public void render(SpriteBatch batch){
        TextureRegion frameActual = estaMoviendo ? animaciones.get(direccionActual).getKeyFrame(stateTime,true) : frameQuieto;
        batch.draw(frameActual,x,y,width,height);
    }

    public void limitarMovimiento(float worldWidth, float worldHeight){
        x = MathUtils.clamp(x,0,worldWidth-width);
        y = MathUtils.clamp(y,0,worldHeight-height);
    }

    public void setEspacioPresionado(boolean valor){ espacioPresionado = valor; }
    public boolean estaEspacioPresionado(){ return espacioPresionado; }
    public Direccion getDireccion(){ return direccionActual; }
    public Rectangle getHitbox(){ return hitbox; }
    public void dispose(){ textureQuieto.dispose(); animaciones.values().forEach(anim -> { for(TextureRegion tr: anim.getKeyFrames()) tr.getTexture().dispose(); }); }
    public float getX() {return this.x;}
    public float getY() {return this.y;}
	public void setPosition(float f, float g) {
		this.x = f;
		this.y = g;
	}
}
