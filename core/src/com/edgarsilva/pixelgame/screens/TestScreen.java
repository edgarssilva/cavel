package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TestScreen implements Screen {
    Viewport viewport;
    Stage stage;


    public TestScreen() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, new SpriteBatch());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.left().bottom();

        Image upImage = new Image(new Texture("hud/flatDark/flatDark25.png"));
        Image downImage = new Image(new Texture("hud/flatDark/flatDark26.png"));
        Image leftImage = new Image(new Texture("hud/flatDark/flatDark23.png"));
        Image rightImage = new Image(new Texture("hud/flatDark/flatDark24.png"));

        upImage.setSize(50, 50);
        downImage.setSize(50, 50);
        rightImage.setSize(50, 50);
        leftImage.setSize(50, 50);
/*
        upImage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                up = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                up = false;
                return;
            }
        });

        downImage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                down = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                down = false;
                return ;
            }
        });

        leftImage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                left = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                left = false;
                return ;
            }
        });

        rightImage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                right = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                right = false;
                return ;
            }
        });
*/
        table.add();
        table.add(upImage).size(upImage.getImageWidth(), upImage.getImageHeight());
        table.add();
        table.row().pad(5, 5, 5, 5);
        table.add(leftImage).size(leftImage.getImageWidth(), leftImage.getImageHeight());
        table.add();
        table.add(rightImage).size(rightImage.getImageWidth(), rightImage.getImageHeight());
        table.row().padBottom(5);
        table.add();
        table.add(downImage).size(downImage.getImageWidth(), downImage.getImageHeight());
        table.add();

        stage.addActor(table);
    }


    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }


    private OrthographicCamera camera;

    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(38/255f,32/255f,51/255f,1);

        stage.draw();
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

}
