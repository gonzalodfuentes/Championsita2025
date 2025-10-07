package com.championsita.sistemas;

import com.badlogic.gdx.math.Rectangle;
import com.championsita.constantes.Constantes;
import com.championsita.entidades.Arco;
import com.championsita.entidades.Cancha;
import com.championsita.entidades.Pelota;

public class SistemaPartido {

    float notadorEquipo1 = 0;
    float notadorEquipo2 = 0;

    public boolean checkGol(Pelota pelota, Arco arco) {
        Rectangle arcoRect = arco.getHitbox();
        Rectangle pelotaRect = pelota.getHitbox();

        // Verificar que toda la pelota esté dentro del arco
        boolean dentro =
                pelotaRect.x >= arcoRect.x &&                                         // borde izq pelota >= borde izq arco
                        pelotaRect.x + pelotaRect.width <= arcoRect.x + arcoRect.width &&     // borde der pelota <= borde der arco
                        pelotaRect.y >= arcoRect.y &&                                         // borde abajo pelota >= borde abajo arco
                        pelotaRect.y + pelotaRect.height <= arcoRect.y + arcoRect.height;     // borde arriba pelota <= borde arriba arco

        return dentro;
    }



    public void verificarSiHayGol(Pelota pelota, Cancha cancha){

        if(checkGol(pelota, cancha.getArcoDerecho())){ // equipo 2
            System.out.println("Gol equipo 2!!");
            notadorEquipo1++;
            reiniciarPelota(pelota);
        }

        else if(checkGol(pelota, cancha.getArcoIzquierdo())){
            System.out.println("Gol equipo 1!!");
            notadorEquipo2++;
            reiniciarPelota(pelota);
        }


    }

    public void reiniciarPelota(Pelota pelota){

        pelota.detenerPelota();
        pelota.setPosition(Constantes.MUNDO_ANCHO / 2f, Constantes.MUNDO_ALTO / 2f);

    }







}
