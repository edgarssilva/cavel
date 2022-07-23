package com.edgarsilva.pixelgame.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.edgarsilva.pixelgame.PixelGame;

public class GameplayPreferences {

    private static final String health = "health";
    private static final String coins  = "coins";
    private static  String PREFS_NAME = "cavel.gameplay";

    public GameplayPreferences() {
        update();
    }

    private Preferences preferences;

    protected Preferences getPrefs() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        return preferences;
    }

    public void update(){
        PixelGame.coins = getPrefs().getInteger(coins, 0);
        PixelGame.hp = (short) getPrefs().getInteger(health, 3);
    }

    public void reset() {
        getPrefs().clear();
    }
}
