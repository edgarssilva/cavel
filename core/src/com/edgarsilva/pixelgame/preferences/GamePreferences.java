package com.edgarsilva.pixelgame.preferences;

public class GamePreferences {

    public static final String PREF_NAME = "settings";

    public SoundPreferences    sound    = new SoundPreferences(PREF_NAME);
    public GraphicPreferences  graphics = new GraphicPreferences(PREF_NAME);


}
