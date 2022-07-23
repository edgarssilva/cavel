package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;

public class LevelScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private Table table;

    private PixelGame game;

    public LevelScreen(final PixelGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT));
        skin = game.assets.getSkin();


        Gdx.input.setInputProcessor(stage);

        table = new Table();
        stage.addActor(table);
        table.defaults().space(20);
        stage.setDebugAll(PixelGame.DEBUG);
       // table.center();



        TextButton level1 = new TextButton("Level 1", skin);
        level1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setLevel("maps/1.tmx");
            }
        });

        TextButton level2 = new TextButton("Level 2", skin);
        level2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setLevel("maps/2.tmx");
            }
        });

        TextButton level3 = new TextButton("Level 3", skin);
        level3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setLevel("maps/3.tmx");
            }
        });


        TextButton back = new TextButton("Back", game.assets.getSkin());
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(PixelGame.MENU_SCREEN);
            }
        });

        table.setTransform(true);
        table.setScale(2f);

        table.add(level1);
        table.add(level2);
        table.add(level3);
        table.row();
        table.add(back).center();
        table.setPosition(stage.getWidth() / 2, stage.getHeight() / 2.5f);
    }

    void setLevel(String map) {
        game.setMap(PixelGame.LOADING_SCREEN, map);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        skin.getFont("BitPotionExt").getData().setScale(2f);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.setScreen(PixelGame.MENU_SCREEN);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
        skin.dispose();
    }
}

// Codigo para ler os niveis por ficheiros
/*FileHandle[] files = Gdx.files.internal("maps/").list("tmx");

        int count = 1;
        for (final FileHandle file : files) {
            TextButton button = new TextButton(file.nameWithoutExtension(), skin);
            button.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println(file.path());
                    setLevel(file.path());
                }
            });
            //table.add(button).width(stage.getWidth() / 5).uniform().padLeft(stage.getWidth() / 30).padRight(stage.getWidth() / 30);

            table.add(button).width(stage.getWidth()/6).uniform();
            if  (count % 4 == 0)
                table.row();
            count++;
        }*/
