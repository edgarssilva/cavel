package com.edgarsilva.pixelgame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.managers.SoundManager;
import com.edgarsilva.pixelgame.preferences.GamePreferences;
import com.edgarsilva.pixelgame.screens.SplashScreen;


public class PixelGame extends Game {

    public static final float WIDTH = 960;
    public static final float HEIGHT = 540;

    public static boolean DEBUG = true;

    private GamePreferences preferences;

    public  GameAssetsManager assets;

    public PixelGame() {
        assets = new GameAssetsManager();
    }

    @Override
    public void render() {
        super.render();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11))
            preferences.graphics.changeWindowMode();
    }

    @Override
    public void create () {
        preferences = new GamePreferences();
        new SoundManager(preferences.sound, assets);

        if (Gdx.app.getType() == Application.ApplicationType.Android)
            Gdx.input.setCatchBackKey(true);



        setScreen(new SplashScreen(this));
        // setScreen(new TestScreen());
       // setScreen(new LoadingScreen(this, "maps/Cave.tmx"));
    }

    @Override
    public void dispose () {
        super.dispose();
        assets.manager.dispose();

    }

    public GamePreferences getPreferences() {
        return preferences;
    }
}
