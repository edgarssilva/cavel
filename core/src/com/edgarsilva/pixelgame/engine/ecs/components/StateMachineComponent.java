package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;

public class StateMachineComponent<S extends State<Entity>> implements Component, Updateable {


    public StateMachine<Entity, S> machine;


    @Override
    public void update(float deltaTime) {
        machine.update();

    }
}
