package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;


public class HealthBarComponent implements Component, Pool.Poolable {
    public Texture texture;
    public Texture background;
    public Texture damage;
    public float scale;
    public boolean show = false;

    public int previousHealth;
    public float damagedHealth;

    @Override
    public void reset() {
        texture = null;
        background = null;
        damage = null;
        scale = 0;
        show = false;
    }
}
