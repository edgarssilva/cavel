package com.edgarsilva.pixelgame.engine.utils.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Array;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgent;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyState;
import com.edgarsilva.pixelgame.engine.ecs.listeners.EntitiesListener;
import com.edgarsilva.pixelgame.engine.ecs.systems.AnimationSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.AttachedSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.CoinSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.DeathSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.DropperSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.HealthBarSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.LightSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.MessageSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.PhysicsDebugSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.PhysicsSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.ecs.systems.StateAnimationSystem;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public class EntityManager {

    private static Entity player;
    private static PooledEngine engine;

    private static ComponentMapper<EnemyAgent> enemyAgentComp = ComponentMapper.getFor(EnemyAgent.class);

    public EntityManager(PlayScreen screen) {
        engine = screen.getEngine();

        //Visual Systems
        engine.addSystem(new StateAnimationSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new RenderSystem(screen.getBatch(), screen.getCameraManager().getCamera()));
        engine.addSystem(new HealthBarSystem());
        engine.addSystem(new MessageSystem(screen));


        //Physics Systems
        engine.addSystem(new PhysicsSystem(screen.getWorld()));
        engine.addSystem(new LightSystem());
        if (PixelGame.DEBUG)
            engine.addSystem(new PhysicsDebugSystem(screen.getWorld(), screen.getCameraManager().getCamera()));
        engine.addSystem(new AttachedSystem());


        //Map Objects Systems
        engine.addSystem(new DropperSystem());
        engine.addSystem(new DeathSystem());
        engine.addSystem(new CoinSystem());

        //Listeners
        engine.addEntityListener(new EntitiesListener(screen));
    }



    public void update(float deltaTime) {
        engine.update(deltaTime);
        for (Updateable agent : updateables) agent.update(deltaTime);

        for (Entity entity : destroyEntities)
            try {
                engine.removeEntity(entity);
            } catch (Exception ex) {System.err.println(ex);}

        destroyEntities.clear();
    }

    public static void destroyAllEnemies(){
        for (Entity entity : engine.getEntitiesFor(Family.all(EnemyAgent.class).get())) {
            enemyAgentComp.get(entity).stateMachine.changeState(EnemyState.Dying);
        }

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
