package com.edgarsilva.pixelgame.engine.ai.fsm.boss;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.physics.box2d.Body;
import com.edgarsilva.pixelgame.engine.ai.fsm.Agent;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.utils.factories.EntitiesFactory;

public class WitchAgent extends Agent {

    private Body body;
    public Entity attack;

    private StatsComponent statsComp;
    private ComponentMapper<StatsComponent> statsMap;
    public StateMachine<WitchAgent, WitchState> stateMachine;

    public WitchAgent() {}

    public WitchAgent(Entity witch) {
        this.owner = witch;

        statsMap = ComponentMapper.getFor(StatsComponent.class);
        statsComp = statsMap.get(witch);
        body = witch.getComponent(BodyComponent.class).body;
        stateMachine = new DefaultStateMachine<WitchAgent, WitchState>(this, WitchState.Attacking);
    }

    @Override
    public void update(float deltaTime) {
        stateMachine.update();
        animationState = stateMachine.getCurrentState();
    }


    public void attack(){
        attack = EntitiesFactory.createWitchAttack(owner);
    }


    @Override
    public void hit() {

    }
}
