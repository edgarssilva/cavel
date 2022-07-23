package com.edgarsilva.pixelgame.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Base64Coder;
import com.edgarsilva.pixelgame.PixelGame;


public class GameSave {

    private static final String PREF_NAME = "save";

    public static void save(String game){
        Preferences preferences = Gdx.app.getPreferences(PREF_NAME);
        preferences.putString("coins", Base64Coder.encodeString(String.valueOf(PixelGame.coins)));
        preferences.putString("game", Base64Coder.encodeString(game));
        preferences.flush();
    }

    public static String load(){
        Preferences preferences = Gdx.app.getPreferences(PREF_NAME);

        try {
            PixelGame.coins = Integer.parseInt(Base64Coder.decodeString(preferences.getString("coins")));
        } catch (NumberFormatException ex) {
            PixelGame.coins = 0;
        }

        try {
            PixelGame.hp = Short.parseShort(Base64Coder.decodeString(preferences.getString("maxHealth")));
        } catch (NumberFormatException ex) {
        PixelGame.hp = 3;
    }

       return(Base64Coder.decodeString(preferences.getString("game", "")));
    }
}
