package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;

/**
 * Este ecrã desmonstra uma animação da equipa "Spaghetti Games"
 * enquanto carrega em memória os recursos necessários para os menus
 */

public class SplashScreen implements Screen {

    private PixelGame game;
    private SpriteBatch batch;
    private Viewport viewport;

    private float countDown = 3f; // Tempo da animação

    private Sprite spaghetti;
    private Sprite games;
    private Sprite logo;

    public SplashScreen(PixelGame game) {
        this.game = game;

        batch = new SpriteBatch();
        viewport = new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT);


        game.assets.queueAddLoadingAssets();
        game.assets.manager.finishLoading();
        ObjectSet<Texture> textures = game.assets.getSkin().getAtlas().getTextures();

        for (Texture texture : textures) {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }

        TextureAtlas atlas = game.assets.manager.get(GameAssetsManager.splashAtlas);
        games     = new Sprite(atlas.findRegion("games"));
        spaghetti = new Sprite(atlas.findRegion("spaghetti"));
        logo      = new Sprite(atlas.findRegion("logo"));

        games.setPosition(330f, -80f);
        logo.setPosition(PixelGame.WIDTH, 120);
        spaghetti.setPosition(110f, PixelGame.HEIGHT);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1); //  clear the screen
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        games.draw(batch);
        spaghetti.draw(batch);
        logo.draw(batch);
        batch.end();

        // Atulizar o carregamento das texturas
        game.assets.manager.update();

        // Movimento das texturas
        if (spaghetti.getY() == 260f) {
            if (games.getY() == 200f) {
                if (logo.getX() == 600f)
                    countDown -= delta;
                else if (logo.getX() < 600f) logo.setX(600f);
                else logo.setX(logo.getX() - delta * 400f);
            } else if (games.getY() > 200f) games.setY(200f);
            else games.setY(games.getY() + delta * 400f);
        }else {
            if (spaghetti.getY() < 260f) spaghetti.setY(260f);
            else spaghetti.setY(spaghetti.getY() - delta * 400f);
        }

        if (countDown < 0) game.setScreen(PixelGame.MENU_SCREEN);
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
    }
}
