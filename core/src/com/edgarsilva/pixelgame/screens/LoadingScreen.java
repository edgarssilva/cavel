package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private ShapeRenderer shape;

    private String map;
    private Save save;

    private float frame;

    private int bX = 0;
    private float minDuration = 2f;
    private float playerX = 100f;
    private float alpha = 0;

    /**
     *  Construtor da classe
     *
     * @param game Instância do jogo.
     */
    public LoadingScreen(PixelGame game) {
        this.game = game;
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

        bX = 0;
        minDuration = 2f;
        playerX = 100f;
        alpha = 0;

        viewport = new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT);
        shape = new ShapeRenderer();

        game.assets.queueAddTextures();
        game.assets.queueAddFonts();
        game.assets.queueAddParticleEffects();
        game.assets.queueAddMusic();
        game.assets.queueAddSounds();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.gl.glClearColor(33/255f,38/255f,63/255f,1);


        if (game.assets.manager.update() &&  minDuration < 0) {
            playerX += 800 * delta;
            alpha += delta * 0.75;

            shape.setProjectionMatrix(viewport.getCamera().combined);

            Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
            Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            shape.setColor(0, 0, 0, alpha);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shape.end();
            Gdx.gl.glDisable(Gdx.gl.GL_BLEND);

            if (alpha > 1.5){
                if (save != null)
                    game.setSave(PixelGame.PLAY_SCREEN, save);
                else if (map != null)
                    game.setMap(PixelGame.PLAY_SCREEN, map);
            }

        }else{
            minDuration -= delta;
            bX += 1200 * delta;

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
        shape.dispose();
    }

}
