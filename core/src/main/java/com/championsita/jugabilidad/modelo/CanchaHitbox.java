package com.championsita.jugabilidad.modelo;

import com.badlogic.gdx.math.Rectangle;
import com.championsita.jugabilidad.constantes.Constantes;

public class CanchaHitbox {

        public Rectangle lineaIzquierda;
        public Rectangle lineaDerecha;
        public Rectangle lineaSuperior;
        public Rectangle lineaInferior;

        public CanchaHitbox(float mundo_ancho, float mundo_alto) {
            lineaIzquierda = new Rectangle(0.2f, 0, 0, 72f); // ancho mínimo
            lineaDerecha   = new Rectangle(mundo_ancho - 0.2f, 0, 0f, 72f);
            lineaSuperior  = new Rectangle(0, mundo_alto - 0.18f, 1280, 0f);
            lineaInferior  = new Rectangle(0, 0.1f, 1280, 0f);

        }










}
