package com.championsita.jugabilidad.sistemas;

import com.championsita.jugabilidad.modelo.Equipo;
import com.championsita.jugabilidad.modelo.Pelota;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.jugabilidad.modelo.SalidaPelota;
import com.championsita.partida.nucleo.ContextoModoDeJuego;

import static com.championsita.jugabilidad.modelo.SalidaPelota.*;

public class SistemaSaqueLateral {

    private final ContextoModoDeJuego ctx;

    private boolean saqueActivo = false;
    private Equipo equipoConSaque = null;

    // Guardamos las coordenadas para mantener la pelota fija
    private float xPelota, yPelota;

    public SistemaSaqueLateral(ContextoModoDeJuego ctx) {
        this.ctx = ctx;
    }

    public void procesarSaque(SalidaPelota salida) {

        // 1. Si el saque YA está activo, solo aseguramos que la pelota no se mueva y retornamos.
        // No queremos volver a calcular quién saca ni reiniciar lógica.
        if (saqueActivo) {
            mantenerPelotaEnPosicion();
            return;
        }

        // 2. Si la pelota está dentro y no hay saque activo, no hacemos nada.
        if (salida == DENTRO) {
            return;
        }

        // 3. Lógica de inicio de saque (solo sucede en el frame que la pelota sale)
        Personaje ultimo = ctx.pelota.getUltimoJugadorQueLaToco();
        if (ultimo == null) return;

        Equipo equipoUltimo = ultimo.getEquipo();
        Equipo equipoQueSaca = (equipoUltimo == Equipo.ROJO ? Equipo.AZUL : Equipo.ROJO);

        this.equipoConSaque = equipoQueSaca;
        this.saqueActivo = true;

        ctx.partido.setEquipoQueSaca(equipoQueSaca);

        System.out.println("Saque para " + equipoQueSaca);

        // Calculamos dónde va la pelota y la ponemos ahí
        calcularPosicionSaque(equipoQueSaca, salida);
        mantenerPelotaEnPosicion();
    }

    // Renombrado para claridad: Solo calcula coordenadas, no resetea jugadores
    private void calcularPosicionSaque(Equipo equipoQueSaca, SalidaPelota salida) {

        // ELIMINADO: ctx.pelota.resetJugadorTocando();  <-- ESTO CAUSABA EL ERROR

        if (salida == FUERA_IZQUIERDA) {
            if (equipoQueSaca == Equipo.ROJO) {
                xPelota = 1f;
                yPelota = (ctx.viewport.getWorldHeight() / 2) - 0.1f;
            } else {
                xPelota = 0.3f;
                yPelota = ctx.viewport.getWorldHeight() - 0.7f;
            }
        }
        else if (salida == FUERA_DERECHA) {
            if (equipoQueSaca == Equipo.AZUL) {
                xPelota = ctx.viewport.getWorldWidth() - 1f;
                yPelota = (ctx.viewport.getWorldHeight() / 2) - 0.1f;
            } else {
                xPelota = ctx.viewport.getWorldWidth() - 0.5f;
                yPelota = ctx.viewport.getWorldHeight() - 0.7f;
            }
        }
        else if (salida == FUERA_ARRIBA) {
            xPelota = ctx.pelota.getX();
            yPelota = ctx.viewport.getWorldHeight() - 0.7f;
        }
        else if (salida == FUERA_ABAJO) {
            xPelota = ctx.pelota.getX();
            yPelota = 0.7f;
        }
    }

    private void mantenerPelotaEnPosicion() {

        ctx.pelota.detenerPelota();
        ctx.pelota.setPosicion(xPelota, yPelota);
    }

    public void intentarTocarPelota(Pelota pelota) {
        if (!saqueActivo) return;

        Personaje JugadorTocando = pelota.getJugadorTocandoPelota();

        // Ahora esto ya no debería ser null si hubo colisión en ModoBase
        if (JugadorTocando == null) return;

        Equipo equipoQueToca = JugadorTocando.getEquipo();

        // Validamos que sea el equipo correcto
        if (equipoQueToca != equipoConSaque) {
            System.out.println("No puede tocar, es saque para " + equipoConSaque);
            // Forzamos la posición de nuevo para evitar que el otro equipo la robe empujándola
            mantenerPelotaEnPosicion();
            return;
        }

        // Si llega acá → el equipo correcto tocó la pelota
        System.out.println("Saque ejecutado por " + equipoQueToca);
        finalizarSaque();
    }

    public boolean isSaqueActivo() { return saqueActivo; }

    public Equipo getEquipoConSaque() { return equipoConSaque; }

    public void finalizarSaque() {
        this.saqueActivo = false;
        this.equipoConSaque = null;
        // Opcional: Avisar al partido que ya no hay saque activo
        ctx.partido.setEquipoQueSaca(null);
    }
}