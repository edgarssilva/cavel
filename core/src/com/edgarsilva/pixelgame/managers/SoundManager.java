package com.edgarsilva.pixelgame.managers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.edgarsilva.pixelgame.preferences.SoundPreferences;

public class SoundManager implements Disposable {

    private static SoundPreferences preferences;
    private static GameAssetsManager assets;

    private static Music music;
    private static Sound sfx;

    private static boolean playing = true;

    public SoundManager(SoundPreferences preferences, GameAssetsManager assets) {
        SoundManager.preferences = preferences;
        SoundManager.assets = assets;
    }

    public static void setMusic(String file,boolean looping){
        if (music!=null)music.dispose();
        music = assets.manager.get(file,Music.class);
        music.setLooping(looping);
        update();
    }

    public static void update(){
        music.setVolume(preferences.getMusicVolume());
        if (!music.isPlaying() && preferences.isMusicEnabled() && playing) music.play();
        if (music.isPlaying() && !playing) music.pause();
    }

    @Override
    public void dispose(){
        if (music!=null)music.dispose();
    }
}
