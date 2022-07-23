package com.edgarsilva.pixelgame.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Base64Coder;
import com.edgarsilva.pixelgame.managers.Save;

/**
 * Classe que guarda o jogo no exato momento usando a classe Save.
 * @see Save
 *
 */
public class GameSave {

    private static final String PREF_NAME = "cavel.save";

    public static void save(String game){
        Preferences preferences = Gdx.app.getPreferences(PREF_NAME);
        preferences.putString("game", Base64Coder.encodeString(game));
        preferences.flush();
    }

    public static String load(){
        Preferences preferences = Gdx.app.getPreferences(PREF_NAME);
       return(Base64Coder.decodeString(preferences.getString("game", "")));
    }
}
