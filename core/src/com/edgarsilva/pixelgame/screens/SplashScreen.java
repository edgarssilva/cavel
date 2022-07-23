package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.edgarsilva.pixelgame.PixelGame;

public class SplashScreen implements Screen {

    private PixelGame game;
    private SpriteBatch batch;

    private float countDown = 3f;

    public SplashScreen(PixelGame game) {
        this.game = game;
        batch = new SpriteBatch();
        game.assets.queueAddLoadingImages();
        game.assets.queueAddSounds();
        game.assets.queueAddMusic();
        game.assets.queueAddFonts();
        game.assets.manager.finishLoading();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1); //  clear the screen
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.end();

        countDown -= delta;
        if (countDown < 0) {
            game.setScreen(new MenuScreen(game));
        }

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
        batch.dispose();
    }
}
