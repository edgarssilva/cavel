package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgent;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;

public class DeathSystem extends IteratingSystem {

    private ComponentMapper<StatsComponent> sm;

    public DeathSystem() {
        super(Family.all(com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent.class).exclude(EnemyAgent.class).get(), 10);
        sm = ComponentMapper.getFor(com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StatsComponent sc = sm.get(entity);
        if(sc.health <= 0)
            ;//EntityManager.setToDestroy(entity);
    }

}
