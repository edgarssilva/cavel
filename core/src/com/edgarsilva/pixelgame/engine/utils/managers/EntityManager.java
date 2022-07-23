package com.edgarsilva.pixelgame.engine.utils.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Array;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.ecs.listeners.EntitiesListener;
import com.edgarsilva.pixelgame.engine.ecs.systems.AnimationSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.AttachedSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.DeathSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.DropperSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.HealthBarSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.MessageSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.PhysicsDebugSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.PhysicsSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public class EntityManager {

    private static Entity player;
    private static PooledEngine engine;

    public EntityManager(PlayScreen screen) {
        engine = screen.getEngine();

        //Visual Systems
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new RenderSystem(screen.getBatch(), screen.getCameraManager().getCamera()));
        engine.addSystem(new HealthBarSystem());
        engine.addSystem(new MessageSystem(screen));

        //Physics Systems
        engine.addSystem(new PhysicsSystem(screen.getWorld()));
        if (PixelGame.DEBUG)
            engine.addSystem(new PhysicsDebugSystem(screen.getWorld(), screen.getCameraManager().getCamera()));
        engine.addSystem(new AttachedSystem());


        //Map Objects Systems
        engine.addSystem(new DropperSystem());
        engine.addSystem(new DeathSystem());

        //Listeners
        engine.addEntityListener(new EntitiesListener(screen));
    }



    public void update(float deltaTime) {
        engine.update(deltaTime);
        for (Updateable agent : updateables) agent.update(deltaTime);

        for (Entity entity : destroyEntities) engine.removeEntity(entity);
        destroyEntities.clear();
    }

    public void reset(){
        updateables.clear();
        destroyEntities.clear();
        engine.removeAllEntities();
    }

    private static Array<Entity> destroyEntities = new Array<Entity>();
    private static Array<Updateable> updateables = new Array<Updateable>();

    public static void add(Updateable agent) { updateables.add(agent); }
    public static void setToDestroy(Entity entity) {
        destroyEntities.add(entity);
    }

    //Getters and Setters
    public static void setPlayer(Entity entity) {
        player = entity;
    }
    public static Entity getPlayer() {
        return player;
    }

}
