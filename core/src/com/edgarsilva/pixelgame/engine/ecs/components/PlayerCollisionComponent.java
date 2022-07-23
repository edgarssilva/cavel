package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.edgarsilva.pixelgame.engine.utils.objects.CollisionComponent;

public class PlayerCollisionComponent implements CollisionComponent, Pool.Poolable {
    public short numFoot        = 0;
    public short numRightWall   = 0;
    public short numLeftWall    = 0;

    public PlayerCollisionComponent() {
    }

    @Override
    public void reset() {
        numFoot = 0;
        numRightWall = 0;
        numLeftWall = 0;
    }

    @Override
    public void handleCollision(Entity collider) {

    }
}
