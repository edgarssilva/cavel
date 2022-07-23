package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.ecs.comparators.ZComparator;
import com.edgarsilva.pixelgame.engine.ecs.components.HealthBarComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TextureComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;

public class RenderSystem extends SortedIteratingSystem {

    private SpriteBatch batch;
    private Array<Entity> renderQueue;
    private OrthographicCamera cam;

    public static final float PPM = 32f;
    public static final float PIXELS_TO_METERS = 1.0f / PPM;

    public static final float FRUSTUM_WIDTH = PixelGame.WIDTH / PPM;
    public static final float FRUSTUM_HEIGHT = PixelGame.HEIGHT /PPM;

    private ComponentMapper<TextureComponent>   textureM;
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<HealthBarComponent> healthM;
    public RenderSystem(SpriteBatch batch, OrthographicCamera camera) {
        super(Family.all(TransformComponent.class).one(TextureComponent.class).get(), new ZComparator());
        this.batch = batch;
        this.cam   = camera;

        textureM    = ComponentMapper.getFor(TextureComponent.class);
        transformM  = ComponentMapper.getFor(TransformComponent.class);
        healthM     = ComponentMapper.getFor(HealthBarComponent.class);
        renderQueue = new Array<Entity>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();
        batch.begin();

        for (Entity entity : renderQueue) {

            TextureComponent tex = textureM.get(entity);
            TransformComponent t = transformM.get(entity);

            if (tex.region == null || t.isHidden) {
                continue;
            }

            float originX = t.width / 2 ;
            float originY = t.height/ 2;

            if (t.flipX != tex.region.isFlipX())  tex.region.flip(true, false);

            batch.draw(tex.region,
                    t.position.x - originX, t.position.y - originY,
                    originX, originY,
                    t.width, t.height,
                    PixelsToMeters(t.scale.x), PixelsToMeters(t.scale.y),
                    t.rotation);

            if (healthM.has(entity) && healthM.get(entity).show) {

                HealthBarComponent healthBar = healthM.get(entity);

                float scale = t.width /110f;

                float width  = PixelsToMeters(110f * scale);
                float height = PixelsToMeters( 12f * scale);

                Texture texture    = healthBar.texture;
                Texture damage     = healthBar.damage;
                Texture background = healthBar.background;

                batch.draw(background, t.position.x - width / 2, t.position.y + .35f, width, height);
                batch.draw(damage, t.position.x - width / 2, t.position.y + .35f, width * healthBar.damagedHealth, height);
                batch.draw(texture, t.position.x - width / 2, t.position.y + .35f, width * healthBar.scale, height);

            }

        }

        batch.end();
        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    public static float PixelsToMeters(float pixelValue){
        return pixelValue * PIXELS_TO_METERS;
    }

}

