package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.managers.LoginManager;

public class LoginScreen implements Screen {


    private Stage stage;
    private Table table;

    private TextField username;
    private TextField password;
    private Button submit;

    public LoginScreen(PixelGame game) {
        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT));
        stage.setDebugAll(PixelGame.DEBUG);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        username = new TextField("", game.assets.getSkin());
        password = new TextField("", game.assets.getSkin());
        submit = new Button(game.assets.getSkin());

        submit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LoginManager.login(
                        username.getText(),//Base64Coder.encodeString(username.getText()),
                        password.getText(),//Base64Coder.encodeString(password.getText()),
                        "save",
                        LoginManager.DEFAULT_LISTENER);
            }
        });

        table.add(username).fill().uniform();
        table.row().padTop(10).padBottom(10);
        table.add(password).fill().uniform();
        table.row().padTop(10).padBottom(10);
        table.add(submit);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
