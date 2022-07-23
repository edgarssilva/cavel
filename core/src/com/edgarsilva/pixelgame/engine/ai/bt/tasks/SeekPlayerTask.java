package com.edgarsilva.pixelgame.engine.ai.bt.tasks;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class SeekPlayerTask extends LeafTask<Entity> {

    @Override
    public void start() {
        System.out.println("Entrou");
    }
    @Override
    public Status execute() {
        System.out.println("Seeking Player");

    return Status.RUNNING;
    }
    public void end() {
        System.out.println("Stopped Seeking the Player");
        super.end();
    }
    @Override
    protected Task<Entity> copyTo(Task<Entity> task) {
        return null;
    }
}
