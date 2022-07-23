package com.edgarsilva.pixelgame.preferences;

/**
 * Classe que guarda as preferências e outros, do jogador
 */

public class GamePreferences {

    public SoundPreferences    sound;
    public GraphicPreferences  graphics;
    public GameplayPreferences gameplay;
    public AccountPreferences  account;

    public GamePreferences() {
        sound    = new SoundPreferences();
        graphics = new GraphicPreferences();
        gameplay = new GameplayPreferences();
        account  = new AccountPreferences();
    }

    public void reset(){
        sound.reset();
        graphics.reset();
        gameplay.reset();
        account.reset();
    }
}
