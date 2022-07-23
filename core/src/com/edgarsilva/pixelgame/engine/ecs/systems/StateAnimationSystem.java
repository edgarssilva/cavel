package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.edgarsilva.pixelgame.engine.ai.fsm.Agent;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgent;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAgent;
import com.edgarsilva.pixelgame.engine.ai.fsm.boss.WitchAgent;
import com.edgarsilva.pixelgame.engine.ecs.components.StateAnimationComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TextureComponent;


public class StateAnimationSystem extends IteratingSystem {

    private ComponentMapper<TextureComponent> tm;
    private ComponentMapper<StateAnimationComponent> am;
    private ComponentMapper<PlayerAgent> playerMap;
    private ComponentMapper<EnemyAgent> enemyMap;
    private ComponentMapper<WitchAgent> witchMap;

    public StateAnimationSystem(){
        super(Family.all(TextureComponent.class, StateAnimationComponent.class).get());

        tm = ComponentMapper.getFor(TextureComponent.class);
        am = ComponentMapper.getFor(StateAnimationComponent.class);
        playerMap = ComponentMapper.getFor(PlayerAgent.class);
        enemyMap = ComponentMapper.getFor(EnemyAgent.class);
        witchMap = ComponentMapper.getFor(WitchAgent.class);

    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StateAnimationComponent ani = am.get(entity);
        TextureComponent tex = tm.get(entity);

        Agent agent;

        if (playerMap.has(entity)) {
            agent = playerMap.get(entity);
        } else if (enemyMap.has(entity)) {
            agent = enemyMap.get(entity);
        } else if (witchMap.has(entity)) {
            agent = witchMap.get(entity);
        } else {
            return;
        }


        if (ani.animations.containsKey(agent.animationState)) {
            tex.region = ani.animations.get(agent.animationState).getKeyFrame(agent.timer);
            agent.animationDuration = ani.animations.get(agent.animationState).getAnimationDuration();
            agent.finishedAnimation = ani.animations.get(agent.animationState).isAnimationFinished(agent.timer);
            agent.timer += deltaTime;
        }

    }
}

