package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.edgarsilva.pixelgame.engine.ecs.components.AttachedComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;


public class AttachedSystem extends IteratingSystem {

    private ComponentMapper<AttachedComponent> attachCompMap = ComponentMapper.getFor(AttachedComponent.class);
    private ComponentMapper<BodyComponent> bodyCompMap = ComponentMapper.getFor(BodyComponent.class);


    public AttachedSystem() {
        super(Family.all(AttachedComponent.class, BodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AttachedComponent attachComp = attachCompMap.get(entity);
        Body body = bodyCompMap.get(entity).body;
        Body attachBody = bodyCompMap.get(attachComp.attachTo).body;

        body.setTransform(attachBody.getPosition(), 0f);
    }
}
