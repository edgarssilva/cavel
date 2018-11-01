package com.edgarsilva.pixelgame.engine.utils.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public class OnScreenController extends Controller implements Updateable, Disposable {

    Stage stage;

    public OnScreenController(PlayScreen screen) {
        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT), screen.getBatch());
        Gdx.input.setInputProcessor(stage);

        Image upImage = new Image(new Texture("hud/flatDark/flatDark25.png"));
        Image downImage = new Image(new Texture("hud/flatDark/flatDark26.png"));
        Image leftImage = new Image(new Texture("hud/flatDark/flatDark23.png"));
        Image rightImage = new Image(new Texture("hud/flatDark/flatDark24.png"));
        Image attackImage = new Image(new Texture("hud/flatDark/flatDark48.png"));

        upImage.setSize(70, 70);
        downImage.setSize(70, 70);
        rightImage.setSize(70, 70);
        leftImage.setSize(70, 70);
        attackImage.setSize(70, 70);

        upImage.setPosition(PixelGame.WIDTH - upImage.getWidth() * 2, upImage.getHeight() * 2);
        rightImage.setPosition(leftImage.getWidth() + rightImage.getWidth() * 2, rightImage.getHeight() / 2);
        leftImage.setPosition(leftImage.getWidth(), leftImage.getHeight() / 2);
        attackImage.setPosition(PixelGame.WIDTH - attackImage.getWidth() * 3f, attackImage.getHeight() * 0.5f);

        upImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                up = true;
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                up = false;
                return;
            }
        });

        downImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                down = true;
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                down = false;
                return;
            }
        });

        leftImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                left = true;
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                left = false;
                return;
            }
        });

        rightImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                right = true;
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                right = false;
                return;
            }
        });

        attackImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                attack = true;
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                attack = false;
                return;
            }
        });

        stage.addActor(upImage);
        // stage.addActor(downImage);
        stage.addActor(leftImage);
        stage.addActor(rightImage);
        stage.addActor(attackImage);
        EntityManager.add(this);

    }


    @Override
    public void update(float deltaTime) {
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
