package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class StatsComponent implements Component, Pool.Poolable {

    public int maxHealth = 100;
    public int health = maxHealth;


    public int armor = 10;
    public int damage = 50;
    public int magic = 10;

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
