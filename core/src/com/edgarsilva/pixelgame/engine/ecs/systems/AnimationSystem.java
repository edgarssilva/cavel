package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.fsm.State;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAgent;
import com.edgarsilva.pixelgame.engine.ecs.components.AnimationComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StateMachineComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TextureComponent;


public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<TextureComponent> tm;
    private ComponentMapper<AnimationComponent> am;
    private ComponentMapper<StateMachineComponent> smcm;

    public AnimationSystem(){
        super(Family.all(TextureComponent.class, AnimationComponent.class).get());

        tm = ComponentMapper.getFor(TextureComponent.class);
        am = ComponentMapper.getFor(AnimationComponent.class);
        smcm = ComponentMapper.getFor(StateMachineComponent.class);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        AnimationComponent ani = am.get(entity);
        TextureComponent tex = tm.get(entity);



        if (smcm.has(entity)) {
            StateMachineComponent state = smcm.get(entity);
            if (ani.animations.containsKey(state.machine.getCurrentState())) {
                tex.region = ani.animations.get(state.machine.getCurrentState()).getKeyFrame(ani.timer);
                ani.timer+=deltaTime;
            }

        }else{
            State animState;

            if (PlayerAgent.attacking) {
                animState = PlayerAgent.getAttackState();
                PlayerAgent.finishedAnimation = ani.animations.get(animState).isAnimationFinished(PlayerAgent.timer);
            } else
                animState = PlayerAgent.getCurrentState();
            tex.region = ani.animations.get(animState).getKeyFrame(PlayerAgent.timer);
            PlayerAgent.timer += deltaTime;
        }

    }
}
