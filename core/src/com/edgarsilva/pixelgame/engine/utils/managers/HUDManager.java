package com.edgarsilva.pixelgame.engine.utils.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public class HUDManager implements Updateable, Disposable {

    private static Stage stage;
    private static Skin skin;

    private static SpriteBatch batch;

    private static Texture raw;
    private static TextureRegion icon;
    private static TextureRegion healthKnob;

    private static ProgressBar healthBar;
    private ComponentMapper<StatsComponent> stats;

    public HUDManager(String skinPath, PlayScreen screen) {
        HUDManager.batch = screen.getBatch();
        skin = new Skin(Gdx.files.internal(skinPath));
        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT), batch);

        //batch = new SpriteBatch();
        raw = screen.getGame().assets.manager.get(GameAssetsManager.hudImage);
        icon = new TextureRegion(raw, 0, 0, 220 ,80);
        healthKnob = new TextureRegion(raw, 240,24,102,8);

        //Set ProgressBarStyle
        TextureRegionDrawable drawable;

        Pixmap pixmap = new Pixmap(0, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        TextureRegionDrawable healthDrawable = new TextureRegionDrawable(healthKnob);

        ProgressBar.ProgressBarStyle style =  new ProgressBar.ProgressBarStyle();
        style.background = null;
        style.knob = drawable;
        style.knobBefore = healthDrawable;

        healthBar = new ProgressBar(0,100,1,false, style);
        healthBar.setBounds(90, stage.getHeight() - icon.getRegionHeight()/2+8, 100, 8);
        stage.addActor(healthBar);

        stats = ComponentMapper.getFor(StatsComponent.class);
        EntityManager.add(this);
    }

    @Override
    public void update(float deltaTime) {
        //Se o player estiver vivo atualizar a barra de hp
        if (stats.has(com.edgarsilva.pixelgame.engine.utils.managers.EntityManager.getPlayer()))

            healthBar.setValue(stats.get(EntityManager.getPlayer()).health);

        //Update HUD
        stage.act(deltaTime);

        //Defenir a viewport do HUD ao screen
        batch.setProjectionMatrix(stage.getViewport().getCamera().combined);


        //Draw HUD
        batch.begin();
        batch.draw(icon,0, stage.getHeight() - icon.getRegionHeight());
        batch.end();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }


    @Override
    public void dispose() {
        stage.dispose();
        raw.dispose();
    }
}
