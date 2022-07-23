package com.edgarsilva.pixelgame.managers;

import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;

public class EnemySave {
    public float x;
    public float y;
    public boolean flipX;
    public StatsComponent stats;
    public String stateName;
    public boolean moveToLeft;
    public String enemyTypeName;

    EnemySave() {}

    EnemySave(float x, float y, boolean flipX, StatsComponent stats, boolean moveToLeft, String stateName, String enemyTypeOrdinal) {
        this.x = x;
        this.y = y;
        this.flipX = flipX;
        this.stats = stats;
        this.stateName = stateName;
        this.moveToLeft = moveToLeft;
        this.enemyTypeName = enemyTypeOrdinal;
    }
}

