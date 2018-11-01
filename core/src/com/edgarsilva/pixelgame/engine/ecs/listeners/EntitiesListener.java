package com.edgarsilva.pixelgame.engine.ecs.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.DropComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.DropperComponent;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public class EntitiesListener implements EntityListener {

    private PlayScreen screen;

    private ComponentMapper<BodyComponent> bcm = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<DropComponent> dcm = ComponentMapper.getFor(DropComponent.class);

    public EntitiesListener(PlayScreen screen) {
        this.screen = screen;
    }

    @Override
    public void entityAdded(Entity entity) {

    }


    @Override
    public void entityRemoved(Entity entity) {
        if (bcm.has(entity)) screen.getWorld().destroyBody(bcm.get(entity).body);
        if (dcm.has(entity)) dcm.get(entity).dropper.getComponent(DropperComponent.class).droppable = true;
        if (EntityManager.getPlayer() == entity) PlayScreen.gameOver();
    }
}
