package com.championsita.menus.compartido;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public final class Assets {

    private static final Map<String, Texture> textures = new HashMap<>();
    private static final Map<String, Sound> sounds = new HashMap<>();
    private static final Map<String, Music> musics = new HashMap<>();

    private Assets() {}

    public static Texture tex(String path) {
        Texture t = textures.get(path);
        if (t == null) {
            t = new Texture(Gdx.files.internal(path));
            textures.put(path, t);
        }
        return t;
    }

    public static Sound sfx(String path) {
        Sound s = sounds.get(path);
        if (s == null) {
            s = Gdx.audio.newSound(Gdx.files.internal(path));
            sounds.put(path, s);
        }
        return s;
    }

    public static Music music(String path) {
        Music m = musics.get(path);
        if (m == null) {
            m = Gdx.audio.newMusic(Gdx.files.internal(path));
            musics.put(path, m);
        }
        return m;
    }

    public static void disposeAll() {
        for (Texture t : textures.values()) t.dispose();
        for (Sound s : sounds.values()) s.dispose();
        for (Music m : musics.values()) m.dispose();
        textures.clear();
        sounds.clear();
        musics.clear();
    }
}
