package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.fsm.State;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgentComponent;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAgent;
import com.edgarsilva.pixelgame.engine.ecs.components.AnimationComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TextureComponent;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;


public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<TextureComponent> tm;
    private ComponentMapper<AnimationComponent> am;
    private ComponentMapper<EnemyAgentComponent> agentmap;

    public AnimationSystem(){
        super(Family.all(TextureComponent.class, AnimationComponent.class).get());

        tm = ComponentMapper.getFor(TextureComponent.class);
        am = ComponentMapper.getFor(AnimationComponent.class);
        agentmap = ComponentMapper.getFor(EnemyAgentComponent.class);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //deltaTime = Gdx.graphics.getDeltaTime();

        AnimationComponent ani = am.get(entity);
        TextureComponent tex = tm.get(entity);


        if (agentmap.has(entity)) {
            EnemyAgentComponent state = agentmap.get(entity);
            if (ani.animations.containsKey(state.stateMachine.getCurrentState())) {

                tex.region = ani.animations.get(state.stateMachine.getCurrentState()).getKeyFrame(state.timer);
                state.anim = ani.animations.get(state.stateMachine.getCurrentState());
                state.timer += deltaTime;
               // System.out.println(state.timer + deltaTime);
            }

        }else{
            if (entity != EntityManager.getPlayer()) return;
            State animState;

            if (PlayerAgent.attacking) {
                animState = PlayerAgent.getAttackState();
            } else
                animState = PlayerAgent.getCurrentState();

            PlayerAgent.finishedAnimation = ani.animations.get(animState).isAnimationFinished(PlayerAgent.timer);

            tex.region = ani.animations.get(animState).getKeyFrame(PlayerAgent.timer);
            PlayerAgent.timer += deltaTime;
        }

    }
}
