package com.edgarsilva.pixelgame.screens;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.ai.fsm.Enemies;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgentComponent;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyState;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAttackState;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerState;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.CoinSystem;
import com.edgarsilva.pixelgame.engine.utils.ColorDrawer;
import com.edgarsilva.pixelgame.engine.utils.PhysicsConstants;
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
import com.edgarsilva.pixelgame.managers.EnemySave;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.managers.PlayerSave;
import com.edgarsilva.pixelgame.managers.Save;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class PlayScreen implements Screen {

    private static PixelGame game;

    //Managers
    public HUDManager hud;
    private PauseManager pauseMenu;
    private CameraManager cameraManager;

    //Graphics
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    //Physics
    private World world;
    private RayHandler rayHandler;
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


    //Test
    private boolean light = true;
    private String map;


    public PlayScreen(PixelGame game) {
        this.paused        = false;
        this.gameOver      = false;
        this.gameOverTimer = 0f;
        this.alpha         = 0;
        PlayScreen.game    = game;

        world          = new World(new Vector2(0, -9.6f), true);
        world.setContactListener(new CollisionListener());

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(Color.DARK_GRAY);
        rayHandler.setAmbientLight(0.5f);
        //rayHandler.setBlurNum(3);
        rayHandler.setShadows(true);
        rayHandler.setCulling(true);
        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);


        Filter filter = new Filter();
        filter.maskBits = PhysicsConstants.FRIENDLY_BITS | PhysicsConstants.LEVEL_BITS | PhysicsConstants.ENEMY_BITS;

        PointLight.setGlobalContactFilter(filter);

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
    }

    public void setSave(Save save){
        resetGame();
        setMap(save.map);

        CoinSystem.current_coins = CoinSystem.coins = save.coins;

        for (PlayerSave player : save.playerSaves) {
            EntitiesFactory.createPlayer(
                    new Vector2(player.x,player.y),
                    PlayerState.valueOf(player.stateName),
                    PlayerAttackState.valueOf(player.attackStateName)
            );

            Entity entity = EntityManager.getPlayer();
            entity.add(player.stats);
            entity.getComponent(TransformComponent.class).flipX = player.flipX;
        }

        for (EnemySave enemy : save.enemySaves) {
            if (enemy.enemyTypeName.equals(Enemies.SKELETON.name())) {
                Entity entity = EntitiesFactory.createSkeleton(new Vector2(enemy.x, enemy.y));

                StatsComponent stats = entity.getComponent(StatsComponent.class);
                stats.maxHealth = enemy.stats.maxHealth;
                stats.health = enemy.stats.health;
                stats.magic = enemy.stats.magic;
                stats.armor = enemy.stats.armor;
                stats.damage = enemy.stats.damage;

                entity.getComponent(TransformComponent.class).flipX = enemy.flipX;
                entity.getComponent(EnemyAgentComponent.class).stateMachine.changeState(EnemyState.valueOf(enemy.stateName));
                entity.getComponent(EnemyAgentComponent.class).moveToLeft = enemy.moveToLeft;
            }
        }

    }

    public void setMap(String map) {
        this.map = map;
        System.out.println(map);
        LevelManager.loadLevel(map);
        LevelManager.generateLevel();
    }

    @Override
    public void show() {
        PlayScreen.getGame().sound.setMusic(GameAssetsManager.level1, true);
        Gdx.input.setInputProcessor(inputMultiplexer);
        if (EntityManager.getPlayer() == null) generateGame();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(33 / 255f, 38 / 255f, 63 / 255f, 1);

        Gdx.graphics.setTitle("Cavel " + Gdx.graphics.getFramesPerSecond());

        //Verficar se o jogo deverá estar em pausa e se um comando está ligado
        if (!gameOver) checkChanges();

        // A definir o delta para 0 secs não há alteraçoes a fazer pois nao passou tempo desdo ultimo frame
        if (paused  ) delta = 0;

        Gdx.input.setCursorCatched(!paused);
        cameraManager.update(delta);
        batch.setProjectionMatrix(cameraManager.getCamera().combined);
        shapeRenderer.setProjectionMatrix(cameraManager.getCamera().combined);
        rayHandler.setCombinedMatrix(cameraManager.getCamera());

        LevelManager.render(cameraManager.getCamera());

        GdxAI.getTimepiece().update(delta);
        entityManager.update(delta);
        if (light) rayHandler.updateAndRender();
        hud.update(delta);


        if (Gdx.app.getType() == Application.ApplicationType.Android)
            ((OnScreenController) controller).update(delta);

        if (paused) pauseMenu.render();
        if (gameOver) {
            gameOverTimer += Gdx.graphics.getDeltaTime();
            alpha += Gdx.graphics.getDeltaTime() / 1.5;
            ColorDrawer.drawColor(shapeRenderer, 0, 0, 0, alpha);
        }
        if (gameOverTimer > 3){ resetGame(); generateGame();}

    }


    @Override
    public void resize(int width, int height) {
        cameraManager.resize(width, height, false);
        hud.resize(width, height);
        rayHandler.resizeFBO(width, height);
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) light = !light;
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) resetGame();
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) new Save(this);
        }
    }

    public void gameOver() {
        this.gameOver = true;
        MessageManager.getInstance().dispatchMessage(0);
    }

    public void resetGame() {
        entityManager.reset();

        paused = false;
        gameOver = false;
        gameOverTimer = 0f;
        alpha = 0f;
    }
    public void generateGame(){
        EntityManager.add(hud);
        LevelManager.generateLevel();
        LevelManager.generateEntities();
        CoinSystem.coins =  PixelGame.coins;
    }

    public static void levelComplete(){
        PixelGame.coins = CoinSystem.coins;
        game.setMap(PixelGame.LOADING_SCREEN, "maps/2.tmx");
    }

    @Override
    public void pause() {
        paused = true;
    }

    public void unPause() {
        paused = false;
    }

    @Override
    public void resume() {
        resetGame();
    }

    @Override
    public void hide() {

    }

    public void exit(){
        entityManager.reset();
        engine.clearPools();
        LevelManager.dispose();
    }


    @Override
    public void dispose() {
        entityManager.reset();
        engine.clearPools();
        hud.dispose();
        batch.dispose();
        LevelManager.dispose();
        rayHandler.dispose();
        world.dispose();
    }


    //Getters and Setters
    public PooledEngine getEngine() {
        return engine;
    }
    public String getMap() {
        return map;
    }
    public World getWorld() {
        return world;
    }
    public RayHandler getRayHandler() {
        return rayHandler;
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
