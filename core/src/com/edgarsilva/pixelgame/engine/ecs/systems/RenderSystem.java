package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.ecs.comparators.ZComparator;
import com.edgarsilva.pixelgame.engine.ecs.components.SpriteComponent;
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

    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;

    public RenderSystem(SpriteBatch batch, OrthographicCamera camera) {
        super(Family.all(TransformComponent.class).one(TextureComponent.class, SpriteComponent.class).get(), new ZComparator());
        this.batch = batch;
        this.cam = camera;

        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
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

