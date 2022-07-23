package com.edgarsilva.pixelgame.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class GameAssetsManager {


    public final AssetManager manager = new AssetManager();

    // Textures
    public static final String splashAtlas   = "atlas/splash.atlas";
    public static final String menuFrame1    = "raw/frame1.png";
    public static final String menuFrame2    = "raw/frame2.png";
    public static final String atlas         = "atlas/cavel.atlas";
    public static final String keyboardAtlas = "atlas/keyboard.atlas";

    // Sounds
    public static final String attack1 = "sounds/attack1.ogg";
    public static final String attack2 = "sounds/attack2.ogg";
    public static final String attack3 = "sounds/attack3.ogg";
    public static final String jump = "sounds/jump.ogg";
    public static final String doublejump = "sounds/doublejump.ogg";
    public static final String coinSound = "sounds/coin1.wav";

    // Music
    public final String playingSong = "";
    public static final String titleSong = "audio/music/Title-Screen.ogg";
    public static final String level1 = "audio/music/Level-1.ogg";
    public static final String ending = "audio/music/Ending.ogg";

    // Skin
    private static final String skin = "skin/bitpotion.json";

    //Keyboard
    public HashMap<String, Animation<TextureRegion>> keyboard = new HashMap<String, Animation<TextureRegion>>();

    // a small set of images used by the loading screen
    public void queueAddLoadingAssets(){
        manager.load(splashAtlas, TextureAtlas.class);
        manager.load(skin, Skin.class);
        manager.load(titleSong,Music.class);
        manager.load(menuFrame1, Texture.class);
        manager.load(menuFrame2, Texture.class);
        queueAddFonts();
    }

    public void queueAddTextures(){
        manager.load(atlas, TextureAtlas.class);
    }

    public void queueAddSounds(){
        manager.load(attack1, Sound.class);
        manager.load(attack2, Sound.class);
        manager.load(attack3, Sound.class);
        manager.load(jump, Sound.class);
        manager.load(doublejump, Sound.class);
        manager.load(coinSound, Sound.class);
        manager.load(keyboardAtlas, TextureAtlas.class);
    }

    public void queueAddMusic(){
        // manager.load(playingSong, Music.class);

        manager.load(level1,Music.class);
        manager.load(ending,Music.class);
    }

    public void queueAddFonts(){
    }

    public void queueAddParticleEffects(){
    }

    public void unloadAssets(String ... assets){
        for (String asset: assets) {
            manager.unload(asset);
        }
    }


    public Skin getSkin(){
        return manager.get(skin, Skin.class);
    }

    public void loadKeyboard() {
        TextureAtlas atlas = manager.get("atlas/keyboard.atlas");
        String[] letters = {
                "w",
                "a",
                "s",
                "d",
                "esc",
                "space",
                "k"
        };

        for (String letter : letters) {
            keyboard.put(
                    letter,
                    new Animation<TextureRegion>(
                            1/6f,
                            atlas.findRegion(letter),
                            atlas.findRegion("pressed"+letter)
                    )
            );
        }

    }
}
