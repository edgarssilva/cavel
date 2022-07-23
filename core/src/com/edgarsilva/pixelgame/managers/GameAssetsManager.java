package com.edgarsilva.pixelgame.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetsManager {


    public final AssetManager manager = new AssetManager();
    public BitmapFont font;

    //Loading Screen
    public static final String loadingBackground = "raw/loading.png";

    // Textures
    public static final String hudImage = "hud/ui_upscaled.png";
    public static final String playerAtlas = "entities/sprites/Player.atlas";
    public static final String skeletonAtlas = "entities/sprites/Skeleton.atlas";
    public static final String slimeAtlas = "entities/sprites/slime.atlas";
    public static final String gameOverImage = "raw/textures/gameover.jpg";
    public static final String witchTexture = "entities/sprites/witch.png";


    // Sounds
    public static final String attack1 = "entities/sounds/attack1.ogg";
    public static final String attack2 = "entities/sounds/attack2.ogg";
    public static final String attack3 = "entities/sounds/attack3.ogg";
    public static final String jump = "entities/sounds/jump.ogg";
    public static final String doublejump = "entities/sounds/doublejump.ogg";
    public static final String coinSound = "entities/sounds/coin1.wav";

    // Music
    public final String playingSong = "";
    public static final String titleSong = "raw/audio/music/Title-Screen.ogg";
    public static final String level1 = "raw/audio/music/Level-1.ogg";
    public static final String ending = "raw/audio/music/Ending.ogg";

    public static final String skin = "skin/glassy-ui.json";
    public static final String BitPotion = "bitmaps/BitPotionExt.fnt";



    // a small set of images used by the loading screen
    public void queueAddLoadingAssets(){
        manager.load(loadingBackground, Texture.class);
        manager.load(skin, Skin.class);
        manager.load(titleSong,Music.class);
        queueAddFonts();
    }

    public void queueAddTextures(){
        manager.load(hudImage, Texture.class);
        manager.load(playerAtlas, TextureAtlas.class);
        manager.load(skeletonAtlas, TextureAtlas.class);
        manager.load(slimeAtlas, TextureAtlas.class);
        manager.load(gameOverImage, Texture.class);
        manager.load(witchTexture, Texture.class);
    }

    public void queueAddSounds(){
        manager.load(attack1, Sound.class);
        manager.load(attack2, Sound.class);
        manager.load(attack3, Sound.class);
        manager.load(jump, Sound.class);
        manager.load(doublejump, Sound.class);
        manager.load(coinSound, Sound.class);
    }

    public void queueAddMusic(){
       // manager.load(playingSong, Music.class);

        manager.load(level1,Music.class);
        manager.load(ending,Music.class);
    }

    public void queueAddFonts(){
        manager.load(BitPotion, BitmapFont.class);

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

}
