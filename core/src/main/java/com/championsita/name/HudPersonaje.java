package com.championsita.name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HudPersonaje {

    private Personaje personaje;

    private Texture texturaBarra;

    private NinePatch barraStamina;

    private OrthographicCamera camaraHUD;


    float anchoBarra = 1000;
    float altoBarra = 100;

   float porcentaje;
    float stamina;
    float staminaMax;



    public HudPersonaje(Personaje personaje) {
        this.personaje = personaje;

        texturaBarra = new Texture("interiorBarraStamina.png");
        camaraHUD = new OrthographicCamera();
        camaraHUD.setToOrtho(false, 1280, 720);

    }

    public void dibujarBarraStamina(SpriteBatch batch) {

        camaraHUD.update();
        batch.setProjectionMatrix(camaraHUD.combined);

        // Calcular porcentaje de stamina
        float porcentaje = personaje.getStamina() / personaje.getStaminaMax();

        // Dibujar barra proporcional a la stamina
        batch.draw(texturaBarra, 20, 650, anchoBarra * porcentaje, altoBarra);
        System.out.println("Deberia funcionar");





    }



}
