package com.edgarsilva.pixelgame.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.edgarsilva.pixelgame.engine.ecs.components.BehaviorComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.BehaviorSystem;

public class TestScreen implements Screen {

    PooledEngine engine;
    BehaviorTree seila;
    public TestScreen() {
        engine = new PooledEngine();
        Entity entity = engine.createEntity();
        TransformComponent transComp = engine.createComponent(TransformComponent.class);
        BehaviorComponent behaviorComp = engine.createComponent(BehaviorComponent.class);
        transComp.position.set(10, 10, 0);
        BehaviorTreeParser<Entity> parser = new BehaviorTreeParser<Entity>(BehaviorTreeParser.DEBUG_HIGH);
        behaviorComp.bTree = parser.parse(Gdx.files.internal("entities/behavior/witch.tree").reader(), entity);
        entity.add(transComp).add(behaviorComp);
        engine.addEntity(entity);
        engine.addSystem(new BehaviorSystem());


        seila = entity.getComponent(BehaviorComponent.class).bTree;
        seila.addListener(new BehaviorTree.Listener() {
            @Override
            public void statusUpdated(Task task, Task.Status previousStatus) {

            }

            @Override
            public void childAdded(Task task, int index) {
                Gdx.app.log("Classe Adicionada",task.toString());
            }

        });
        seila.start();





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
        engine.update(delta);
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
