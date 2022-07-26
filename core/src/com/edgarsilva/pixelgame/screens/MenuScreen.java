package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.utils.Shake;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.managers.Save;

/**
 * Menu do Jogo
 *  Aqui é possível começar um novo jogo, ir às definições,
 *  dar login/logout e outros
 */

public class MenuScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private PixelGame game;
    private Animation anim;
    private float frame;

    private boolean loadFromServer = false;
    private float playerX = 0f;
    private float playerY = 0f;
    private float playerA = 0f;

    private Texture background;
    private Texture cavel;
    private float backgroundPos = 0f;

    private TextButton connect;
    private TextButton loadGame;

    public MenuScreen(PixelGame pixelGame) {
        this.game = pixelGame;
        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT));

        TextureRegion frame1 = new TextureRegion(game.assets.manager.get(GameAssetsManager.menuFrame1, Texture.class));
        TextureRegion frame2 = new TextureRegion(game.assets.manager.get(GameAssetsManager.menuFrame2, Texture.class));

        background = new Texture("teste.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        cavel = new Texture("Cavel.png");

        anim = new Animation<TextureRegion>( 1 / 8f, frame1, frame2);
        stage.setDebugAll(PixelGame.DEBUG);

        skin = game.assets.getSkin();


        TextButton newGame = new TextButton("New Game", skin);

        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);
        loadGame = new TextButton("Load Game", skin);


        loadGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadFromServer = Save.loadFromServer(game);
            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(PixelGame.LEVEL_SCREEN);
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(PixelGame.SETTINGS_SCREEN);
            }
        });

        exit.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        connect = new TextButton("Connect", skin);


        connect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.getPreferences().account.logedIn())
                    new Dialog("LogOut", PixelGame.assets.getSkin()){
                        @Override
                        protected void result(Object object) {
                            if ((Boolean) object){
                                update();
                                game.getPreferences().account.reset();
                                game.setScreen(PixelGame.SETTINGS_SCREEN);
                                game.setScreen(PixelGame.MENU_SCREEN);
                            }
                        }
                    }.text("Are you sure you want to Log Out !")
                            .button("Cancel", false).button("Yes", true)
                            .show(stage);
                else game.setScreen(new LoginScreen(game));
            }
        });

        TextButton shop = new TextButton("Shop", skin);

        loadGame.setTransform(true);
        loadGame.setScale(2f, 1.5f);
        loadGame.getLabel().setFontScale(1.5f, 2f);
        loadGame.setPosition(stage.getWidth() - 160f, 120f);

        connect.setTransform(true);
        connect.setScale(1.2f);
        connect.getLabel().setFontScale(1.8f);
        connect.setPosition(stage.getWidth() - 90f, stage.getHeight() - 45f);


        newGame.setTransform(true);
        newGame.setScale(3f);
        newGame.getLabel().setFontScale(1.5f);
        newGame.setPosition(stage.getWidth() - 225f, 10f);

        preferences.setTransform(true);
        preferences.setScale(2f, 1f);
        preferences.getLabel().setFontScale(1f, 2f);
        preferences.setPosition(10f, 10f);

        shop.setTransform(true);
        shop.setScale(1.5f, 1f);
        shop.getLabel().setFontScale(1f, 1.5f);
        shop.setPosition(10f, 50f);

        exit.setTransform(true);
        exit.setScale(.7f);
        exit.getLabel().setFontScale(2f);
        exit.setPosition(10, stage.getHeight() - 30f);

        stage.addActor(connect);
        stage.addActor(newGame);
        stage.addActor(preferences);
        stage.addActor(shop);
        stage.addActor(exit);
        stage.addActor(loadGame);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        game.sound.setMusic(GameAssetsManager.titleSong,true);
        skin.getFont("BitPotionExt").getData().setScale(2);
        update();
    }

    @Override
    public void render(float delta) {
        // Gdx.gl.glClearColor(33 / 255f, 38 / 255f, 63 / 255f, 1);
        Gdx.gl.glClearColor(24 / 255f, 32 / 255f, 61 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Player and Backgroud movement
        if (Shake.getShakeTimeLeft() > 0){
            Shake.tick(Gdx.graphics.getDeltaTime());
            playerX = MathUtils.lerp(playerX, Shake.getPos().x, delta);
            playerY = MathUtils.lerp(playerY, Shake.getPos().y, delta);
            playerA = MathUtils.lerp(playerA, Shake.getPos().z, delta);
        }else{
            Shake.shake(100, 400f);
        }

        backgroundPos += delta * 3000;

        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, 0, (int) backgroundPos, (int) stage.getViewport().getWorldWidth(), (int) stage.getViewport().getWorldHeight());
        stage.getBatch().draw(cavel, stage.getWidth() / 2 - cavel.getWidth() / 1.9f + playerY, stage.getHeight() - cavel.getHeight() + 30 + playerX);
        stage.getBatch().draw((TextureRegion) anim.getKeyFrame(frame += delta, true),
                playerX - 20, playerY - 70,
                stage.getWidth() / 2, stage.getHeight() / 2,
                stage.getWidth(), stage.getHeight(),
                0.8f, 0.8f,
                playerA
        );
        stage.getBatch().end();

        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();

        // Load game from server
        if (loadFromServer && PixelGame.serverSave != null) {
            loadFromServer = false;
            game.setSave(PixelGame.LOADING_SCREEN, PixelGame.serverSave);
        }
    }

    public void update(){
        if (game.getPreferences().account.logedIn()){
            stage.addActor(loadGame);
            connect.getLabel().setText("Logout");
            loadGame.addAction(Actions.show());
        }else{
            loadGame.addAction(Actions.hide());
            connect.getLabel().setText("Connect");
        }

    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
