package com.championsita.entidades;

import com.championsita.constantes.Constantes;

public class ConfiguracionPersonaje {

    final String texturaQuieto;
    final String sheetDerecha;
    final String sheetIzquierda;
    final String sheetArriba;
    final String sheetAbajo;
    final String sheetArribaDerecha;
    final String sheetArribaIzquierda;
    final String sheetAbajoDerecha;
    final String sheetAbajoIzquierda;

    public ConfiguracionPersonaje(
            String texturaQuieto,
            String sheetDerecha,
            String sheetIzquierda,
            String sheetArriba,
            String sheetAbajo,
            String sheetArribaDerecha,
            String sheetArribaIzquierda,
            String sheetAbajoDerecha,
            String sheetAbajoIzquierda
    ) {
        this.texturaQuieto = texturaQuieto;
        this.sheetDerecha = sheetDerecha;
        this.sheetIzquierda = sheetIzquierda;
        this.sheetArriba = sheetArriba;
        this.sheetAbajo = sheetAbajo;
        this.sheetArribaDerecha = sheetArribaDerecha;
        this.sheetArribaIzquierda = sheetArribaIzquierda;
        this.sheetAbajoDerecha = sheetAbajoDerecha;
        this.sheetAbajoIzquierda = sheetAbajoIzquierda;
    }

    /** Config por defecto tomando rutas de Constantes. */
    public static ConfiguracionPersonaje porDefecto() {
        return new ConfiguracionPersonaje(
            Constantes.SPR_JUGADOR_QUIETO,
            Constantes.SPR_JUGADOR_CORRIENDO_DERECHA,
            Constantes.SPR_JUGADOR_CORRIENDO_IZQUIERDA,
            Constantes.SPR_JUGADOR_CORRIENDO_ARRIBA,
            Constantes.SPR_JUGADOR_CORRIENDO_ABAJO,
            Constantes.SPR_JUGADOR_CORRIENDO_ARRIBA_DERECHA,
            Constantes.SPR_JUGADOR_CORRIENDO_ARRIBA_IZQUIERDA,
            Constantes.SPR_JUGADOR_CORRIENDO_ABAJO_DERECHA,
            Constantes.SPR_JUGADOR_CORRIENDO_ABAJO_IZQUIERDA
        );
    }

    public String getTexturaQuieto() { return texturaQuieto; }
    public String getSheetDerecha() { return sheetDerecha; }
    public String getSheetIzquierda() { return sheetIzquierda; }
    public String getSheetArriba() { return sheetArriba; }
    public String getSheetAbajo() { return sheetAbajo; }
    public String getSheetArribaDerecha() { return sheetArribaDerecha; }
    public String getSheetArribaIzquierda() { return sheetArribaIzquierda; }
    public String getSheetAbajoDerecha() { return sheetAbajoDerecha; }
    public String getSheetAbajoIzquierda() { return sheetAbajoIzquierda; }
}
