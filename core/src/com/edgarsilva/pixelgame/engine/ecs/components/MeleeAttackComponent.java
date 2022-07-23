package com.edgarsilva.pixelgame.engine.ecs.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class MeleeAttackComponent implements Component, Pool.Poolable {
    public float damage   = 115f;
    public float range    = 10.5f;
    public float duration = 1f; //Seconds
    public float timer    = 0.0f;

    @Override
    public void reset() {
        this.damage   = 115f;
        this.range    = 10.5f;
        this.duration = 1f;
        this.timer    = 0.0f;

    }
}
