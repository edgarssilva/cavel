package com.edgarsilva.pixelgame.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.edgarsilva.pixelgame.engine.ecs.components.BehaviorComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.BehaviorSystem;

public class TestScreen implements Screen {

    PooledEngine engine;
    BehaviorTree seila;
    TransformComponent transCompo;
    ShapeRenderer render;
    OrthographicCamera camera;
    TransformComponent transAi;
    public static Entity player;

    public TestScreen() {
        engine = new PooledEngine();
        Entity entity = engine.createEntity();
        transCompo = engine.createComponent(TransformComponent.class);
        transCompo.position.set(-300, -10, 0);
        transCompo.width=50;
        transCompo.height=50;
        entity.add(transCompo);
        engine.addEntity(entity);
        engine.addSystem(new BehaviorSystem());
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        render = new ShapeRenderer();
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
        player= entity;

        BehaviorTreeParser<Entity> parser = new BehaviorTreeParser<Entity>(BehaviorTreeParser.DEBUG_HIGH);
        Entity ai = engine.createEntity();
        transAi= engine.createComponent(TransformComponent.class);
        BehaviorComponent behaviorComp = engine.createComponent(BehaviorComponent.class);
        transAi.position.set(300, -10, 0);
        transAi.width=50;
        transAi.height=50;
        behaviorComp.bTree = parser.parse(Gdx.files.internal("").reader(), ai);
        ai.add(transAi).add(behaviorComp);
        engine.addEntity(ai);
        behaviorComp.bTree.start();

    }
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
        ;
    }



    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        input(delta);
        engine.update(delta);
        render.setProjectionMatrix(camera.combined);
        render.setColor(Color.WHITE);
        render.begin(ShapeRenderer.ShapeType.Filled);
        render.rect(transCompo.position.x,transCompo.position.y,transCompo.width,transCompo.height);
        render.setColor(Color.RED);
        render.rect(transAi.position.x,transAi.position.y,transAi.width,transAi.height);
        render.end();
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
    public void input(float delta){
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            transCompo.position.y+=200*delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            transCompo.position.y-=200*delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            transCompo.position.x+=200*delta;
        }if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            transCompo.position.x-=200*delta;
        }

    }
}
