package com.edgarsilva.pixelgame.screens;


import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.ai.pfa.GraphPathImp;
import com.edgarsilva.pixelgame.engine.ai.pfa.PathfindingDebugger;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.utils.controllers.Controller;
import com.edgarsilva.pixelgame.engine.utils.controllers.JoystickController;
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
import com.edgarsilva.pixelgame.engine.utils.managers.PauseManager;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.managers.SoundManager;

public class PlayScreen implements Screen {

    private static PixelGame game;

    //Managers
    private HUDManager hud;
    private PauseManager pauseMenu;
    private CameraManager cameraManager;

    //Graphics
    private SpriteBatch batch;
    public static ShapeRenderer shapeRenderer;


    //Physics
    private World world;

    //ECS
    private PooledEngine engine;
    private EntityManager entityManager;

    //Utils
    private Controller controller;
    private InputMultiplexer inputMultiplexer;


    public boolean paused = false;

    //Test
    public static GraphPathImp resultPath;
    TransformComponent tc;

    Vector3 mouse = new Vector3();

    public PlayScreen(PixelGame game, String map) {
        PlayScreen.game = game;

        inputMultiplexer = new InputMultiplexer();

        world = new World(new Vector2(0, -9.6f), true);
        world.setContactListener(new CollisionListener());

        cameraManager = new CameraManager();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        engine = new PooledEngine();
        entityManager = new EntityManager(this);



        //		Gdx.input.setCursorCatched(true); // remove mouse cursor

        hud = new HUDManager( this);
        pauseMenu = new PauseManager(this);

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

        inputMultiplexer.addProcessor(controller.getInputProcessor());
        inputMultiplexer.addProcessor(hud.getStage());
        Controllers.addListener(new JoystickController());

        new BodyFactory(world);
        BodyGenerator.registerWorld(world);
        new EntitiesFactory(this);
        new LevelFactory(this);

        LevelManager.loadLevel(map);
        LevelFactory.makeEntities(LevelManager.tiledMap,"Entities");
        LevelFactory.createPhysics(LevelManager.tiledMap,"Collisions");
        // LevelFactory.makeObstacles(LevelManager.tiledMap,"Obstacles");


        // EntityManager.add(hud);


        //Try code here
        PathfindingDebugger.setCamera(cameraManager.getCamera());
        tc = EntityManager.getPlayer().getComponent(TransformComponent.class);
        resultPath = new GraphPathImp();


        EntitiesFactory.createWitch(new Vector2(
                EntityManager.getPlayer().getComponent(TransformComponent.class).position.x,
                EntityManager.getPlayer().getComponent(TransformComponent.class).position.y
        ));

    }


    @Override
    public void show() {
        SoundManager.setMusic(GameAssetsManager.level1, true);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.setTitle("Cavel " + Gdx.graphics.getFramesPerSecond());

        //Parar o jogo
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (paused)
                inputMultiplexer.removeProcessor(pauseMenu.getStage());
            else
                inputMultiplexer.addProcessor(pauseMenu.getStage());
            paused = !paused;
        }

        //Verificar se o jogo está parado
        if (paused) delta = 0;

        //Gdx.gl.glClearColor(38/255f,32/255f,51/255f,1);
        //Gdx.gl.glClearColor(33/255f,38/255f,63/255f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);


        cameraManager.update(delta);
        batch.setProjectionMatrix(cameraManager.getCamera().combined);
        shapeRenderer.setProjectionMatrix(cameraManager.getCamera().combined);

        LevelManager.renderer.setView(cameraManager.getCamera());
        LevelManager.renderer.render();

        GdxAI.getTimepiece().update(delta);
        entityManager.update(delta);
        // hud.update(delta);

        //PathfindingDebugger.drawPath(resultPath);

        if (paused) {
            //Fundo preto semi-transparente
            Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
            Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setColor(0, 0, 0, 0.5f);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(Gdx.gl.GL_BLEND);


            pauseMenu.render();
        }

       /* for (com.badlogic.gdx.controllers.Controller controller : Controllers.getControllers()) {
            System.out.println(controller.getName());
        }*/

    }


    @Override
    public void resize(int width, int height) {
        cameraManager.resize(width, height, false);
        hud.resize(width, height);
        pauseMenu.resize(width, height);
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
        game.setScreen(new EndScreen(game));
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
    public static PixelGame getGame() {
        return game;
    }
}
