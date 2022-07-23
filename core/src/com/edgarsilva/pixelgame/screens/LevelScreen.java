package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
        table.defaults().space(20);
        stage.setDebugAll(PixelGame.DEBUG);
       // table.align(Align.topLeft);
        table.center();
        FileHandle[] files = Gdx.files.internal("maps/").list("tmx");

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
        }

        TextButton back = new TextButton("Back", game.assets.getSkin());
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });
        table.row();
        table.bottom().add(back);
    }

    void setLevel(String map) {
        game.setScreen(new LoadingScreen(game, map));
        this.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.setScreen(new MenuScreen(game));
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
