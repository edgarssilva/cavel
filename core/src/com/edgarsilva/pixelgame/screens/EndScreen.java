package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.utils.Scene2dAnimations;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;

public class EndScreen implements Screen {

    private PixelGame game;
    private Scene2dAnimations animations;

    private Stage stage;

    public EndScreen(PixelGame pixelGame) {
        this.game = pixelGame;

        stage = new Stage(new ScreenViewport());

        animations = new Scene2dAnimations(this,
                new TextureRegion(pixelGame.assets.manager.get(GameAssetsManager.gameOverImage,Texture.class)),
                stage,Scene2dAnimations.FADEOUT
        );
    }

    @Override
    public void show() {
        game.sound.setMusic(GameAssetsManager.ending, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(PixelGame.MENU_SCREEN);
        }
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
