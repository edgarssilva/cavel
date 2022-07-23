package com.edgarsilva.pixelgame.engine.ai.bt.tasks;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.MathUtils;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.screens.TestScreen;

public class SeekPlayerTask extends LeafTask<Entity> {
    ComponentMapper<TransformComponent> cm = ComponentMapper.getFor(TransformComponent.class);
    TransformComponent player;
    TransformComponent ai;
    @Override
    public void start() {
        player = cm.get(TestScreen.player);
        ai = cm.get(getObject());
    }
    @Override
    public Status execute() {
        float difx=Math.abs(player.position.x-ai.position.x);
        float dify=Math.abs(player.position.y-ai.position.y);
        if(difx<50&&dify<50){
            return Status.SUCCEEDED;
        }else{
            ai.position.x= MathUtils.lerp(ai.position.x,player.position.x,0.02f);
            ai.position.y= MathUtils.lerp(ai.position.y,player.position.y,0.02f);
        }
        return Status.FAILED;
    }

    public void end() {
        super.end();
        if(getStatus()==Status.SUCCEEDED){
            System.out.println("Encontrou");
        }
        if(getStatus()==Status.FAILED){
            System.out.println("A Procura");
        }
    }

    @Override
    protected Task<Entity> copyTo(Task<Entity> task) {
        return null;
    }
}
