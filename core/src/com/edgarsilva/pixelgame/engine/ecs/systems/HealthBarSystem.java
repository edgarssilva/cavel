package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.edgarsilva.pixelgame.engine.ecs.components.HealthBarComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.PlayerCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;

public class HealthBarSystem extends IteratingSystem {

    private ComponentMapper<StatsComponent>     scm;
    private ComponentMapper<HealthBarComponent> hcm;

    public HealthBarSystem() {
        super(Family.all(HealthBarComponent.class, TransformComponent.class, StatsComponent.class).exclude(PlayerCollisionComponent.class).get());

        scm = ComponentMapper.getFor(StatsComponent.class);
        hcm = ComponentMapper.getFor(HealthBarComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StatsComponent     sc = scm.get(entity);
        HealthBarComponent hc = hcm.get(entity);

        int damaged = hc.previousHealth - sc.health;
        hc.show = hc.scale < 1;
        hc.previousHealth = sc.health;
        hc.scale = (sc.health / (float) sc.maxHealth);
        hc.damagedHealth = damaged > 0 ? hc.scale + (damaged / (float) sc.maxHealth) : hc.damagedHealth - deltaTime;
        hc.damagedHealth = hc.damagedHealth < 0 ? 0 : hc.damagedHealth;
    }
}
