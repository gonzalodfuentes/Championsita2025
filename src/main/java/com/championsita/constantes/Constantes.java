package com.championsita.constantes;

public final class Constantes {
	  private Constantes() {}

	  // Mundo
	  public static final float MUNDO_ANCHO = 8f;
	  public static final float MUNDO_ALTO  = 5f;

	  // Pelota
	  public static final float ESCALA_PELOTA   = 0.002f;
	  public static final float FRICCION_PELOTA = 0.95f;
	  public static final float FUERZA_DISPARO  = 2.5f;
	  public static final float FUERZA_EMPUJE   = 1.0f;
	  public static final float UMBRAL_VEL_CASI_CERO = 0.01f;
	  public static final float EMPUJON_CORTO = 0.35f;

	  // Personaje
	  public static final float ESCALA_PERSONAJE = 0.003f;
	  public static final float VELOCIDAD_BASE   = 1.0f;
	  public static final float VELOCIDAD_SPRINT = 1.8f;
	  public static final float STAMINA_MAX  = 100f;
	  public static final float STAMINA_GASTO_SPRINT  = 5f;
	  public static final float STAMINA_RECUPERACION  = 10f;
	  public static final float STAMINA_BLOQUEO_UMBRAL = 0.9f;
	  public static final float STAMINA_BLOQUEO_TIEMPO = 2f;

	  // Sprites (si querés centralizar rutas)
	  public static final String SPR_JUGADOR_QUIETO = "Jugador.png";
	  public static final String SPR_JUGADOR_CORRIENDO_DERECHA = "jugadorCorriendoDerecha.png";
	  public static final String SPR_JUGADOR_CORRIENDO_IZQUIERDA = "jugadorCorriendoIzquierda.png";
	  public static final String SPR_JUGADOR_CORRIENDO_ARRIBA = "jugadorCorriendoArriba.png";
	  public static final String SPR_JUGADOR_CORRIENDO_ABAJO = "jugadorCorriendoAbajo.png";
	  public static final String SPR_JUGADOR_CORRIENDO_ARRIBA_DERECHA = "jugadorCorriendoArribaDerecha.png";
	  public static final String SPR_JUGADOR_CORRIENDO_ARRIBA_IZQUIERDA = "jugadorCorriendoArribaIzquierda.png";
	  public static final String SPR_JUGADOR_CORRIENDO_ABAJO_DERECHA = "jugadorCorriendoAbajoDerecha.png";
	  public static final String SPR_JUGADOR_CORRIENDO_ABAJO_IZQUIERDA = "jugadorCorriendoAbajoIzquierda.png";
}
