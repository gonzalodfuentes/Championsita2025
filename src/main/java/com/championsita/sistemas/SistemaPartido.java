package com.championsita.sistemas;

import com.championsita.modelo.Arco;
import com.championsita.modelo.Cancha;
import com.championsita.modelo.Pelota;

public class SistemaPartido {

    float notadorEquipo1;
    float notadorEquipo2;

    public boolean checkGol(Pelota pelota, Arco arco){
        return arco.getHitbox().overlaps(pelota.getHitbox());
    }

    public void verificarSiHayGol(Pelota pelota, Cancha cancha){

        if(checkGol(pelota, cancha.getArcoDerecho())){ // equipo 2
            System.out.println("Gol equipo 2!!");
        }
        else if(checkGol(pelota, cancha.getArcoIzquierdo())){
            System.out.println("Gol equipo 1!!");
        }

    }








}
