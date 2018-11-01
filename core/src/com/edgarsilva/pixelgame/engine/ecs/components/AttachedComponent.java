package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class AttachedComponent implements Component, Pool.Poolable {
    public Entity attachTo;

    @Override
    public void reset() {
        this.attachTo = null;
    }

}
