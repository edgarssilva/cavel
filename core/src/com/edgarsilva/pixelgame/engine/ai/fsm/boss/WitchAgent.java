package com.edgarsilva.pixelgame.engine.ai.fsm.boss;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.physics.box2d.Body;
import com.edgarsilva.pixelgame.engine.ai.fsm.Agent;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.utils.factories.EntitiesFactory;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public class WitchAgent extends Agent {

    public Body body;
    public Entity attack;

    private StatsComponent statsComp;
    public TransformComponent tfComp;
    private ComponentMapper<StatsComponent> statsMap;
    public StateMachine<WitchAgent, WitchState> stateMachine;

    public float lastHit = 0f;
    public boolean leftSide = true;
    public WitchAgent() {}

    public WitchAgent(Entity witch) {
        this.owner = witch;

        statsMap     = ComponentMapper.getFor(StatsComponent.class);
        statsComp    = statsMap.get(witch);
        tfComp       = witch.getComponent(TransformComponent.class);
        body         = witch.getComponent(BodyComponent.class).body;
        stateMachine = new DefaultStateMachine<WitchAgent, WitchState>(this, WitchState.Walking);
    }

    @Override
    public void update(float deltaTime) {
        if (statsComp.health > 0) {
            stateMachine.update();
            animationState = stateMachine.getCurrentState();
            lastHit += deltaTime;
        }
    }


    public void attack(){
        attack = EntitiesFactory.createWitchAttack(owner);
    }


    @Override
    public void hit() {
        if (stateMachine.isInState(WitchState.Attacking) && lastHit > 0.5f) {
            lastHit = 0f;
            statsComp.health -= 20;
            if (statsComp.health <= 0) {
                EntityManager.setToDestroy(attack);
                EntityManager.setToDestroy(owner);
                PlayScreen.levelComplete();
            }
        }
    }
}
