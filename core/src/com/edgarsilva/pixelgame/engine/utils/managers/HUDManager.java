package com.edgarsilva.pixelgame.engine.utils.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
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

    private Texture health;
    private Texture background;

    private float healthBarScale = 1;
    private float healthBarDamage = 0;
    private float previousHealth;

    private BitmapFont font;
    private ComponentMapper<StatsComponent> stats;

    public static final int INPUT_INDEX = 1;


    public HUDManager(PlayScreen screen) {
        this.screen = screen;

       // font = new BitmapFont();

          font = PlayScreen.getGame().assets.font;
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.setUseIntegerPositions(false);


        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT), screen.getBatch());

        health     = new Texture("raw/player_health.png");
        background = new Texture("raw/player_health_background.png");

        stats = ComponentMapper.getFor(StatsComponent.class);
    }

    @Override
    public void update(float deltaTime) {

        //Se o player estiver vivo atualizar a barra de hp
        if (stats.has(EntityManager.getPlayer())) {
            StatsComponent sc = stats.get(EntityManager.getPlayer());

            float scale = sc.maxHealth / 75f;
            float damaged = previousHealth - sc.health;

            previousHealth = sc.health;
            healthBarScale = sc.health / (float) sc.maxHealth;
            if (damaged > 0) healthBarDamage = healthBarScale + (damaged / (float) sc.maxHealth);
            else  healthBarDamage = MathUtils.lerp(healthBarDamage, healthBarScale, deltaTime * 5);
            healthBarDamage = healthBarDamage < healthBarScale ? healthBarScale : healthBarDamage;

            //healthBar.setValue(health);
            if (sc.health <= 0 ) screen.gameOver();


            //Update HUD
            stage.act(deltaTime);

            //Defenir a viewport do HUD ao screen
            screen.getBatch().setProjectionMatrix(stage.getViewport().getCamera().combined);
            screen.getBatch().enableBlending();

            font.getData().setScale(2);
            font.setColor(1, 1, 1, 1);


            //Draw HUD
            screen.getBatch().begin();
            screen.getBatch().draw(health, 10, stage.getHeight() - background.getHeight()  - 20 * scale, 250f * scale * healthBarDamage, 20 * scale);
            screen.getBatch().draw(background, 10, stage.getHeight() - background.getHeight()  - 20 * scale, 250f * scale , 20 * scale);
            font.draw(screen.getBatch(),  String.valueOf(Math.round(CoinSystem.current_coins)),stage.getWidth() - 100,  stage.getHeight() - 20);
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
