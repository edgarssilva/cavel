package com.edgarsilva.pixelgame.engine.ai.bt.tasks;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class AttackTask extends LeafTask<Entity> {

    float cont;
    public void start() {
    }
    @Override
    public Status execute() {
        cont+= Gdx.graphics.getDeltaTime();
        System.out.println(cont);
        if(cont>=1){
            System.out.println("ATAQUEI");
            return Status.SUCCEEDED;
        }
        return Status.FAILED;
    }
    public void end() {
        super.end();
        if(getStatus()==Status.SUCCEEDED){
            cont=0;
        }
    }
    @Override
    protected Task<Entity> copyTo(Task<Entity> task) {
        return null;
    }
}
