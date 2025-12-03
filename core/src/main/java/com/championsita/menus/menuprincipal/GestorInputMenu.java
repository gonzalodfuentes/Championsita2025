package com.championsita.menus.menuprincipal;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class GestorInputMenu {
    Menu menu;
    public GestorInputMenu(Menu m){
        this.menu = m;
    }

    public boolean condicionFlechas(Sprite flecha, int x, int y) {
        float fx = flecha.getX();
        float fy = flecha.getY();
        float fAnc = flecha.getWidth();
        float fAlt = flecha.getHeight();

        boolean dentro = x >= fx && x <= fx + fAnc && y >= fy && y <= fy + fAlt;
        flecha.setTexture(dentro ? menu.flechaCursor : menu.flecha);
        return dentro;
    }

    public boolean condicionDentro(int x, int y, Sprite sprite) {
        return x >= sprite.getX() && x <= sprite.getX() + sprite.getWidth() &&
                y >= sprite.getY() && y <= sprite.getY() + sprite.getHeight();
    }

    public boolean condicionColor(boolean dentro, Sprite sprite) {
        sprite.setColor(dentro ? menu.colorAccion : menu.colorBoton);
        return dentro;
    }

}
