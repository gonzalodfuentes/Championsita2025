package com.championsita.name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.name.personajes.Normal;

public class Principal extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture canchaDeFutbol;

    private Personaje jugador1;
    private Personaje jugador2;
    private Pelota   pelota;

    private ManejadorInput controlador1;
    private ManejadorInput controlador2;
    private InputMultiplexer multiplexer;

    private FitViewport viewport;

    private final PersonajeConfig configJugador1 = new PersonajeConfig(
            "Jugador.png",
            "jugadorCorriendoDerecha.png",
            "jugadorCorriendoIzquierda.png",
            "jugadorCorriendoArriba.png",
            "jugadorCorriendoAbajo.png",
            "jugadorCorriendoArribaDerecha.png",
            "jugadorCorriendoArribaIzquierda.png",
            "jugadorCorriendoAbajoDerecha.png",
            "jugadorCorriendoAbajoIzquierda.png"
    );

    private final PersonajeConfig configJugador2 = new PersonajeConfig(
            "Jugador.png",
            "jugadorCorriendoDerecha.png",
            "jugadorCorriendoIzquierda.png",
            "jugadorCorriendoArriba.png",
            "jugadorCorriendoAbajo.png",
            "jugadorCorriendoArribaDerecha.png",
            "jugadorCorriendoArribaIzquierda.png",
            "jugadorCorriendoAbajoDerecha.png",
            "jugadorCorriendoAbajoIzquierda.png"
    );

    @Override
    public void create() {
        batch = new SpriteBatch();
        canchaDeFutbol = new Texture("CampoDeJuego.png");

        // Ajustá el ctor de Normal a tu firma real
        jugador1 = new Normal("J1", configJugador1, 0.003f, 1.0f, 1.8f, 100f);
        jugador2 = new Normal("J2", configJugador2, 0.003f, 1.0f, 1.8f, 100f);

        jugador1.setPosition(2.0f, 2.5f);
        jugador2.setPosition(6.0f, 2.5f);

        controlador1 = new ManejadorInput(jugador1, Keys.W, Keys.S, Keys.A, Keys.D, Keys.SPACE);
        controlador2 = new ManejadorInput(jugador2, Keys.I, Keys.K, Keys.J, Keys.L, Keys.O);

        multiplexer = new InputMultiplexer(controlador1, controlador2);
        Gdx.input.setInputProcessor(multiplexer);

        pelota = new Pelota(4f, 2.5f, 0.002f);
        viewport = new FitViewport(8, 5);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 1) Inputs y actores
        controlador1.actualizar(delta);
        controlador2.actualizar(delta);

        jugador1.update(delta);
        jugador2.update(delta);

        jugador1.limitarMovimiento(viewport.getWorldWidth(), viewport.getWorldHeight());
        jugador2.limitarMovimiento(viewport.getWorldWidth(), viewport.getWorldHeight());

        if (jugador1.getHitbox().overlaps(jugador2.getHitbox())) {
            resolverColision(jugador1, jugador2);
        }

        // 2) Contactos pelota: registrar por jugador (pueden ser ambos)
        registrarContactoConPelota(jugador1);
        registrarContactoConPelota(jugador2);

        // 3) Actualizar pelota UNA sola vez por frame
        pelota.actualizar(delta);

        // 4) Dibujar
        dibujar();
    }



    private void registrarContactoConPelota(Personaje p) {
        if (!p.getHitbox().overlaps(pelota.getHitbox())) return;

        // 1) Resolver penetración primero (sacar la pelota de adentro del jugador)
        resolverPenetracionPelotaConJugador(p);

        // 2) Dirección de empuje: mejor por centros (más robusto que “facing”)
        float bCx = pelota.getHitbox().x + pelota.getHitbox().width  / 2f;
        float bCy = pelota.getHitbox().y + pelota.getHitbox().height / 2f;
        float pCx = p.getHitbox().x + p.getHitbox().width  / 2f;
        float pCy = p.getHitbox().y + p.getHitbox().height / 2f;

        float dx = bCx - pCx;
        float dy = bCy - pCy;
        float len = (float)Math.sqrt(dx*dx + dy*dy);
        if (len != 0) { dx /= len; dy /= len; } else { dx = 0; dy = 1; } // fallback

        boolean disparo = p.estaEspacioPresionado();
        pelota.registrarContacto(dx, dy, disparo);
    }

    private void resolverPenetracionPelotaConJugador(Personaje p) {
        // AABB vs AABB: movemos SOLO la pelota por el vector de menor penetración
        com.badlogic.gdx.math.Rectangle A = p.getHitbox();
        com.badlogic.gdx.math.Rectangle B = pelota.getHitbox();

        float aLeft = A.x, aRight = A.x + A.width,  aBottom = A.y, aTop = A.y + A.height;
        float bLeft = B.x, bRight = B.x + B.width,  bBottom = B.y, bTop = B.y + B.height;

        float overlapX = Math.min(aRight, bRight) - Math.max(aLeft, bLeft);
        float overlapY = Math.min(aTop,   bTop)   - Math.max(aBottom, bBottom);
        if (overlapX <= 0 || overlapY <= 0) return; // no hay penetración

        float pCenterX = A.x + A.width  / 2f;
        float pCenterY = A.y + A.height / 2f;
        float bCenterX = B.x + B.width  / 2f;
        float bCenterY = B.y + B.height / 2f;

        float newBX = B.x, newBY = B.y;
        float EPS = 0.001f;

        if (overlapX < overlapY) {
            // empujar en X
            if (bCenterX >= pCenterX) newBX += overlapX + EPS; else newBX -= overlapX + EPS;
        } else {
            // empujar en Y
            if (bCenterY >= pCenterY) newBY += overlapY + EPS; else newBY -= overlapY + EPS;
        }

        pelota.setPosition(newBX, newBY); // esto ya actualiza la hitbox internamente
    }


    private void dibujar() {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        batch.draw(canchaDeFutbol, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        jugador1.render(batch);
        jugador2.render(batch);
        pelota.render(batch);

        // Si tu HUD por jugador está disponible:
        if (jugador1.hud != null) jugador1.hud.dibujarBarraStamina(batch, jugador1.getX(), jugador1.getY());
        if (jugador2.hud != null) jugador2.hud.dibujarBarraStamina(batch, jugador2.getX(), jugador2.getY());

        batch.end();
    }

    @Override
    public void resize(int width, int height) { viewport.update(width, height, true); }

    @Override
    public void dispose() {
        batch.dispose();
        canchaDeFutbol.dispose();
        // llamar dispose de personajes/pelota si corresponde
    }

    private void resolverColision(Personaje a, Personaje b) {
        float aCx = a.getX() + a.getHitbox().width  / 2f;
        float aCy = a.getY() + a.getHitbox().height / 2f;
        float bCx = b.getX() + b.getHitbox().width  / 2f;
        float bCy = b.getY() + b.getHitbox().height / 2f;

        float dx = aCx - bCx, dy = aCy - bCy;
        if (dx == 0 && dy == 0) dy = 0.01f;

        float len = (float) Math.sqrt(dx*dx + dy*dy);
        dx /= len; dy /= len;

        float overlap = 0.01f;
        a.setPosition(a.getX() + dx * overlap, a.getY() + dy * overlap);
        b.setPosition(b.getX() - dx * overlap, b.getY() - dy * overlap);
    }
}
