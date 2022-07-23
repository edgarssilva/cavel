package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.utils.Shake;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.managers.Save;

public class MenuScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private PixelGame game;
    private ShapeRenderer shape;
    private Animation anim;
    private float frame;

    private boolean loadFromServer = false;
    private float playerX = 0f;
    private float playerY = 0f;
    private float playerA = 0f;

    public MenuScreen(PixelGame pixelGame) {
        this.game = pixelGame;
        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT));

        TextureRegion frame1 = new TextureRegion(game.assets.manager.get(GameAssetsManager.menuFrame1, Texture.class));
        TextureRegion frame2 = new TextureRegion(game.assets.manager.get(GameAssetsManager.menuFrame2, Texture.class));


        anim = new Animation<TextureRegion>( 1 / 8f, frame1, frame2);
        stage.setDebugAll(PixelGame.DEBUG);

        skin = game.assets.getSkin();


        TextButton newGame = new TextButton("New Game", skin);
        TextButton loadGame = new TextButton("Load Game", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(PixelGame.LEVEL_SCREEN);
            }
        });

        loadGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Save.loadFromServer();
                loadFromServer = true;
                //if (Save.loadFromServer()) {
                //  while (PixelGame.serverSave == null) System.out.println(PixelGame.serverSave);
                //    game.setSave(PixelGame.LOADING_SCREEN, Save.load(PixelGame.serverSave));
                // }
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

        TextButton connect = new TextButton("Connect", skin);

        connect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                /*LoginManager.login(
                        Base64Coder.encodeString("admin"),
                        Base64Coder.encodeString("admin"),
                        LoginManager.DEFAULT_LISTENER);*/
                game.setScreen(new LoginScreen(game));
            }
        });

        connect.setTransform(true);
        connect.setScale(1.2f);
        connect.getLabel().setFontScale(2f);
        connect.setPosition(stage.getWidth() - 90f, stage.getHeight() - 45f);


        newGame.setTransform(true);
        newGame.setScale(3f);
        newGame.getLabel().setFontScale(1.5f);
        newGame.setPosition(stage.getWidth() - 225f, 10f);

        preferences.setTransform(true);
        preferences.setScale(2f, 1f);
        preferences.getLabel().setFontScale(1f, 2f);
        preferences.setPosition(10f, 10f);

        exit.setTransform(true);
        exit.setScale(.7f);
        exit.getLabel().setFontScale(2f);
        exit.setPosition(10, stage.getHeight() - 30f);

        stage.addActor(connect);
        stage.addActor(newGame);
        stage.addActor(preferences);
        stage.addActor(exit);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        game.sound.setMusic(GameAssetsManager.titleSong,true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(33 / 255f, 38 / 255f, 63 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Shake.getShakeTimeLeft() > 0){
            Shake.tick(Gdx.graphics.getDeltaTime());
            playerX = MathUtils.lerp(playerX, Shake.getPos().x, delta);
            playerY = MathUtils.lerp(playerY, Shake.getPos().y, delta);
            playerA = MathUtils.lerp(playerA, Shake.getPos().z, delta);
        }else{
            Shake.shake(100, 300f);
        }

        stage.getBatch().begin();
        stage.getBatch().draw((TextureRegion) anim.getKeyFrame(frame += delta, true),
                playerX, playerY,
                stage.getWidth() / 2, stage.getHeight() / 2,
                stage.getWidth(), stage.getHeight(),
                1, 1,
                playerA
        );
        stage.getBatch().end();

        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();

        if (loadFromServer && PixelGame.serverSave != null) {
            loadFromServer = false;
            game.setSave(PixelGame.LOADING_SCREEN, PixelGame.serverSave);
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
