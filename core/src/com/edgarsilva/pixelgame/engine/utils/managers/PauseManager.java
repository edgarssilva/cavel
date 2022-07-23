package com.edgarsilva.pixelgame.engine.utils.managers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.utils.ColorDrawer;
import com.edgarsilva.pixelgame.managers.Save;
import com.edgarsilva.pixelgame.screens.PlayScreen;
import com.edgarsilva.pixelgame.screens.SettingsScreen;


public class PauseManager {

    private Stage stage;
    private Table table;

    private PlayScreen screen;
    public static final int INPUT_INDEX = 3;

    public PauseManager(final PlayScreen screen) {
        this.screen = screen;
        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT), screen.getBatch());
        table = new Table(PlayScreen.getGame().assets.getSkin());

        stage.setDebugAll(PixelGame.DEBUG);

        table.setFillParent(true);

        TextButton continueBtn = new TextButton("Continue", PlayScreen.getGame().assets.getSkin());

        continueBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.unPause();
            }
        });

        TextButton saveBtn = new TextButton("Save", PlayScreen.getGame().assets.getSkin());
        saveBtn.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                new Save(screen);
            }
        });


        TextButton settingsBtn = new TextButton("Settings", PlayScreen.getGame().assets.getSkin());
        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PlayScreen.getGame().setScreen(new SettingsScreen(PlayScreen.getGame(), PixelGame.PLAY_SCREEN));
            }
        });
        TextButton exitButton = new TextButton("Exit", PlayScreen.getGame().assets.getSkin());
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.exit();
                PlayScreen.getGame().setScreen(PixelGame.MENU_SCREEN);
            }
        });

        Label pauseText = new Label("--- PAUSED ---", PlayScreen.getGame().assets.getSkin());
        pauseText.setAlignment(Align.center);
        pauseText.setFontScale(2.5f);

        table.add(pauseText).center().width(stage.getWidth() / 4).uniformY();
        table.row().pad(10, 0, 10, 0);
        table.add(continueBtn).height(continueBtn.getHeight() / 2).uniform();
        table.row().pad(10, 0, 10, 0);
        table.add(saveBtn).height(continueBtn.getHeight() / 2).uniform();
        table.row().pad(10, 0, 10, 0);
        table.add(settingsBtn).height(settingsBtn.getHeight() / 2).uniform();
        table.row().pad(10, 0, 10, 0);
        table.add(exitButton).height(exitButton.getHeight() / 2).uniform();

        stage.addActor(table);
    }


    public void render(){
        stage.act();

        //Fundo preto semi-transparente
        ColorDrawer.drawColor(screen.getShapeRenderer(), 0, 0, 0, 0.5f);

        stage.draw();
    }

    public void resize(int width, int height){
        stage.getViewport().update(width, height);
    }

    public Stage getStage() {
        return stage;
    }
}
