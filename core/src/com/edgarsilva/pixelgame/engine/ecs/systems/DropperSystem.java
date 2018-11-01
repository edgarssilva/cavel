package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.edgarsilva.pixelgame.engine.ecs.components.DropperComponent;
import com.edgarsilva.pixelgame.engine.utils.factories.EntitiesFactory;

public class DropperSystem extends IteratingSystem {

    private ComponentMapper<DropperComponent> dcm;

    public DropperSystem() {
        super(Family.all(DropperComponent.class).get());
        dcm = ComponentMapper.getFor(DropperComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        float random = MathUtils.random(0, 50);

        DropperComponent dc = dcm.get(entity);

        float odd = dc.odd;

        if(random <= odd && dc.droppable) {
            EntitiesFactory.createDrop(dc.originX, dc.originY, dc.width, dc.height, entity);
            dc.droppable = false;
        }
    }
}
