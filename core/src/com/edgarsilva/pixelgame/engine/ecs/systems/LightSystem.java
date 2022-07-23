package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.edgarsilva.pixelgame.engine.ecs.components.LightComponent;


public class LightSystem extends IteratingSystem {

    private ComponentMapper<LightComponent> lightMap;

    public LightSystem() {
        super(Family.all(LightComponent.class).get());
        lightMap = ComponentMapper.getFor(LightComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        lightMap.get(entity).light.update();

    }
}
