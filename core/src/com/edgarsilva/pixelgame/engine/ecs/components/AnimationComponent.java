package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class AnimationComponent implements Component, Pool.Poolable {

    public Animation<TextureRegion> animation;
    public float timer = 0f;
    public boolean looping = false;

    @Override
    public void reset() {
        animation = null;
        looping = false;
    }
}
