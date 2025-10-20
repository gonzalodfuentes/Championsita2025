package com.championsita.jugabilidad.sistemas;

import com.championsita.jugabilidad.modelo.Arco;
import com.championsita.jugabilidad.modelo.Pelota;
import com.championsita.jugabilidad.modelo.Cancha;
import com.championsita.jugabilidad.constantes.Constantes;


public class SistemaPartido {

    private int notadorEquipo1;
    private int notadorEquipo2;
    private float centroY = Constantes.MUNDO_ALTO / 2f;
    private float centroX = Constantes.MUNDO_ANCHO / 2f;



    public boolean checkGol(Pelota pelota, Arco arco){
        return arco.getHitbox().overlaps(pelota.getHitbox());
    }

    public void verificarSiHayGol(Pelota pelota,Cancha cancha){

        if(checkGol(pelota, cancha.getArcoDerecho())){ // equipo 2
            System.out.println("Gol equipo 2!!");
            notadorEquipo2++;
            reiniciarPelota(centroX,centroY, pelota);


        }
        else if(checkGol(pelota, cancha.getArcoIzquierdo())){
            System.out.println("Gol equipo 1!!");
            notadorEquipo1++;
            reiniciarPelota(centroX,centroY, pelota);



        }

    }

    private void reiniciarPelota(float x, float y, Pelota pelota) {

        pelota.setVelocidad(0f,0f);
        pelota.setPosition(x,y);

    }

    public int getNotadorEquipo1() { return notadorEquipo1; }
    public int getNotadorEquipo2() { return notadorEquipo2; }


}