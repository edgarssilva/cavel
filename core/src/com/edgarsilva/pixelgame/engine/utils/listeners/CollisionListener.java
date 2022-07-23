package com.edgarsilva.pixelgame.engine.utils.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.edgarsilva.pixelgame.engine.ecs.components.AttackCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.EnemyCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.PlayerCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TypeComponent;
import com.edgarsilva.pixelgame.engine.utils.PhysicsConstants;

public class CollisionListener implements ContactListener {

    private ComponentMapper<PlayerCollisionComponent> sensorMap;
    private ComponentMapper<EnemyCollisionComponent> enemyMap;
    private ComponentMapper<TypeComponent> typeMap;
    private ComponentMapper<AttackCollisionComponent> acm;

    public CollisionListener() {
        sensorMap = ComponentMapper.getFor(PlayerCollisionComponent.class);
        typeMap = ComponentMapper.getFor(TypeComponent.class);
        acm = ComponentMapper.getFor(AttackCollisionComponent.class);
        enemyMap = ComponentMapper.getFor(EnemyCollisionComponent.class);
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
/*
        if(fa.getBody().getUserData() instanceof Entity){
            Entity ent = (Entity) fa.getBody().getUserData();
            entityCollision(ent,fb);
        }else if(fb.getBody().getUserData() instanceof Entity){
            Entity ent = (Entity) fb.getBody().getUserData();
            entityCollision(ent,fa);
        }

*/

        Entity actorA = (Entity) fa.getUserData();
        Entity actorB = (Entity) fb.getUserData();




        if (actorA == null || actorB == null) return;

        if (fa.getFilterData().categoryBits == PhysicsConstants.ATTACK_SENSOR && fb.getFilterData().categoryBits == PhysicsConstants.ENEMY_BITS) {
            acm.get(actorA).handleCollision(actorA, actorB);
        } else if (fb.getFilterData().categoryBits == PhysicsConstants.ATTACK_SENSOR && fa.getFilterData().categoryBits == PhysicsConstants.ENEMY_BITS) {
            acm.get(actorB).handleCollision(actorB, actorA);
        }


        if (sensorMap.has(actorA) || sensorMap.has(actorB)) {

            PlayerCollisionComponent data = null;
            short categoryBits = 0;

            if (fa.isSensor() && fb.getFilterData().categoryBits == PhysicsConstants.LEVEL_BITS) {
                data = sensorMap.get(actorA);
                categoryBits = fa.getFilterData().categoryBits;
            } else if (fb.isSensor() && fa.getFilterData().categoryBits == PhysicsConstants.LEVEL_BITS) {
                data = sensorMap.get(actorB);
                categoryBits = fb.getFilterData().categoryBits;
            }


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

    /* private void entityCollision(Entity ent, Fixture fb) {
         if(fb.getBody().getUserData() instanceof Entity) {
             Entity colEnt = (Entity) fb.getBody().getUserData();

             if (fb.getFilterData().categoryBits == PhysicsConstants.ENEMY_BITS) {
                 System.out.println("Enemy");
                 AttackCollisionComponent acc = ent.getComponent(AttackCollisionComponent.class);
                 if (acc != null) {
                     System.out.println("Listener AttackTask");
                     acc.handleCollision( colEnt);
                 } else {
                     System.out.println("Null");
                 }
             }

         /*    CollisionComponent col = ent.getComponent(CollisionComponent.class);
             CollisionComponent colb = colEnt.getComponent(CollisionComponent.class);



             //TODO Make entities collision dynamic using ashley ECS
             //Check for collisions

             //Collision with the player
             if(ent == EntityManager.getPlayer()){
                 Entity player = pc != null ? ent : colEnt;

                 //Check if player got hit by an drop
                 DropComponent dc = ent.getComponent(DropComponent.class);
                 DropComponent DC = colEnt.getComponent(DropComponent.class);

                 if (dc != null || DC != null){
                     System.out.println("Got hit by an Drop");
                     StatsComponent stats = player.getComponent(StatsComponent.class);
                     stats.health -= 50;
                 }

             }else {


                 //Check if an melee attack has hit an enemy
                 MeleeAttackComponent ac = ent.getComponent(MeleeAttackComponent.class);
                 MeleeAttackComponent AC = colEnt.getComponent(MeleeAttackComponent.class);

                 if (ac != null || AC != null) {
                     MeleeAttackComponent attack = ac != null ? ac : AC;

                     //Check if has hit an enemy
                     EnemyComponent ec = ent.getComponent(EnemyComponent.class);
                     EnemyComponent EC = colEnt.getComponent(EnemyComponent.class);

                     if (ec != null || EC != null) {
                         System.out.println("Attacked an Enemy");
                         StatsComponent enemy = attack == ac ? colEnt.getComponent(StatsComponent.class) : ent.getComponent(StatsComponent.class);
                         enemy.health -= attack.damage - enemy.armor;
                     }else{
                         //Check if has hit an drop
                         DropComponent dc = ent.getComponent(DropComponent.class);
                         DropComponent DC = colEnt.getComponent(DropComponent.class);

                         if (dc != null || DC != null){
                             Entity drop = dc != null ? ent : colEnt;
                             System.out.println("Destroyed an Drop");
                             //TODO Make a reset dropper method in the DropComponent also usable in the system
                             EntityManager.setToDestroy(drop);
                         }
                     }

                 } else {

                     //Check other collisions
                 }
             }
             *//*
        }
    }
*/
    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();


        Entity actorA = (Entity) fa.getUserData();
        Entity actorB = (Entity) fb.getUserData();

        if (actorA == null || actorB == null) return;

        if (sensorMap.has(actorA) || sensorMap.has(actorB)) {
            PlayerCollisionComponent data = null;
            short categoryBits = 0;

            if (fa.isSensor() && fb.getFilterData().categoryBits == PhysicsConstants.LEVEL_BITS) {
                data = sensorMap.get(actorA);
                categoryBits = fa.getFilterData().categoryBits;
            } else if (fb.isSensor() && fa.getFilterData().categoryBits == PhysicsConstants.LEVEL_BITS) {
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
                enemyMap.get(actorA).handleCollision(actorA, actorB);
            else if (fb.getFilterData().categoryBits == PhysicsConstants.ENEMY_ATTACK_SENSOR)
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
