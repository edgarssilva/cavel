package com.edgarsilva.pixelgame.engine.utils.managers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public class PauseManager {

    private Stage stage;
    private Table table;

    private PlayScreen screen;

    public PauseManager(PlayScreen screen) {
        this.screen = screen;

        stage = new Stage(screen.getCameraManager().getViewport(), screen.getBatch());
        table = new Table();
    }

}
