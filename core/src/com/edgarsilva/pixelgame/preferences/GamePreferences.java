package com.edgarsilva.pixelgame.preferences;

public class GamePreferences {

    public static String PREF_NAME = "default";

    public SoundPreferences    sound    = new SoundPreferences(PREF_NAME);
    public GraphicPreferences  graphics = new GraphicPreferences(PREF_NAME);


}
