package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DropperComponent implements Component, Pool.Poolable {

    public float odd = 0;
    public int tile = 0;

    public boolean droppable = true;

    public float width, height;

    public float originX, originY;


    @Override
    public void reset() {
        this.odd = this.tile = 0;
        this.droppable = false;
        this.width = this.height = this.originX = this.originY = 0f;
    }
}
