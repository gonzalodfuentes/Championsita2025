package com.championsita.jugabilidad.modelo;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HudPersonaje {

    private Personaje personaje;

    private Texture texturaBarra;

    private NinePatch barraStamina;

    private OrthographicCamera camaraHUD;


    float anchoBarra = 0.5f;
    float altoBarra = 0.5f;

   float porcentaje;
    float stamina;
    float staminaMax;



    public HudPersonaje(Personaje personaje) {
        this.personaje = personaje;

        texturaBarra = new Texture("interiorBarraStamina.png");
        camaraHUD = new OrthographicCamera();
        camaraHUD.setToOrtho(false, 1280, 720);

    }

    public void dibujarBarraStamina(SpriteBatch batch, float xPersonaje, float yPersonaje) {

        // Calcular porcentaje de stamina
        float porcentaje = personaje.getStaminaActual() / personaje.getStaminaMaxima();

        // Centrar la barra horizontalmente sobre el jugador
        float posX = (personaje.getAncho() - anchoBarra) / 2f;

        float posY = personaje.getAlto() -0.3f; // ajustar según escala del personaje

        // Dibujar barra proporcional a la stamina
        batch.draw(texturaBarra, xPersonaje + posX, yPersonaje + posY, anchoBarra * porcentaje, altoBarra);
    }



}
