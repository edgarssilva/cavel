package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.managers.LoginManager;

public class LoginScreen implements Screen {


    public static Stage stage;
    private Table table;

    private TextField username;
    private TextField password;
    private TextButton submit;
    private TextButton exit;
    private TextButton signUp;

    public static Dialog successDialog;
    public static Dialog errorDialog;

    public LoginScreen(final PixelGame game) {
        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT));
        stage.setDebugAll(PixelGame.DEBUG);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        successDialog = new Dialog("Login", PixelGame.assets.getSkin()){
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    game.setScreen(PixelGame.MENU_SCREEN);
                }
            }
        };
        successDialog.text("Success");
        successDialog.button("OK",true);
        successDialog.key(Input.Keys.ENTER, true);

        errorDialog = new Dialog("Login", PixelGame.assets.getSkin()){
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    game.setScreen(PixelGame.MENU_SCREEN);
                }
            }
        };
        //errorDialog.text("Error");
        errorDialog.button("OK",true);
        errorDialog.key(Input.Keys.ENTER, true);


        username = new TextField("", game.assets.getSkin());
        password = new TextField("", game.assets.getSkin());
        submit = new TextButton("Login", game.assets.getSkin());
        exit = new TextButton("Back", game.assets.getSkin());
        signUp = new TextButton("Signup", game.assets.getSkin());


        submit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LoginManager.login(
                        Base64Coder.encodeString(username.getText()),
                        Base64Coder.encodeString(password.getText()),
                        LoginManager.DEFAULT_LISTENER);
            }
        });

        signUp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI("https://spaghettigames.ga/signupPage.php");
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(PixelGame.MENU_SCREEN);
            }
        });

        game.assets.getSkin().getFont("BitPotionExt").getData().setScale(2f);

        submit.setScale(2f);
        submit.getLabel().setFontScale(3f);

        signUp.setScale(2f);
        signUp.getLabel().setFontScale(1.5f);
        signUp.setPosition(stage.getWidth() - 80, stage.getHeight() - 40);

        exit.setScale(3f);
        exit.getLabel().setFontScale(2f);
        exit.setPosition(10, 10);


        table.add(new Label("Username", game.assets.getSkin())).left();
        table.row();
        table.add(username).width(300).height(40);
        table.row().padTop(30);
        table.add(new Label("Password", game.assets.getSkin())).left();
        table.row();
        table.add(password).width(300).height(40);
        table.row().padTop(30);
        table.add(submit).width(150).height(50);
        stage.addActor(exit);
        stage.addActor(signUp);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(24 / 255f, 32 / 255f, 61 / 255f, 1);
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
