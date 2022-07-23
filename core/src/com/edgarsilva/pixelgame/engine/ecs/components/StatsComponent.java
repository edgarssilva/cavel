package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class StatsComponent implements Component, Pool.Poolable {

    public short maxHealth = 100;
    public short health = maxHealth;


    public short armor = 10;
    public short damage = 50;
    public short magic = 10;

    @Override
    public void reset() {
        this.maxHealth = this.health = 100;
        this.armor =  10;
        this.damage = 50;
        this.magic =  10;
    }

    public void attack(StatsComponent playerStats) {
        this.health -= playerStats.damage;
    }
}
