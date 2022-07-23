package com.edgarsilva.pixelgame.engine.utils.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAgent;
import com.edgarsilva.pixelgame.engine.ecs.components.AttackCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.EnemyCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.MessageComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.PlayerCollisionComponent;
import com.edgarsilva.pixelgame.engine.utils.PhysicsConstants;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.engine.utils.managers.LevelManager;

public class CollisionListener implements ContactListener {

    private ComponentMapper<PlayerCollisionComponent> sensorMap;
    private ComponentMapper<EnemyCollisionComponent>  enemyMap;
    private ComponentMapper<AttackCollisionComponent> acm;
    private ComponentMapper<MessageComponent>         mcm;

    public CollisionListener() {
        sensorMap = ComponentMapper.getFor(PlayerCollisionComponent.class);
        acm       = ComponentMapper.getFor(AttackCollisionComponent.class);
        enemyMap  = ComponentMapper.getFor(EnemyCollisionComponent.class);
        mcm       = ComponentMapper.getFor(MessageComponent.class);
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();


        Entity actorA = (Entity) fa.getUserData();
        Entity actorB = (Entity) fb.getUserData();



        if (actorA == null || actorB == null) return;

        if (fa.getFilterData().categoryBits == PhysicsConstants.ATTACK_SENSOR && fb.getFilterData().categoryBits == PhysicsConstants.ENEMY_BITS) {
            acm.get(actorA).handleCollision(actorA, actorB);
        } else if (fb.getFilterData().categoryBits == PhysicsConstants.ATTACK_SENSOR && fa.getFilterData().categoryBits == PhysicsConstants.ENEMY_BITS) {
            acm.get(actorB).handleCollision(actorB, actorA);
        }


        if (sensorMap.has(actorA) || sensorMap.has(actorB)) {
            if (fa.getFilterData().categoryBits == PhysicsConstants.MESSAGE_BITS) {
                mcm.get(actorA).showMessage = true;
            } else if (fb.getFilterData().categoryBits == PhysicsConstants.MESSAGE_BITS) {
                mcm.get(actorB).showMessage = true;
            }

            if (fa.getFilterData().categoryBits == PhysicsConstants.HIDDEN_BITS) {
                fa.setSensor(true);
                LevelManager.showHiddenWalls = false;
            } else if (fb.getFilterData().categoryBits == PhysicsConstants.HIDDEN_BITS) {
                fb.setSensor(true);
                LevelManager.showHiddenWalls = false;
            }


            if (fa.getFilterData().categoryBits == PhysicsConstants.COIN_BITS) {
                EntityManager.setToDestroy(actorA);
            } else if (fb.getFilterData().categoryBits == PhysicsConstants.COIN_BITS) {
                EntityManager.setToDestroy(actorB);
            }

            PlayerCollisionComponent data = null;
            short categoryBits = 0;

            if (fa.isSensor() && (fb.getFilterData().categoryBits == PhysicsConstants.LEVEL_BITS || fb.getFilterData().categoryBits == PhysicsConstants.OBSTACLE_BITS )) {
                data = sensorMap.get(actorA);
                categoryBits = fa.getFilterData().categoryBits;
            }if (fb.isSensor() && (fa.getFilterData().categoryBits == PhysicsConstants.LEVEL_BITS || fb.getFilterData().categoryBits == PhysicsConstants.OBSTACLE_BITS )) {
                data = sensorMap.get(actorB);
                categoryBits = fb.getFilterData().categoryBits;
            }

            if (fa.getFilterData().categoryBits == PhysicsConstants.OBSTACLE_BITS ||
            fb.getFilterData().categoryBits == PhysicsConstants.OBSTACLE_BITS)
                PlayerAgent.kill();

            switch (categoryBits) {
                case PhysicsConstants.FOOT_SENSOR:
                    data.numFoot++;
                    break;
                case PhysicsConstants.RIGHT_WALL_SENSOR:
                    data.numRightWall++;
                    break;
                case PhysicsConstants.LEFT_WALL_SENSOR:
                    data.numLeftWall++;
                    break;
            }
        }


        if (enemyMap.has(actorA) || enemyMap.has(actorB)) {

            if (fa.getFilterData().categoryBits == PhysicsConstants.ENEMY_ATTACK_SENSOR  && fb.getFilterData().categoryBits == PhysicsConstants.FRIENDLY_BITS)
                enemyMap.get(actorA).handleCollision(actorA, actorB);
            else if (fb.getFilterData().categoryBits == PhysicsConstants.ENEMY_ATTACK_SENSOR && fa.getFilterData().categoryBits == PhysicsConstants.FRIENDLY_BITS)
                enemyMap.get(actorB).handleCollision(actorB, actorA);


            EnemyCollisionComponent data;
            short categoryBits;


            if (fa.isSensor() && fb.getFilterData().categoryBits == PhysicsConstants.LEVEL_BITS) {
                data = enemyMap.get(actorA);
                categoryBits = fa.getFilterData().categoryBits;
            } else if (fb.isSensor() && fa.getFilterData().categoryBits == PhysicsConstants.LEVEL_BITS) {
                data = enemyMap.get(actorB);
                categoryBits = fb.getFilterData().categoryBits;
            } else {
                return;
            }


            switch (categoryBits) {

                case PhysicsConstants.LEFT_GROUND_SENSOR:
                    data.numGroundLeft++;
                    break;
                case PhysicsConstants.RIGHT_GROUND_SENSOR:
                    data.numGroundRight++;
                    break;
                case PhysicsConstants.WALL_LEFT_SENSOR:
                    data.numWallLeft++;
                    break;
                case PhysicsConstants.WALL_RIGHT_SENSOR:
                    data.numWallRight++;
                    break;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();


        Entity actorA = (Entity) fa.getUserData();
        Entity actorB = (Entity) fb.getUserData();

        if (actorA == null || actorB == null) return;

        if (sensorMap.has(actorA) || sensorMap.has(actorB)) {


            if (fa.getFilterData().categoryBits == PhysicsConstants.MESSAGE_BITS) {
                mcm.get(actorA).showMessage = false;
            } else if (fb.getFilterData().categoryBits == PhysicsConstants.MESSAGE_BITS) {
                mcm.get(actorB).showMessage = false;
            }


            if (fa.getFilterData().categoryBits == PhysicsConstants.HIDDEN_BITS) {
              //  fa.setSensor(false);
                LevelManager.showHiddenWalls = true;
            } else if (fb.getFilterData().categoryBits == PhysicsConstants.HIDDEN_BITS) {
               // fb.setSensor(false);
                LevelManager.showHiddenWalls = true;
            }


            PlayerCollisionComponent data = null;
            short categoryBits = 0;

            if (fa.isSensor() && (fb.getFilterData().categoryBits == PhysicsConstants.LEVEL_BITS || fb.getFilterData().categoryBits == PhysicsConstants.OBSTACLE_BITS )) {
                data = sensorMap.get(actorA);
                categoryBits = fa.getFilterData().categoryBits;
            } else if (fb.isSensor() && (fa.getFilterData().categoryBits == PhysicsConstants.LEVEL_BITS || fb.getFilterData().categoryBits == PhysicsConstants.OBSTACLE_BITS )) {
                data = sensorMap.get(actorB);
                categoryBits = fb.getFilterData().categoryBits;
            }

            switch (categoryBits) {
                case PhysicsConstants.FOOT_SENSOR:
                    data.numFoot--;
                    break;
                case PhysicsConstants.RIGHT_WALL_SENSOR:
                    data.numRightWall--;
                    break;
                case PhysicsConstants.LEFT_WALL_SENSOR:
                    data.numLeftWall--;
                    break;
            }
        }

        if (enemyMap.has(actorA) || enemyMap.has(actorB)) {

            if (fa.getFilterData().categoryBits == PhysicsConstants.ENEMY_ATTACK_SENSOR)
                enemyMap.get(actorA).endCollision(actorA, actorB);
            else if (fb.getFilterData().categoryBits == PhysicsConstants.ENEMY_ATTACK_SENSOR)
                enemyMap.get(actorB).endCollision(actorB, actorA);



            EnemyCollisionComponent data;
            short categoryBits;

            if (fa.isSensor() && fb.getFilterData().categoryBits == PhysicsConstants.LEVEL_BITS) {
                data = enemyMap.get(actorA);
                categoryBits = fa.getFilterData().categoryBits;
            } else if (fb.isSensor() && fa.getFilterData().categoryBits == PhysicsConstants.LEVEL_BITS) {
                data = enemyMap.get(actorB);
                categoryBits = fb.getFilterData().categoryBits;
            } else {
                return;
            }


            switch (categoryBits) {
                case PhysicsConstants.LEFT_GROUND_SENSOR:
                    data.numGroundLeft--;
                    break;
                case PhysicsConstants.RIGHT_GROUND_SENSOR:
                    data.numGroundRight--;
                    break;
                case PhysicsConstants.WALL_LEFT_SENSOR:
                    data.numWallLeft--;
                    break;
                case PhysicsConstants.WALL_RIGHT_SENSOR:
                    data.numWallRight--;
                    break;
                case PhysicsConstants.ENEMY_ATTACK_SENSOR:
                    data.attackPlayer = false;
                    break;
            }
        }

    }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}
