package com.edgarsilva.pixelgame.engine.utils.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.CoinSystem;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public class HUDManager implements Updateable, Disposable {

    private PlayScreen screen;

    public Stage stage;

    /*private Texture raw;
    private static TextureRegion icon;

    private static ProgressBar healthBar;*/

    private Animation<TextureRegion> health;
    private Animation<TextureRegion> coin;

    private short previousHealth;
    private float lifeTimer = 0f;
    private float coinTimer = 0f;
    private BitmapFont font;
    private float fontWidth;
    private ComponentMapper<StatsComponent> stats;

    public static final int INPUT_INDEX = 1;


    public HUDManager(PlayScreen screen) {
        this.screen = screen;

        // font = new BitmapFont();

        font = PlayScreen.getGame().assets.font;
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.setUseIntegerPositions(false);
        fontWidth = font.getData().getGlyph('0').width;

        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT), screen.getBatch());

        TextureRegion[][] regions = TextureRegion.split(new Texture("raw/hearts.png"), 17, 17);
        TextureRegion[] hearts = regions[0];
        health = new Animation<TextureRegion>(1 / 12f, hearts);

        TextureAtlas atlas = screen.getGame().assets.manager.get(GameAssetsManager.atlas);

        coin = new Animation<TextureRegion>( 1 / 6f,
                atlas.findRegion("coin-0"),
                atlas.findRegion("coin-1"),
                atlas.findRegion("coin-2"),
                atlas.findRegion("coin-3"),
                atlas.findRegion("coin-4"),
                atlas.findRegion("coin-5"),
                atlas.findRegion("coin-6"),
                atlas.findRegion("coin-7"));

        stats = ComponentMapper.getFor(StatsComponent.class);
//        previousHealth = stats.get(EntityManager.getPlayer()).health;

    }

    @Override
    public void update(float deltaTime) {

        //Se o player estiver vivo atualizar a barra de hp
        if (stats.has(EntityManager.getPlayer())) {
            StatsComponent sc = stats.get(EntityManager.getPlayer());

            if (lifeTimer > 0 || previousHealth - sc.health > 0)
                if (health.isAnimationFinished(lifeTimer)){
                    lifeTimer = 0f;
                    previousHealth = sc.health;
                }else{
                    lifeTimer += deltaTime;
                }
            coinTimer += deltaTime;

            //healthBar.setValue(health);
            if (sc.health <= 0 ) screen.gameOver();


            //Update HUD
            stage.act(deltaTime);

            //Defenir a viewport do HUD ao screen
            screen.getBatch().setProjectionMatrix(stage.getViewport().getCamera().combined);
            screen.getBatch().enableBlending();

            font.getData().setScale(2);
            font.setColor(1, 1, 1, 1);

            String coins = String.valueOf(Math.round(CoinSystem.current_coins));
            //Draw HUD
            screen.getBatch().begin();
            for (int i = 1; i <= sc.health; i++) {
                screen.getBatch().draw(health.getKeyFrame(lifeTimer), 10 * i + 34 * i-1, stage.getHeight() - 47, 34, 34);
            }
            font.draw(screen.getBatch(), coins,stage.getWidth() - 86 - coins.length() * fontWidth * font.getScaleX() ,  stage.getHeight() - 20);
            screen.getBatch().draw(coin.getKeyFrame(coinTimer, true), stage.getWidth() - 90, stage.getHeight() - 60, 56, 68);
            // screen.getBatch().draw(icon,0, stage.getHeight() - icon.getRegionHeight());
            screen.getBatch().end();
            stage.draw();
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }


    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
        PlayScreen.getGame().assets.unloadAssets(GameAssetsManager.hudImage);
        font.dispose();
        //raw.dispose();
    }
}
