package com.edgarsilva.pixelgame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.managers.SoundManager;
import com.edgarsilva.pixelgame.preferences.GamePreferences;
import com.edgarsilva.pixelgame.screens.EndScreen;
import com.edgarsilva.pixelgame.screens.LoadingScreen;
import com.edgarsilva.pixelgame.screens.MenuScreen;
import com.edgarsilva.pixelgame.screens.PlayScreen;
import com.edgarsilva.pixelgame.screens.SettingsScreen;


public class PixelGame extends Game {

    public static final float WIDTH = 960;
    public static final float HEIGHT = 540;

    public static boolean DEBUG = true;

    private GamePreferences preferences;

    public  GameAssetsManager assets;

    private LoadingScreen loadingScreen;
    private SettingsScreen settingsScreen;
    private MenuScreen menuScreen;
    private PlayScreen playScreen;
    private EndScreen endScreen;

    public final static int MENU = 0;
    public final static int SETTINGS = 1;
    public final static int APPLICATION = 2;
    public final static int ENDGAME = 3;


    public PixelGame() {
        assets = new GameAssetsManager();
    }


    @Override
    public void render() {
        super.render();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11))
            preferences.graphics.changeWindowMode();
    }

    public void changeScreen(int screen){
        switch(screen){
            case MENU:
                if(menuScreen == null) menuScreen = new MenuScreen(this);
                this.setScreen(menuScreen);
                break;
            case SETTINGS:
                if(settingsScreen == null) settingsScreen = new SettingsScreen(this);
                this.setScreen(settingsScreen);
                break;
            case APPLICATION:
                 playScreen = new PlayScreen(this);
               // else playScreen.restartGame();*/
                this.setScreen(playScreen);
                break;
            case ENDGAME:
                if(endScreen == null) endScreen = new EndScreen(this);
                this.setScreen(endScreen);
                break;
        }
    }


	@Override
	public void create () {
      /*  Gdx.graphics.setVSync(false);
        System.out.println(Gdx.graphics.getGLVersion().getMajorVersion());*/
        preferences = new GamePreferences();
        new SoundManager(preferences.sound, assets);

        if (Gdx.app.getType() == Application.ApplicationType.Android)
            Gdx.input.setCatchBackKey(true);


        loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
        // setScreen(new TestScreen());
	}

	@Override
	public void dispose () {
        super.dispose();
        super.screen.dispose();
        assets.manager.dispose();

    }

    public GamePreferences getPreferences() {
        return preferences;
    }
}
