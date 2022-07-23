package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.managers.Save;


/**
 * Classe responsável por carregar os assets necessários e libertar os desnecessários
 * enquanto demonstra uma animação de espera.
 */
public class LoadingScreen implements Screen {

    private PixelGame game;

    private Viewport viewport;
    private SpriteBatch batch;

    private String map;
    private Save save;

    private BitmapFont font;

    private float minDuration = 1000f;
    private long startTime;

    /**
     *  Construtor da classe
     *
     * @param game Instância do jogo.
     */
    public LoadingScreen(PixelGame game) {
        this.game = game;

        viewport = new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT);
        batch = new SpriteBatch();

    }

    public void setMap(String map) {
        this.map  = map;
        this.save = null;
    }
    public void setSave(Save save){
        this.save = save;
        this.map  = null;
    }

    @Override
    public void show() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.assets.queueAddTextures();
        game.assets.queueAddFonts();
        game.assets.queueAddParticleEffects();
        game.assets.queueAddMusic();
        game.assets.queueAddSounds();

        font = game.assets.font;
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        startTime = System.currentTimeMillis();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.gl.glClearColor(33/255f,38/255f,63/255f,1);

        minDuration -= delta;

        batch.setProjectionMatrix(viewport.getCamera().combined);
        font.setColor(1, 1, 1, 1);
        font.getData().setScale(3.5f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        batch.begin();
        font.draw(batch, "Loading . . ." ,PixelGame.WIDTH - 230f, 50f);
        batch.end();

        if (game.assets.manager.update() && System.currentTimeMillis() - startTime >= minDuration ) {
            if (save != null)
                game.setSave(PixelGame.PLAY_SCREEN, save);
            else if (map != null)
                game.setMap(PixelGame.PLAY_SCREEN, map);
        }
        Gdx.graphics.setTitle("Cavel - " + game.assets.manager.getProgress() * 100 +"%");
    }



    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
