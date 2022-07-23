package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.edgarsilva.pixelgame.PixelGame;



/**
 * Classe responsável por carregar os assets necessários e libertar os desnecessários
 * enquanto demonstra uma animação de espera.
 *
 *
 */
public class LoadingScreen implements Screen {

    private PixelGame game;

    private Viewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer shape;

    private String map;

    private Animation anim;
    private float frame;
    private Texture background;

    private int bX = 0;
    private float minDuration = 2f;
    private float playerX = 100f;
    private float alpha = 0;

    /**
     *  Construtor da classe
     *
     * @param game Instância do jogo.
     */
    public LoadingScreen(PixelGame game, String map) {
        this.game = game;
        this.map = map;

        game.assets.queueAddLoadingImages();
        game.assets.manager.finishLoading();

        viewport = new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT);
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        TextureAtlas atlas = new TextureAtlas("entities/sprites/Player.atlas");
        background = new Texture("raw/loading.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.addAll(
                atlas.findRegion("adventurer-run-00"),
                atlas.findRegion("adventurer-run-01"),
                atlas.findRegion("adventurer-run-02"),
                atlas.findRegion("adventurer-run-03"),
                atlas.findRegion("adventurer-run-04"),
                atlas.findRegion("adventurer-run-05"));

        anim = new Animation<TextureRegion>( 1 / 12f, frames, Animation.PlayMode.LOOP);

        game.assets.queueAddTextures();
        game.assets.queueAddFonts();
        game.assets.queueAddParticleEffects();
        game.assets.queueAddMusic();
        game.assets.queueAddSounds();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.gl.glClearColor(33/255f,38/255f,63/255f,1);

        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        batch.draw(background, 0, 0, bX, 0, (int) viewport.getWorldWidth(), (int) viewport.getWorldHeight());
        batch.draw((TextureRegion) anim.getKeyFrame(frame += delta), playerX, 150, 180, 120);
        batch.end();

        if (game.assets.manager.update() &&  minDuration < 0) {
            playerX += 8;
            alpha += delta * 0.75;

            shape.setProjectionMatrix(viewport.getCamera().combined);

            Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
            Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            shape.setColor(0, 0, 0, alpha);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shape.end();
            Gdx.gl.glDisable(Gdx.gl.GL_BLEND);

            if (alpha > 1.5) {
                game.setScreen(new PlayScreen(game, map));
                dispose();
            }

        }else{
            minDuration -= delta;
            bX += 10;

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
        shape.dispose();
    }
}
