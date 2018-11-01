package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.engine.utils.objects.CollisionComponent;

public class AttackCollisionComponent implements CollisionComponent, Pool.Poolable {

    private ComponentMapper<StatsComponent> statsCompMap = ComponentMapper.getFor(StatsComponent.class);
    private ComponentMapper<AttachedComponent> attachCompMap = ComponentMapper.getFor(AttachedComponent.class);

    @Override
    public void reset() {

    }

    @Override
    public void handleCollision(Entity owner, Entity collider) {
        AttachedComponent attachComp = attachCompMap.get(owner);
        Entity attached = /*attachComp.attachTo*/ EntityManager.getPlayer();
        StatsComponent playerStats = statsCompMap.get(attached);

        if (statsCompMap.has(collider)) {
            StatsComponent enemyStats = statsCompMap.get(collider);
            enemyStats.attack(playerStats);
        } else {
            EntityManager.setToDestroy(collider);
        }

    }
}
