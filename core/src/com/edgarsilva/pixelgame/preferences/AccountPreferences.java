package com.edgarsilva.pixelgame.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Base64Coder;

public class AccountPreferences {

    private static String PREFS_NAME = "cavel.account";
    public static final String USER = "username";
    public static final String PASS = "password";

    private Preferences preferences;

    protected Preferences getPrefs() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        return preferences;
    }
    public void setUser(String username, String password){
        getPrefs().putString(USER, Base64Coder.encodeString(username));
        getPrefs().putString(PASS, Base64Coder.encodeString(password));
        getPrefs().flush();
    }

    public String getUser() {
        return getPrefs().getString(USER, null);
    }

    public String getPass() {
        String pass = getPrefs().getString(PASS, null);
        return pass;
    }

    public boolean logedIn() {
        return (!(getPrefs().getString(PASS, null) == null || getPrefs().getString(USER, null) == null));
    }

    public void reset() {
        getPrefs().remove(USER);
        getPrefs().remove(PASS);
        getPrefs().flush();
    }
}
