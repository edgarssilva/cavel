package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgentComponent;
import com.edgarsilva.pixelgame.engine.utils.objects.CollisionComponent;

public class EnemyCollisionComponent implements CollisionComponent, Pool.Poolable {

    private ComponentMapper<EnemyAgentComponent> agentMap = ComponentMapper.getFor(EnemyAgentComponent.class);

    public int numGroundLeft  =  0;
    public int numGroundRight =  0;
    public int numWallLeft    =  0;
    public int numWallRight   =  0;
    public boolean attackPlayer = false;

    @Override
    public void handleCollision(Entity owner, Entity collider) {
       /* if (EntityManager.getPlayer().equals(collider)) {
            EnemyAgentComponent agent = agentMap.get(owner);
            agent.stateMachine.changeState(EnemyState.Attacking);
        }*/
       agentMap.get(owner).addEntityToAttack(collider);
    }

    @Override
    public void endCollision(Entity owner, Entity collider) {
        agentMap.get(owner).removeEntityToAttack(collider);
    }

    @Override
    public void reset() {
        numGroundLeft  = 0;
        numGroundRight = 0;
        numWallLeft    = 0;
        numWallRight   = 0;
    }
}
