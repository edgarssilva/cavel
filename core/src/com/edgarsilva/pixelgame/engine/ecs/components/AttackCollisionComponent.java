package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.edgarsilva.pixelgame.engine.ai.fsm.Agent;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgent;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAgent;
import com.edgarsilva.pixelgame.engine.ai.fsm.boss.WitchAgent;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.engine.utils.objects.CollisionComponent;

public class AttackCollisionComponent implements CollisionComponent, Pool.Poolable {

    //  private ComponentMapper<StatsComponent> statsCompMap = ComponentMapper.getFor(StatsComponent.class);
    // private ComponentMapper<AttachedComponent> attachCompMap = ComponentMapper.getFor(AttachedComponent.class);
    private ComponentMapper<EnemyAgent>  enemyCompMap  = ComponentMapper.getFor(EnemyAgent.class);
    private ComponentMapper<WitchAgent>  witchCompMap  = ComponentMapper.getFor(WitchAgent.class);
    private ComponentMapper<PlayerAgent> playerCompMap = ComponentMapper.getFor(PlayerAgent.class);

    @Override
    public void reset() {

    }

    @Override
    public void handleCollision(Entity owner, Entity collider) {
        Agent agent = null;
        if (witchCompMap.has(collider))
            agent = witchCompMap.get(collider);
        else if (enemyCompMap.has(collider))
            agent = enemyCompMap.get(collider);
        else if(playerCompMap.has(collider))
            agent = playerCompMap.get(collider);

        if (agent != null) agent.hit();
        else  EntityManager.setToDestroy(collider);
    }

    @Override
    public void endCollision(Entity owner, Entity collider) {

    }
}
