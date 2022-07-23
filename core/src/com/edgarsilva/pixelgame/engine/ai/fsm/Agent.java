package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;

import java.util.Random;

public abstract class Agent implements Component, Updateable {

    protected Entity owner;

    public float timer = 0f;
    public State animationState;
    public float animationDuration = 0f;
    public boolean finishedAnimation = false;
    public Random random = new Random();
    public abstract void hit();
}
