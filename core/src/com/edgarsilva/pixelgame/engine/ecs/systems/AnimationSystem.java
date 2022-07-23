package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.edgarsilva.pixelgame.engine.ecs.components.AnimationComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TextureComponent;

public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<AnimationComponent> animMap;
    private ComponentMapper<TextureComponent>   textMap;

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class, TextureComponent.class).get());
        animMap = ComponentMapper.getFor(AnimationComponent.class);
        textMap = ComponentMapper.getFor(TextureComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animComp = animMap.get(entity);
        TextureComponent   textComp = textMap.get(entity);

        animComp.timer += deltaTime;
        textComp.region = animComp.animation.getKeyFrame(animComp.timer, animComp.looping);
    }
}
