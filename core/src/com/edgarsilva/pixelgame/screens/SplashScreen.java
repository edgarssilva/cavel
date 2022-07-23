package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.edgarsilva.pixelgame.PixelGame;

public class SplashScreen implements Screen {

    private PixelGame game;
    private SpriteBatch batch;

    public final int IMAGE = 0;
    public final int FONT = 1;
    public final int PARTY = 2;
    public final int SOUND = 3;
    public final int MUSIC = 4;

    private int currentLoadingStage = 0;


    public float countDown = 3f;

    public SplashScreen(PixelGame game) {
        this.game = game;
        batch = new SpriteBatch();
        game.assets.queueAddLoadingImages();
        game.assets.manager.finishLoading();
        game.assets.queueAddTextures();
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


        if (game.assets.manager.update()) {
            currentLoadingStage += 1;
            switch (currentLoadingStage) {
                case FONT:
                    System.out.println("Loading fonts....");
                    game.assets.queueAddFonts();
                    break;
                case PARTY:
                    System.out.println("Loading Particle Effects....");
                    game.assets.queueAddParticleEffects();
                    break;
                case SOUND:
                    System.out.println("Loading Sounds....");
                    game.assets.queueAddSounds();
                    break;
                case MUSIC:
                    System.out.println("Loading fonts....");
                    game.assets.queueAddMusic();
                    break;
                case 5:
                    System.out.println("Finished");
                    break;
            }
            if (currentLoadingStage > 5) {
                countDown -= delta;
                currentLoadingStage = 5;
                if (countDown < 0) {
                    game.setScreen(new LevelScreen(game));
                }
            }
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
