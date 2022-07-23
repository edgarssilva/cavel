package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgentComponent;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.engine.utils.objects.CollisionComponent;

public class AttackCollisionComponent implements CollisionComponent, Pool.Poolable {

  //  private ComponentMapper<StatsComponent> statsCompMap = ComponentMapper.getFor(StatsComponent.class);
   // private ComponentMapper<AttachedComponent> attachCompMap = ComponentMapper.getFor(AttachedComponent.class);
    private ComponentMapper<EnemyAgentComponent> enemyCompMap = ComponentMapper.getFor(EnemyAgentComponent.class);

    @Override
    public void reset() {

    }

    @Override
    public void handleCollision(Entity owner, Entity collider) {
        if (enemyCompMap.has(collider)) {
            enemyCompMap.get(collider).hit();
        }else {
            EntityManager.setToDestroy(collider);
        }
    }

    @Override
    public void endCollision(Entity owner, Entity collider) {

    }
}
