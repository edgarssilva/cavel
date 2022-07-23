package com.edgarsilva.pixelgame.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.edgarsilva.pixelgame.PixelGame;

public class GraphicPreferences {

    private static final String PREF_FULLSCREEN = "window.fullscreen";
    private static  String PREFS_NAME = "cavel.graphics";

    public GraphicPreferences() {
        updateScreen();
    }

    private Preferences preferences;

    protected Preferences getPrefs() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        return preferences;
    }

    public void updateScreen(){
        if(getPrefs().getBoolean(PREF_FULLSCREEN))
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        else
            Gdx.graphics.setWindowedMode((int) PixelGame.WIDTH,(int) PixelGame.HEIGHT);
    }

    public void changeWindowMode() {
            getPrefs().putBoolean(PREF_FULLSCREEN,!getPrefs().getBoolean(PREF_FULLSCREEN) );
            getPrefs().flush();
            updateScreen();
    }

    public void reset() {
        getPrefs().clear();
    }

}
