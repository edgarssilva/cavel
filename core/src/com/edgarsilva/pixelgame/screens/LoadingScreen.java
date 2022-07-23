package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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
    private String map;

    private Animation anim;
    private float frame;

    private SpriteBatch batch;
    private Texture background;
    private int bX;


    private Viewport viewport;
    private OrthographicCamera camera;

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


        camera = new OrthographicCamera();
        viewport = new ExtendViewport(PixelGame.WIDTH, PixelGame.HEIGHT, camera);

        TextureAtlas atlas = new TextureAtlas("entities/sprites/Player.atlas");
        background = new Texture("raw/loading.png");

        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.addAll(
                atlas.findRegion("adventurer-run-00"),
                atlas.findRegion("adventurer-run-01"),
                atlas.findRegion("adventurer-run-02"),
                atlas.findRegion("adventurer-run-03"),
                atlas.findRegion("adventurer-run-04"),
                atlas.findRegion("adventurer-run-05"));

        anim = new Animation<TextureRegion>( 1 / 12f, frames, Animation.PlayMode.LOOP);

        batch = new SpriteBatch();
        bX = background.getWidth();

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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.gl.glClearColor(33/255f,38/255f,63/255f,1);
        Gdx.gl.glClearColor(0 ,0 ,0 ,1);


        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, bX - camera.viewportWidth, 0,  camera.viewportWidth, camera.viewportHeight);
        batch.draw(background, bX,0,camera.viewportWidth, camera.viewportHeight);
        batch.draw((TextureRegion) anim.getKeyFrame(frame), 100, 150, 180, 120);
        batch.end();


        frame += delta;
        bX -= 10;

        if(bX < 0) bX = (int) camera.viewportWidth;

        if(game.assets.manager.update()){
            game.setScreen(new PlayScreen(game, map));
            this.dispose();
        }else{
            Gdx.graphics.setTitle("Cavel - " + game.assets.manager.getProgress() +"%");
        }
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

    }
}
