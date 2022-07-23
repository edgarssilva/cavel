package com.edgarsilva.pixelgame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.managers.SoundManager;
import com.edgarsilva.pixelgame.preferences.GamePreferences;
import com.edgarsilva.pixelgame.screens.LevelScreen;
import com.edgarsilva.pixelgame.screens.LoadingScreen;
import com.edgarsilva.pixelgame.screens.MenuScreen;
import com.edgarsilva.pixelgame.screens.PlayScreen;
import com.edgarsilva.pixelgame.screens.SettingsScreen;
import com.edgarsilva.pixelgame.screens.SplashScreen;


public class PixelGame extends Game {

    public static final float WIDTH = 960;
    public static final float HEIGHT = 540;

    public static boolean DEBUG = true;

    public static int coins = 0;

    private GamePreferences preferences;

    public GameAssetsManager assets;
    public SoundManager sound;

    public static final short SPLASH_SCREEN   = 0;
    public static final short LOADING_SCREEN  = 1;
    public static final short PLAY_SCREEN     = 2;
    public static final short MENU_SCREEN     = 3;
    public static final short LEVEL_SCREEN    = 4;
    public static final short SETTINGS_SCREEN = 5;

    private SplashScreen   splashScreen;
    private LoadingScreen  loadingScreen;
    private PlayScreen     playScreen;
    private MenuScreen     menuScreen;
    private LevelScreen    levelScreen;
    private SettingsScreen settingsScreen;

    private int previousScreen = 0;

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
        sound = new SoundManager(preferences.sound, assets);

        if (Gdx.app.getType() == Application.ApplicationType.Android)
            Gdx.input.setCatchBackKey(true);


        //setScreen(LOADING_SCREEN, "maps/2.tmx");
        // setScreen(new TestScreen());
         setScreen(SPLASH_SCREEN);
    }


    public void setScreen(int screen){
        switch (screen) {
            case SPLASH_SCREEN:
                if (splashScreen == null) splashScreen = new SplashScreen(this);
                setScreen(splashScreen);
                break;
            case MENU_SCREEN:
                if (menuScreen == null) menuScreen = new MenuScreen(this);
                setScreen(menuScreen);
                break;
            case LOADING_SCREEN:
                if (loadingScreen != null) setScreen(loadingScreen);
                break;
            case PLAY_SCREEN:
                if (playScreen != null) setScreen(playScreen);
                break;
            case LEVEL_SCREEN:
                if (levelScreen == null) levelScreen = new LevelScreen(this);
                setScreen(levelScreen);
                break;
            case SETTINGS_SCREEN:
                if (settingsScreen == null) settingsScreen = new SettingsScreen(this, previousScreen);
                settingsScreen.setPreviousScreen(previousScreen);
                setScreen(settingsScreen);
                break;
        }
        previousScreen = screen;
    }

    public void setScreen(int screen, String map){
        switch (screen) {
            case LOADING_SCREEN:
                if (loadingScreen == null) loadingScreen = new LoadingScreen(this, map);
                else loadingScreen.setMap(map);
                break;
            case PLAY_SCREEN:
                if (playScreen == null) playScreen = new PlayScreen(this, map);
                else playScreen.setMap(map);
                break;
        }
        setScreen(screen);
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
