package com.edgarsilva.pixelgame.managers;

import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;

public class PlayerSave {
    public float x;
    public float y;
    public boolean flipX;
    public StatsComponent stats;
    public String stateName;
    public String attackStateName;

    PlayerSave() {}

    PlayerSave(float x, float y, boolean flipX, StatsComponent stats, String stateName, String attackStateName) {
        this.x = x;
        this.y = y;
        this.flipX = flipX;
        this.stats = stats;
        this.stateName = stateName;
        this.attackStateName = attackStateName;
    }
}
