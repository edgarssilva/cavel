package com.edgarsilva.pixelgame.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GameAssetsManager {

    public final AssetManager manager = new AssetManager();

    // Textures
    //public static final String gameImages = "";
    //public static final String loadingImages = "";
    //public static final String meleeAttack = "sprites/swoosh.png";
    public static final String hudImage = "hud/ui_upscaled.png";
    public static final String playerAtlas = "entities/sprites/Player.atlas";
    public static final String skeletonAtlas = "entities/sprites/Skeleton.atlas";
    public static final String slimeAtlas = "entities/sprites/slime.atlas";
    public static final String gameOverImage = "raw/textures/gameover.jpg";

    // Sounds
    public static final  String boingSound = "";
    public static final String pingSound = "";

    // Music
    public final String playingSong = "";
    public static final String titleSong = "raw/audio/music/Title-Screen.ogg";
    public static final String level1 = "raw/audio/music/Level-1.ogg";
    public static final String ending = "raw/audio/music/Ending.ogg";


    // a small set of images used by the loading screen
    public void queueAddLoadingImages(){
        //manager.load(loadingImages, TextureAtlas.class);
    }

    public void queueAddTextures(){
       // manager.load(gameImages, Texture.class);
       // manager.load(meleeAttack, Texture.class);
        manager.load(hudImage, Texture.class);
        manager.load(playerAtlas, TextureAtlas.class);
        manager.load(skeletonAtlas, TextureAtlas.class);
        manager.load(slimeAtlas, TextureAtlas.class);
        manager.load(gameOverImage, Texture.class);
    }

    public void queueAddSounds(){
        //manager.load(boingSound,Sound.class);
        //manager.load(pingSound,Sound.class);
    }

    public void queueAddMusic(){
       // manager.load(playingSong, Music.class);
        manager.load(titleSong,Music.class);
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

}
