package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class StatsComponent implements Component, Pool.Poolable {

    public short maxHealth = 100;
    public short health = maxHealth;

    public short damage = 1;

    @Override
    public void reset() {
        this.maxHealth = this.health = 100;
        this.damage = 1;
    }

    public void attack(StatsComponent playerStats) {
        this.health -= playerStats.damage;
    }
}
