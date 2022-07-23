package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class BodyComponent implements Component, Pool.Poolable {

    public Body body;
    public boolean flippable = true;

    @Override
    public void reset() {
        this.body = null;
        this.flippable = true;
    }
}