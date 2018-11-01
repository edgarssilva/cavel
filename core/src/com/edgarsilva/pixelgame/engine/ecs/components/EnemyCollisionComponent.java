package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyState;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.engine.utils.objects.CollisionComponent;

public class EnemyCollisionComponent implements CollisionComponent, Pool.Poolable {

    private ComponentMapper<StateMachineComponent> stateComp = ComponentMapper.getFor(StateMachineComponent.class);

    @Override
    public void handleCollision(Entity owner, Entity collider) {
        if (EntityManager.getPlayer().equals(collider)) {
            StateMachineComponent state = stateComp.get(owner);
            state.machine.changeState(EnemyState.Attacking);
        }
    }

    @Override
    public void reset() {

    }
}
