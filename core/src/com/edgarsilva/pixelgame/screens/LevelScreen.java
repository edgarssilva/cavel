package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;

public class LevelScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private Table table;

    private PixelGame game;

    public LevelScreen(PixelGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT));
        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
        stage.setDebugAll(PixelGame.DEBUG);


        FileHandle[] files = Gdx.files.internal("maps/").list("tmx");

        for (final FileHandle file : files) {
            TextButton button = new TextButton(file.nameWithoutExtension(), skin);
            button.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println(file.path());
                    setLevel(file.path());
                }
            });
            table.add(button).width(stage.getWidth() / 5).uniform().padLeft(stage.getWidth() / 30).padRight(stage.getWidth() / 30);

        }

    }

    public void setLevel(String map) {
        game.setScreen(new PlayScreen(game, map));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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
