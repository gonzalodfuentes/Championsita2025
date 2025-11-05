package com.championsita.menus.menuprincipal;

public class GestorSonidoMenu {

    public Menu menu;

    public GestorSonidoMenu (Menu m){
        this.menu = m;
    }

    public void inicializarSonido(int cantBotones) {
        menu.cursorSonido = new boolean[cantBotones];
        for (int i = 0; i < menu.cursorSonido.length; i++) menu.cursorSonido[i] = false;
    }

    public void reproducirSonido(int i, boolean dentro) {
        if (dentro) {
            if (!menu.cursorSonido[i]) {
                menu.sonido.play(0.5f);
                menu.cursorSonido[i] = true;
            }
        } else {
            menu.cursorSonido[i] = false;
        }
    }
}
