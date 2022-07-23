package com.edgarsilva.pixelgame.engine.ecs.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.BossComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.CoinComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.DropComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.DropperComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.LightComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.CoinSystem;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public class EntitiesListener implements EntityListener {

    private PlayScreen screen;

    private ComponentMapper<BodyComponent>  bcm  = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<DropComponent>  dcm  = ComponentMapper.getFor(DropComponent.class);
    private ComponentMapper<CoinComponent>  ccm  = ComponentMapper.getFor(CoinComponent.class);
    private ComponentMapper<LightComponent> lcm  = ComponentMapper.getFor(LightComponent.class);
    private ComponentMapper<BossComponent>  bscm = ComponentMapper.getFor(BossComponent.class);

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
        if (ccm.has(entity)) {
            PlayScreen.getGame().sound.playSound(GameAssetsManager.coinSound);
            CoinSystem.coins += ccm.get(entity).value;
        }
        if (lcm.has(entity)) lcm.get(entity).light.remove(true);
        if (bscm.has(entity)) PlayScreen.levelComplete();
    }
}
