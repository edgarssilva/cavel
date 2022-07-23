package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;
import java.util.Map;

public class StateAnimationComponent implements Component, Pool.Poolable {
    public Map<State,Animation<TextureRegion>> animations = new HashMap<State, Animation<TextureRegion>>();
    public float timer = 0f;

    public void add(State state, float frameRate, Animation.PlayMode playMode, TextureRegion ... textures) {
        Array<TextureRegion> array = Array.with(textures);
        animations.put(state, new Animation<TextureRegion>(frameRate, array, playMode));
    }

    @Override
    public void reset() {
        animations.clear();
        timer = 0f;
    }
}