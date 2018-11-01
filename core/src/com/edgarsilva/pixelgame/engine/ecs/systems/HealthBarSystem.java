package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.edgarsilva.pixelgame.engine.ecs.components.AttachedComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.HealthBarComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.SpriteComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;

public class HealthBarSystem extends IteratingSystem {

    ComponentMapper<StatsComponent> scm = ComponentMapper.getFor(com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent.class);
    ComponentMapper<TransformComponent> tcm = ComponentMapper.getFor(com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent.class);
    ComponentMapper<AttachedComponent>  acm = ComponentMapper.getFor(AttachedComponent.class);
    ComponentMapper<com.edgarsilva.pixelgame.engine.ecs.components.SpriteComponent> sm = ComponentMapper.getFor(com.edgarsilva.pixelgame.engine.ecs.components.SpriteComponent.class);
    //ComponentMapper<TextureComponent> texcm = ComponentMapper.getFor(TextureComponent.class);

    public HealthBarSystem() {
        super(Family.all(HealthBarComponent.class, com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent.class, AttachedComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent tc = tcm.get(entity);
        AttachedComponent  ac = acm.get(entity);
        SpriteComponent sprite = sm.get(entity);
       // TextureComponent tex = texcm.get(entity);


        StatsComponent sc = scm.get(ac.attachTo);
        TransformComponent atc = tcm.get(ac.attachTo);

        //tc.width = atc.width * (sc.health / (float) sc.maxHealth);

       // tc.position.x = atc.position.x;
        //tc.position.y = atc.position.y + atc.height; //+20
        //TODO Fazer uma condição em vez de um try catch
        try {
            sprite.sprites.get(1).setScale(RenderSystem.PixelsToMeters(sc.health / (float) sc.maxHealth), RenderSystem.PixelsToMeters(1));
        }catch (Exception ex){
            EntityManager.setToDestroy(entity);
        }
    }
}
