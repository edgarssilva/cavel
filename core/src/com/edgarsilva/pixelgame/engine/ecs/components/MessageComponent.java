package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class MessageComponent implements Component, Pool.Poolable {

    public enum TutorialMessages {
        Movement, Jump, DoubleJump, Attack, FallAttack
    }

    public TutorialMessages message;
    public boolean showMessage = false;
    public float timer = 0f;
    @Override
    public void reset() {
        message = null;
        showMessage = false;
        timer = 0f;
    }

}
