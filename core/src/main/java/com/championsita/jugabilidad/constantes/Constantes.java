package com.championsita.jugabilidad.constantes;

public final class Constantes {



	private Constantes() {}




	  // Mundo
	  public static float MUNDO_ANCHO = 8f;   //8f original
	  public static float MUNDO_ALTO  = 5f;   //5f original

	  public static float MUNDO_ANCHO_MODO_FUTBOL = 10f;   //8f original
	  public static float MUNDO_ALTO_MODO_FUTBOL  = 5f;   //5f original

		public static float MUNDO_ANCHO_MODO_FUTSAL = 6f;   //8f original
		public static float MUNDO_ALTO_MODO_FUTSAL  = 4f;   //5f original




	// Fuentes
	  public static final String fuente1 = "Minercraftory.ttf";
	  public static final String fuente2 = "light-arial.ttf";

	  // Cancha
	  public static final float altoCancha = 0.9f;
	  public static final float anchoCancha = 0.3f;

	  // Pelota
	  public static final float ESCALA_PELOTA_FUTSAL = 0.001f;
	  public static final float ESCALA_PELOTA_FUTBOL = 0.003f;
	  public static final float ESCALA_PELOTA   = 0.002f;
	  public static final float FRICCION_PELOTA = 0.95f;
	  public static final float FUERZA_DISPARO  = 2.5f;
	  public static final float FUERZA_EMPUJE   = 1.0f;
	  public static final float UMBRAL_VEL_CASI_CERO = 0.01f;
	  public static final float EMPUJON_CORTO = 0.35f;

	  public static final float radioSaque = 1.5f;

	  // Personaje
	  public static final float ESCALA_PERSONAJE = 0.003f;
	  public static final float VELOCIDAD_BASE   = 1.0f;
	  public static final float VELOCIDAD_SPRINT = 1.8f;
	  public static final float STAMINA_MAX  = 100f;
	  public static final float STAMINA_GASTO_SPRINT  = 5f;
	  public static final float STAMINA_RECUPERACION  = 10f;
	  public static final float STAMINA_BLOQUEO_UMBRAL = 0.9f;
	  public static final float STAMINA_BLOQUEO_TIEMPO = 2f;


	private static String pielJugador;

	public static void establecerPielJugador(String piel) {
		pielJugador = piel;
	}

	private static String base() {
		return "jugador/" + pielJugador + "/";
	}
	public static String sprJugadorQuieto() {
		return base() + "Jugador.png";
	}

	public static String sprJugadorCorriendoDerecha() {
		return base() + "jugadorCorriendoDerecha.png";
	}

	public static String sprJugadorCorriendoIzquierda() {
		return base() + "jugadorCorriendoIzquierda.png";
	}

	public static String sprJugadorCorriendoArriba() {
		return base() + "jugadorCorriendoArriba.png";
	}

	public static String sprJugadorCorriendoAbajo() {
		return base() + "jugadorCorriendoAbajo.png";
	}

	public static String sprJugadorCorriendoArribaDerecha() {
		return base() + "jugadorCorriendoArribaDerecha.png";
	}

	public static String sprJugadorCorriendoArribaIzquierda() {
		return base() + "jugadorCorriendoArribaIzquierda.png";
	}

	public static String sprJugadorCorriendoAbajoDerecha() {
		return base() + "jugadorCorriendoAbajoDerecha.png";
	}

	public static String sprJugadorCorriendoAbajoIzquierda() {
		return base() + "jugadorCorriendoAbajoIzquierda.png";
	}
}
