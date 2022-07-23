package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;

public class MenuScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private PixelGame game;

    public MenuScreen(PixelGame pixelGame) {
        this.game = pixelGame;
        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT));

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(PixelGame.DEBUG);
        stage.addActor(table);
        stage.setDebugAll(PixelGame.DEBUG);

        skin = pixelGame.assets.getSkin();

        BitmapFont bitPotion = game.assets.manager.get(GameAssetsManager.BitPotion, BitmapFont.class);

        bitPotion.getData().setScale(6);

        TextButton.TextButtonStyle previous = skin.get(TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(
                previous.up, previous.down, previous.checked,bitPotion);


        TextButton newGame = new TextButton("New Game", style);
        TextButton preferences = new TextButton("Preferences", style);
        TextButton exit = new TextButton("Exit", style);

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(PixelGame.LEVEL_SCREEN);
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(PixelGame.SETTINGS_SCREEN);
            }
        });

        exit.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        TextButton connect = new TextButton("Connect", style);

        connect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                /*LoginManager.login(
                        Base64Coder.encodeString("admin"),
                        Base64Coder.encodeString("admin"),
                        LoginManager.DEFAULT_LISTENER);*/
                game.setScreen(new LoginScreen(game));
            }
        });

        connect.setTransform(true);
        connect.setScale(0.4f);
        connect.setOrigin(Align.right, Align.bottom);
        connect.setPosition(PixelGame.WIDTH - 150f, 20f);
        stage.addActor(connect);

        table.add(newGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(preferences).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();
        table.row();


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        game.sound.setMusic(GameAssetsManager.titleSong,true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
