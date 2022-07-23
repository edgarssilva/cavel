package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class MessageComponent implements Component, Pool.Poolable {

    public enum TutorialMessages {
        NONE, Movement, Jump, DoubleJump, Attack, FallAttack, Wall
    }

    public TutorialMessages message = TutorialMessages.NONE;
    public boolean showMessage = false;
    public float timer = 0f;
    @Override
    public void reset() {
        message = TutorialMessages.NONE;
        showMessage = false;
        timer = 0f;
    }

}
