package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.managers.Save;

public class MenuScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private PixelGame game;
    private ShapeRenderer shape;
    private Animation anim;
    private float frame;
    private Texture background;

    private int bX = 0;
    private boolean loadFromServer = false;

    public MenuScreen(PixelGame pixelGame) {
        this.game = pixelGame;
        stage = new Stage(new ExtendViewport(PixelGame.WIDTH, PixelGame.HEIGHT));



        TextureAtlas atlas = new TextureAtlas("entities/sprites/Player.atlas");
        background = new Texture("raw/loading.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.addAll(
                atlas.findRegion("adventurer-run-00"),
                atlas.findRegion("adventurer-run-01"),
                atlas.findRegion("adventurer-run-02"),
                atlas.findRegion("adventurer-run-03"),
                atlas.findRegion("adventurer-run-04"),
                atlas.findRegion("adventurer-run-05"));

        anim = new Animation<TextureRegion>( 1 / 12f, frames, Animation.PlayMode.LOOP);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(PixelGame.DEBUG);
        stage.addActor(table);
        stage.setDebugAll(PixelGame.DEBUG);

        skin = game.assets.getSkin();

        BitmapFont bitPotion = game.assets.manager.get(GameAssetsManager.BitPotion, BitmapFont.class);

        bitPotion.getData().setScale(6);

        TextButton.TextButtonStyle previous = skin.get(TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(
                previous.up, previous.down, previous.checked,bitPotion);


        TextButton newGame = new TextButton("New Game", style);
        TextButton loadGame = new TextButton("Load Game", style);
        TextButton preferences = new TextButton("Preferences", style);
        TextButton exit = new TextButton("Exit", style);

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

        TextButton connect = new TextButton("Connect", style);

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
        connect.setScale(0.4f);
        connect.setOrigin(Align.right, Align.bottom);
        connect.setPosition(PixelGame.WIDTH - 150f, 20f);
        stage.addActor(connect);

        table.add(newGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(loadGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(preferences).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(exit).fillX().uniformX();
        table.row();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        game.sound.setMusic(GameAssetsManager.titleSong,true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1 / 30f));

        bX += 1000 * delta;

        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, bX, 0, (int) stage.getWidth(), (int) stage.getHeight());
        stage.getBatch().draw((TextureRegion) anim.getKeyFrame(frame += delta), 100f, 150, 180, 120);
        stage.getBatch().end();

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
