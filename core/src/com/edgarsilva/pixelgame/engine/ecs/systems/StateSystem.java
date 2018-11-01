package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.edgarsilva.pixelgame.engine.ecs.components.StateMachineComponent;

public class StateSystem extends IteratingSystem {

    private ComponentMapper<StateMachineComponent> stateMap;

    public StateSystem() {
        super(Family.all(StateMachineComponent.class).get());
        stateMap = ComponentMapper.getFor(StateMachineComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StateMachineComponent state = stateMap.get(entity);
        state.update(deltaTime);
    }
}
