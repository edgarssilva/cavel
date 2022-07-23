package com.edgarsilva.pixelgame.screens;


import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.edgarsilva.pixelgame.PixelGame;
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
    private ShapeRenderer shapeRenderer;

    //Physics
    private World world;

    //ECS
    private PooledEngine engine;
    private EntityManager entityManager;

    //Utils
    private Controller controller;
    private InputMultiplexer inputMultiplexer;

    //Pausing & Resting game variables
    private float   gameOverTimer;
    private float   alpha;
    private boolean paused;
    private boolean gameOver;
    private String  mapTitle;


    public PlayScreen(PixelGame game, String map) {
        this.paused        = false;
        this.gameOver      = false;
        this.gameOverTimer = 0f;
        this.alpha         = 0;
        this.mapTitle      = map;
        PlayScreen.game    = game;


        world = new World(new Vector2(0, -9.6f), true);
        world.setContactListener(new CollisionListener());

        batch          = new SpriteBatch();
        cameraManager  = new CameraManager();
        shapeRenderer  = new ShapeRenderer();

        engine         = new PooledEngine();
        entityManager  = new EntityManager(this);


        //Managers
        hud            = new HUDManager(this);
        pauseMenu      = new PauseManager(this);
        cameraManager  = new CameraManager();

        //Engine
        engine         = new PooledEngine();
        entityManager  = new EntityManager(this);


        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            controller = new KeyboardController();
        else if (Gdx.app.getType() == Application.ApplicationType.Android)
            controller = new OnScreenController(this);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(Controller.INPUT_INDEX, controller.getInputProcessor());
        inputMultiplexer.addProcessor(HUDManager.INPUT_INDEX, hud.getStage());
        Controllers.addListener(new JoystickController());

        new BodyFactory(world);
        BodyGenerator.registerWorld(world);
        new EntitiesFactory(this);
        new LevelFactory(this);

        LevelManager.loadLevel(map);
    }


    @Override
    public void show() {
        SoundManager.setMusic(GameAssetsManager.level1, true);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(33/255f,38/255f,63/255f,1);

        Gdx.graphics.setTitle("Cavel " + Gdx.graphics.getFramesPerSecond());

        //Verficar se o jogo deverá estar em pausa e se um comando está ligado
        if (!gameOver) checkChanges();

        // A definir o delta para 0 secs não há alteraçoes a fazer pois nao passou tempo desdo ultimo frame
        if (paused)  delta = 0;

        Gdx.input.setCursorCatched(!paused);

        batch.setProjectionMatrix(cameraManager.getCamera().combined);
        shapeRenderer.setProjectionMatrix(cameraManager.getCamera().combined);
        cameraManager.update(delta);

        LevelManager.renderer.setView(cameraManager.getCamera());
        LevelManager.renderer.render();

        GdxAI.getTimepiece().update(delta);
        entityManager.update(delta);
        hud.update(delta);

        if (paused) pauseMenu.render();
        if (gameOver) {

            gameOverTimer += Gdx.graphics.getDeltaTime();
            alpha += Gdx.graphics.getDeltaTime() / 1.5;
            Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
            Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setColor(0, 0, 0, alpha);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
        }
        if (gameOverTimer > 3) resetGame();
    }


    @Override
    public void resize(int width, int height) {
        cameraManager.resize(width, height, false);
        hud.resize(width, height);
        pauseMenu.resize(width, height);
        if (Gdx.app.getType() == Application.ApplicationType.Android)
            ((OnScreenController) controller).resize(width, height);
    }

    private void checkChanges(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (paused)
                inputMultiplexer.removeProcessor(PauseManager.INPUT_INDEX);
            else
                inputMultiplexer.addProcessor(PauseManager.INPUT_INDEX, pauseMenu.getStage());
            paused = !paused;
        }

        if (JoystickController.connected)
            inputMultiplexer.removeProcessor(Controller.INPUT_INDEX);
        else
            inputMultiplexer.addProcessor(Controller.INPUT_INDEX, controller.getInputProcessor());

    }

    public void gameOver() {
        this.gameOver = true;
        MessageManager.getInstance().dispatchMessage(0);
    }

    private void resetGame(){
        entityManager.reset();

        gameOver      = false;
        gameOverTimer = 0f;
        alpha         = 0f;

        EntityManager.add(hud);
        LevelManager.loadLevel(mapTitle);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        engine.removeAllEntities();
        engine.clearPools();
        hud.dispose();
        batch.dispose();
        LevelManager.dispose();
        world.dispose();
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
    public ShapeRenderer getShapeRenderer(){
        return shapeRenderer;
    }
    public static PixelGame getGame() {
        return game;
    }
}
