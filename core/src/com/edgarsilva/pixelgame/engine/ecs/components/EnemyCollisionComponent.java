package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgentComponent;
import com.edgarsilva.pixelgame.engine.utils.objects.CollisionComponent;

public class EnemyCollisionComponent implements CollisionComponent, Pool.Poolable {

    private ComponentMapper<EnemyAgentComponent> agentMap = ComponentMapper.getFor(EnemyAgentComponent.class);

    @Override
    public void handleCollision(Entity owner, Entity collider) {
       /* if (EntityManager.getPlayer().equals(collider)) {
            EnemyAgentComponent agent = agentMap.get(owner);
            agent.stateMachine.changeState(EnemyState.Attacking);
        }*/
    }

    @Override
    public void reset() {

    }
}
