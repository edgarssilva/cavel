package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.math.MathUtils;

public class CoinSystem extends IntervalSystem {

    public static float current_coins = 0;
    public static int coins = 0;

    public CoinSystem() {
        super(0.05f);
    }

    @Override
    protected void updateInterval() {
        current_coins =MathUtils.lerp(current_coins, coins, 0.1f);
    }
}
