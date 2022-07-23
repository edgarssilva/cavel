package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.edgarsilva.pixelgame.engine.ecs.components.BehaviorComponent;

public class BehaviorSystem extends IteratingSystem {
    ComponentMapper<BehaviorComponent> behaviorMap = ComponentMapper.getFor(BehaviorComponent.class);
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        behaviorMap.get(entity).bTree.step();
    }

    public BehaviorSystem() {
        super(Family.all(BehaviorComponent.class).get());
    }
}
