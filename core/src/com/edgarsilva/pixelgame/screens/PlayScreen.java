package com.edgarsilva.pixelgame.screens;


import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.ai.pfa.GraphPathImp;
import com.edgarsilva.pixelgame.engine.ai.pfa.HeuristicImp;
import com.edgarsilva.pixelgame.engine.ai.pfa.Node;
import com.edgarsilva.pixelgame.engine.ai.pfa.PathfindingDebugger;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.utils.controllers.Controller;
import com.edgarsilva.pixelgame.engine.utils.controllers.KeyboardController;
import com.edgarsilva.pixelgame.engine.utils.controllers.OnScreenController;
import com.edgarsilva.pixelgame.engine.utils.factories.BodyFactory;
import com.edgarsilva.pixelgame.engine.utils.factories.EntitiesFactory;
import com.edgarsilva.pixelgame.engine.utils.factories.LevelFactory;
import com.edgarsilva.pixelgame.engine.utils.generators.BodyGenerator;
import com.edgarsilva.pixelgame.engine.utils.listeners.CollisionListener;
import com.edgarsilva.pixelgame.engine.utils.managers.CameraManager;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.engine.utils.managers.HUDManager;
import com.edgarsilva.pixelgame.engine.utils.managers.LevelManager;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.managers.SoundManager;

public class PlayScreen implements Screen {

    private static PixelGame game;

    //Graphics
    private HUDManager hud;
    private SpriteBatch batch;
    private CameraManager cameraManager;

    //Physics
    private World world;

    //ECS
    private PooledEngine engine;
    private EntityManager entityManager;

    //Utils

    private Controller controller;


    //Test
    GraphPathImp resultPath;
    TransformComponent tc;

    Vector3 mouse = new Vector3();

    public PlayScreen(PixelGame game) {
        PlayScreen.game = game;


        world = new World(new Vector2(0, -9.6f), true);
        world.setContactListener(new CollisionListener());


        cameraManager = new CameraManager();
        batch = new SpriteBatch();

        engine = new PooledEngine();
        entityManager = new EntityManager(this);

 //		Gdx.input.setCursorCatched(true); // remove mouse cursor

        hud = new HUDManager("skin/glassy-ui.json", this);

        //Graphics
        cameraManager = new CameraManager();
        //hud = new HUDManager("skin/glassy-ui.json", this);

        //Engine
        engine =  new PooledEngine();
        entityManager = new EntityManager(this);


        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            controller = new KeyboardController();
        else if (Gdx.app.getType() == Application.ApplicationType.Android)
            controller = new OnScreenController(this);



        new BodyFactory(world);
        BodyGenerator.registerWorld(world);
        new EntitiesFactory(this);
        new LevelFactory(this);

        LevelManager.loadLevel("Cave");
        LevelFactory.makeEntities(LevelManager.tiledMap,"Entities");
        LevelFactory.createPhysics(LevelManager.tiledMap,"Collisions");
       // LevelFactory.makeObstacles(LevelManager.tiledMap,"Obstacles");


        // EntityManager.add(hud);


        //Try code here

        PathfindingDebugger.setCamera(cameraManager.getCamera());
        tc = EntityManager.getPlayer().getComponent(TransformComponent.class);
        resultPath = new GraphPathImp();
    }


    @Override
    public void show() {
        SoundManager.setMusic(GameAssetsManager.level1, true);
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(38/255f,32/255f,51/255f,1);
        //Gdx.gl.glClearColor(33/255f,38/255f,63/255f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);



        batch.setProjectionMatrix(cameraManager.getCamera().combined);
        cameraManager.update(delta);

        cameraManager.update(delta);
        batch.setProjectionMatrix(cameraManager.getCamera().combined);

        LevelManager.renderer.setView(cameraManager.getCamera());
        LevelManager.renderer.render();

        GdxAI.getTimepiece().update(delta);
        entityManager.update(delta);
        // hud.update(delta);


        Gdx.graphics.setTitle("Pixel Game " + Gdx.graphics.getFramesPerSecond());

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {

            //Test Code
            if (resultPath.getCount() >= 1) {
                resultPath.removeIndex(0);
            }

            Node start = LevelManager.graph.getNodeByXY(
                    (int) (tc.position.x * RenderSystem.PPM),
                    (int) (tc.position.y * RenderSystem.PPM)
            );

            mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cameraManager.getCamera().unproject(mouse).scl(RenderSystem.PPM);

            Node end = LevelManager.graph.getNodeByXY(
                    (int) mouse.x,
                    (int) (tc.position.y * RenderSystem.PPM) - LevelManager.tilePixelHeight
            );

            resultPath.clear();
            LevelManager.pathFinder.searchNodePath(start, end, new HeuristicImp(), resultPath);
            try {
                resultPath.removeIndex(0);
            } catch (Exception e) {
            }
        }
        PathfindingDebugger.drawPath(resultPath);

    }

    @Override
    public void resize(int width, int height) {
        cameraManager.resize(width, height, false);
        hud.resize(width, height);
        if (Gdx.app.getType() == Application.ApplicationType.Android)
            ((OnScreenController) controller).resize(width, height);

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
        hud.dispose();
        batch.dispose();
        LevelManager.dispose();
        world.dispose();
        engine.clearPools();
    }

    public static void gameOver() {
        System.out.println("Game Over");
        //Set to game over screen
        game.changeScreen(PixelGame.ENDGAME);
    }


    //Getters and Setters
    public PooledEngine getEngine() {
        return engine;
    }
    public World getWorld() {
        return world;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }
    public SpriteBatch getBatch() {
        return batch;
    }
    public PixelGame getGame() {
        return game;
    }
}
