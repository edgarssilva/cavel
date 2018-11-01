package com.edgarsilva.pixelgame.engine.utils.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;

/**
 * Classe responsável pela camera e a viewport.
 *
 * @author Edgar Silva
 */

public class CameraManager  implements Updateable {

    private Viewport viewport;
    private OrthographicCamera camera;


    public static final float defaultZoom = 0.3f;
    private boolean debugCamera = false;

    private Vector3 position = new Vector3();

    private float viewPortHalfWidth;
    private float viewPortHalfHeight;

    private ComponentMapper<TransformComponent> tm;

    /**
     * Criar uma instancia da classe criando uma camera e uma viewport.
     */
    public CameraManager() {
        camera = new OrthographicCamera(RenderSystem.FRUSTUM_WIDTH, RenderSystem.FRUSTUM_HEIGHT);
        camera.zoom = defaultZoom;
        camera.position.set(RenderSystem.FRUSTUM_WIDTH / 2 * defaultZoom, RenderSystem.FRUSTUM_HEIGHT * defaultZoom, 0);

        viewport = new FitViewport(RenderSystem.FRUSTUM_WIDTH, RenderSystem.FRUSTUM_HEIGHT, camera);
        viewport.apply();
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        TransformComponent tc = tm.get(EntityManager.getPlayer());

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            camera.zoom -= 0.15 * deltaTime;
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            camera.zoom += 0.15 * deltaTime;

        viewPortHalfWidth  =  camera.viewportWidth  * camera.zoom * 0.5f;
        viewPortHalfHeight =  camera.viewportHeight * camera.zoom * 0.5f;

        position.x = MathUtils.clamp(tc.position.x, viewPortHalfWidth, LevelManager.lvlMeterWidth - viewPortHalfWidth);
        position.y = MathUtils.clamp(tc.position.y, viewPortHalfHeight, LevelManager.lvlMeterHeight - viewPortHalfHeight);

        camera.position.x = MathUtils.lerp(camera.position.x, position.x, 5f * deltaTime);
        camera.position.y = MathUtils.lerp(camera.position.y, position.y, 2.5f * deltaTime);

        camera.update();
    }

    public void resize(int screenWidth, int screenHeight, boolean b) {
        viewport.update(screenWidth, screenHeight, b);
    }

    /**
     * Metodo para returnar a camera.
     *
     * @return Uma instancia da OrthographicCamera.
     */
    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * Metodo para returnar a viewport.
     *
     * @return Uma instancia da Viewport(FitViewport).
     */
    public Viewport getViewport() {
        return viewport;
    }

}
