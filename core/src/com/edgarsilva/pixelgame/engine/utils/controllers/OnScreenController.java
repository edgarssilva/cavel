package com.edgarsilva.pixelgame.engine.utils.controllers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public class OnScreenController extends Controller implements Updateable, Disposable {

    Stage stage;

    public OnScreenController(PlayScreen screen) {
        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT), screen.getBatch());
        //Gdx.input.setInputProcessor(stage);

        Image upImage = new Image(new Texture("hud/flatDark25.png"));
        Image downImage = new Image(new Texture("hud/flatDark26.png"));
        Image leftImage = new Image(new Texture("hud/flatDark23.png"));
        Image rightImage = new Image(new Texture("hud/flatDark24.png"));
        Image attackImage = new Image(new Texture("hud/flatDark48.png"));

        upImage.setSize(70, 70);
        downImage.setSize(70, 70);
        rightImage.setSize(70, 70);
        leftImage.setSize(70, 70);
        attackImage.setSize(70, 70);

        upImage.setPosition(PixelGame.WIDTH - attackImage.getWidth() * 3f, attackImage.getHeight() * 0.5f);
        rightImage.setPosition(leftImage.getWidth() + rightImage.getWidth() * 2, rightImage.getHeight() / 2);
        leftImage.setPosition(leftImage.getWidth(), leftImage.getHeight() / 2);
        attackImage.setPosition(PixelGame.WIDTH - upImage.getWidth() * 2, upImage.getHeight() * 2);

        upImage.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                up = true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                up = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                up = false;
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                up = false;
                return super.keyTyped(event, character);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                up = false;
                super.exit(event, x, y, pointer, toActor);
            }
        });

        downImage.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                down = true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                down = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                down = false;
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                down = false;
                return super.keyTyped(event, character);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                down = false;
                super.exit(event, x, y, pointer, toActor);
            }
        });

        leftImage.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                right = false;
                left = true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                left = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                left = false;
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                left = false;
                return super.keyTyped(event, character);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                left = false;
                super.exit(event, x, y, pointer, toActor);
            }
        });

        rightImage.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                right = true;
                left = false;
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                right = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                right = false;
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                right = false;
                return super.keyTyped(event, character);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                right = false;
                super.exit(event, x, y, pointer, toActor);
            }
        });

        attackImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                attack = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                attack = false;
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                attack = false;
                return super.keyTyped(event, character);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                attack = false;
                super.exit(event, x, y, pointer, toActor);
            }
        });

        stage.addActor(upImage);
        // stage.addActor(downImage);
        stage.addActor(leftImage);
        stage.addActor(rightImage);
        stage.addActor(attackImage);
        //EntityManager.add(this);

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

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }
}
