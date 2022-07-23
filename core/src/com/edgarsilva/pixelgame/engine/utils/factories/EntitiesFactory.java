package com.edgarsilva.pixelgame.engine.utils.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgentComponent;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyState;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAgent;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAttackState;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerState;
import com.edgarsilva.pixelgame.engine.ecs.components.AnimationComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.AttachedComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.AttackCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.AttackComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.DropComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.DropperComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.EnemyCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.PlayerCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TextureComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TypeComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.utils.GroundSteering;
import com.edgarsilva.pixelgame.engine.utils.PhysicsConstants;
import com.edgarsilva.pixelgame.engine.utils.generators.BodyEditorLoader;
import com.edgarsilva.pixelgame.engine.utils.generators.BodyGenerator;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.screens.PlayScreen;

/**
 * Classe responsável por criar as Entities usando métodos estáticos
 *
 * @autor: Edgar Silva
 */
public class EntitiesFactory {

    private static PooledEngine engine;
    private static GameAssetsManager assets;
    private static World world;

    private static float frameDuration = 0.175f;
    private static float fastFrameDuration = 0.100f;

    public EntitiesFactory(PlayScreen screen) {
        EntitiesFactory.engine = screen.getEngine();
        EntitiesFactory.world = screen.getWorld();
        EntitiesFactory.assets = screen.getGame().assets;
    }

    public static void createPlayer(Vector2 position){

        Entity entity = engine.createEntity();

        BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
        TransformComponent transform = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StatsComponent sc = engine.createComponent(StatsComponent.class);
        PlayerCollisionComponent sensorComp = engine.createComponent(PlayerCollisionComponent.class);
        AttackComponent attackComp = engine.createComponent(AttackComponent.class);

        //TODO Criar uma class para ler ficheiros com as stats das entities
        // create the data for the components and add them to the components
        sc.health = 100;
        sc.armor = 10;
        sc.damage = 20;
        sc.magic = 0;


        // set object position (x,y,z) z used to define draw order 0 first drawn
        transform.position.set(position.x, position.y,0);
        transform.width = 37.50f;
        transform.height = 27.75f;

        transform.paddingRight = 0f;
        transform.paddingBottom = 10f;


        /*b2dbody.body = BodyFactory.makeBox(x + 16 * RenderSystem.PIXELS_TO_METERS, y+ 16 * RenderSystem.PIXELS_TO_METERS,
                14f, 16f,
                BodyDef.BodyType.DynamicBody,
                false);
                */

        b2dbody.body = BodyGenerator.bodyHelper(entity,
                position,
                new Vector2(transform.width, transform.height),
                Gdx.files.internal("entities/bodies/player.json"),
                PhysicsConstants.FRIENDLY_BITS);


        TextureAtlas atlas = assets.manager.get(GameAssetsManager.playerAtlas, TextureAtlas.class);

        animation.add(PlayerState.Idle,
                frameDuration,
                Animation.PlayMode.LOOP,
                atlas.findRegion("adventurer-idle-00"),
                atlas.findRegion("adventurer-idle-01")
        );

        animation.add(PlayerState.Walking,
                fastFrameDuration,
                Animation.PlayMode.LOOP,
                atlas.findRegion("adventurer-run-00"),
                atlas.findRegion("adventurer-run-01"),
                atlas.findRegion("adventurer-run-02"),
                atlas.findRegion("adventurer-run-03"),
                atlas.findRegion("adventurer-run-04"),
                atlas.findRegion("adventurer-run-05")
        );

        animation.add(PlayerState.Falling,
                fastFrameDuration,
                Animation.PlayMode.NORMAL,
                atlas.findRegion("adventurer-fall-00"),
                atlas.findRegion("adventurer-fall-01")
        );

        animation.add(PlayerState.Jumping,
                fastFrameDuration,
                Animation.PlayMode.NORMAL,
                atlas.findRegion("adventurer-jump-00"),
                atlas.findRegion("adventurer-jump-01"),
                atlas.findRegion("adventurer-jump-02"),
                atlas.findRegion("adventurer-jump-03")
        );

        animation.add(PlayerState.DoubleJumping,
                fastFrameDuration,
                Animation.PlayMode.NORMAL,
                atlas.findRegion("adventurer-smrslt-00"),
                atlas.findRegion("adventurer-smrslt-01"),
                atlas.findRegion("adventurer-smrslt-02"),
                atlas.findRegion("adventurer-smrslt-03")
        );

        animation.add(PlayerAttackState.Attack1,
                fastFrameDuration,
                Animation.PlayMode.NORMAL,
                atlas.findRegion("adventurer-attack1-00"),
                atlas.findRegion("adventurer-attack1-01"),
                atlas.findRegion("adventurer-attack1-02"),
                atlas.findRegion("adventurer-attack1-03"),
                atlas.findRegion("adventurer-attack1-04")
        );

        animation.add(PlayerAttackState.Attack2,
                fastFrameDuration,
                Animation.PlayMode.NORMAL,
                atlas.findRegion("adventurer-attack2-00"),
                atlas.findRegion("adventurer-attack2-01"),
                atlas.findRegion("adventurer-attack2-02"),
                atlas.findRegion("adventurer-attack2-03"),
                atlas.findRegion("adventurer-attack2-04")
        );

        animation.add(PlayerAttackState.Attack3,
                fastFrameDuration,
                Animation.PlayMode.NORMAL,
                atlas.findRegion("adventurer-attack3-00"),
                atlas.findRegion("adventurer-attack3-01"),
                atlas.findRegion("adventurer-attack3-02"),
                atlas.findRegion("adventurer-attack3-03"),
                atlas.findRegion("adventurer-attack3-04"),
                atlas.findRegion("adventurer-attack3-05")
        );

        animation.add(PlayerAttackState.AirAttack1,
                fastFrameDuration,
                Animation.PlayMode.NORMAL,
                atlas.findRegion("adventurer-air-attack-1-00"),
                atlas.findRegion("adventurer-air-attack-1-01"),
                atlas.findRegion("adventurer-air-attack-1-02"),
                atlas.findRegion("adventurer-air-attack-1-03")
        );

        animation.add(PlayerAttackState.AirAttack2,
                fastFrameDuration,
                Animation.PlayMode.NORMAL,
                atlas.findRegion("adventurer-air-attack-2-00"),
                atlas.findRegion("adventurer-air-attack-2-01"),
                atlas.findRegion("adventurer-air-attack-2-02")
        );

        animation.add(PlayerAttackState.FallingAttack,
                fastFrameDuration,
                Animation.PlayMode.LOOP,
                atlas.findRegion("adventurer-air-attack3-loop-00"),
                atlas.findRegion("adventurer-air-attack3-loop-01")
        );

        animation.add(PlayerAttackState.FallAttack,
                fastFrameDuration,
                Animation.PlayMode.NORMAL,
                atlas.findRegion("adventurer-air-attack3-rdy-00"),
                atlas.findRegion("adventurer-air-attack-3-end-00"),
                atlas.findRegion("adventurer-air-attack-3-end-01"),
                atlas.findRegion("adventurer-air-attack-3-end-02")

        );


/*
        animation.add(PlayerAttackState.Hit,
                fastFrameDuration,
                Animation.PlayMode.IDLE,
                atlas.findRegion("adventurer-hurt-00"),
                atlas.findRegion("adventurer-hurt-01"),
                atlas.findRegion("adventurer-hurt-02")
        );

        animation.add(PlayerAttackState.Die,
                fastFrameDuration,
                Animation.PlayMode.IDLE,
                atlas.findRegion("adventurer-die-00"),
                atlas.findRegion("adventurer-die-01"),
                atlas.findRegion("adventurer-die-02"),
                atlas.findRegion("adventurer-die-03"),
                atlas.findRegion("adventurer-die-04"),
                atlas.findRegion("adventurer-die-05"),
                atlas.findRegion("adventurer-die-06")
        );
*/

        type.type = TypeComponent.PLAYER;
        // stateCom.set(PlayerStates.STATE_NORMAL);
        b2dbody.body.setUserData(entity);

        // add the components to the entity
        entity.add(b2dbody)
                .add(transform)
                .add(texture)
                .add(animation)
                .add(attackComp)
                .add(type)
                .add(sensorComp)
                .add(sc);
        // entity.add(stateCom)


        // add the entity to the engine
        engine.addEntity(entity);
        EntityManager.setPlayer(entity);
        EntityManager.add(new PlayerAgent(entity));
    }

    public static Entity createAttack(PlayerAttackState state) {
        Entity attack = engine.createEntity();
        BodyComponent bodyComp = engine.createComponent(BodyComponent.class);
        AttackCollisionComponent collComp = engine.createComponent(AttackCollisionComponent.class);
        AttachedComponent attachComp = engine.createComponent(AttachedComponent.class);


        attachComp.attachTo = EntityManager.getPlayer();

        Body body;

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 0f;
        bdef.active = true;
        body = world.createBody(bdef);


        FixtureDef f = new FixtureDef();
        //f.shape = shape;
        f.isSensor = true;
        f.filter.categoryBits = PhysicsConstants.ATTACK_SENSOR;
        f.filter.maskBits = PhysicsConstants.ENEMY_BITS;

        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("entities/bodies/attacks.json"));

        String name = null;

        switch (state) {
            case Attack1:
                name = "attack1";
                break;
            case Attack2:
                name = "attack2";
                break;
            case Attack3:
                name = "attack3";
                break;
            case AirAttack1:
                name = "airattack1";
                break;
            case AirAttack2:
                name = "airattack2";
                break;
            case FallAttack:
                name = "airattack3";
                break;
            default:
                return null;
        }

        if (EntityManager.getPlayer().getComponent(TransformComponent.class).flipX)
            loader.attachFixture(body, name, f, -1f, 1f);
        else
            loader.attachFixture(body, name, f, 1f, 1f);

        for (Fixture fix : body.getFixtureList()) {
            fix.setUserData(attack);
        }
        body.setUserData(attack);
        bodyComp.body = body;
        bodyComp.flippable = true;
        attack.add(bodyComp).add(collComp).add(attachComp);

        engine.addEntity(attack);
        return attack;
    }

    public static Entity createSkeleton(Vector2 position){
        Entity entity = engine.createEntity();

        BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
        TransformComponent transform = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        //EnemyComponent enemyCom = engine.createComponent(EnemyComponent.class);
        StatsComponent sc = engine.createComponent(StatsComponent.class);
        EnemyCollisionComponent collisionComp = engine.createComponent(EnemyCollisionComponent.class);
        EnemyAgentComponent      agentComp   =   engine.createComponent(EnemyAgentComponent.class);


        sc.health = 75;
        sc.armor = 0;
        sc.damage = 20;
        sc.magic = 0;

        Body body;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.active = true;
        bodyDef.gravityScale = 1;

        //bodyDef.gravityScale = 0.5f;
        bodyDef.position.set(position.x + (18 / 2f * RenderSystem.PIXELS_TO_METERS), position.y + (24 / 2f * RenderSystem.PIXELS_TO_METERS));
        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(18 / 2f * RenderSystem.PIXELS_TO_METERS, 24 / 2f * RenderSystem.PIXELS_TO_METERS);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = box;
        fdef.density = 1;
        fdef.filter.categoryBits = PhysicsConstants.ENEMY_BITS;
        fdef.filter.maskBits = PhysicsConstants.ATTACK_SENSOR | PhysicsConstants.LEVEL_BITS;


        body.createFixture(fdef).setUserData(entity);
        box.dispose();
        body.setUserData(entity);
        b2dbody.body = body;


        // set object position (x,y,z) z used to define draw order 0 first drawn
        transform.position.set(position.x, position.y,2);
        transform.width = 18;
        transform.height = 24;
        transform.flipX = true;
        //position.paddingBottom = 3.5f;

        TextureAtlas atlas = assets.manager.get(GameAssetsManager.skeletonAtlas, TextureAtlas.class);

        animation.add(EnemyState.IDLE, frameDuration, Animation.PlayMode.LOOP,
                atlas.findRegion("idle0"),
                atlas.findRegion("idle1"),
                atlas.findRegion("idle2"),
                atlas.findRegion("idle3"),
                atlas.findRegion("idle4"),
                atlas.findRegion("idle5"),
                atlas.findRegion("idle6"),
                atlas.findRegion("idle7"),
                atlas.findRegion("idle8"),
                atlas.findRegion("idle9"),
                atlas.findRegion("idle10")
        );

        type.type = TypeComponent.ENEMY;
        b2dbody.body.setUserData(entity);



        // add the components to the entity
        entity.add(b2dbody);
        entity.add(transform);
        entity.add(texture);
        entity.add(animation);
        entity.add(type)
                .add(sc).add(collisionComp);


        agentComp = new EnemyAgentComponent(entity, new GroundSteering(entity));
        agentComp.stateMachine = new DefaultStateMachine<EnemyAgentComponent, EnemyState>(agentComp, EnemyState.IDLE);
        EntityManager.add(agentComp);
        entity.add(agentComp);

        engine.addEntity(entity);

        return entity;
    }

    public static Entity createSlime(Vector2 position){
        Entity entity = engine.createEntity();

        AnimationComponent       animComp    =  engine.createComponent(AnimationComponent.class);
        BodyComponent            bodyComp    =  engine.createComponent(BodyComponent.class);
        EnemyCollisionComponent  collComp  =  engine.createComponent(EnemyCollisionComponent.class);

        StatsComponent           statsComp   =  engine.createComponent(StatsComponent.class);
        TextureComponent         textComp    =  engine.createComponent(TextureComponent.class);
        TransformComponent       transfComp  =  engine.createComponent(TransformComponent.class);
        TypeComponent            typeComp    =  engine.createComponent(TypeComponent.class);
        EnemyAgentComponent      agentComp   =   engine.createComponent(EnemyAgentComponent.class);



        TextureAtlas atlas = assets.manager.get(GameAssetsManager.slimeAtlas, TextureAtlas.class);

        animComp.add(EnemyState.IDLE, frameDuration, Animation.PlayMode.LOOP,
                atlas.findRegion("slime-idle-0"),
                atlas.findRegion("slime-idle-1"),
                atlas.findRegion("slime-idle-2"),
                atlas.findRegion("slime-idle-3")
        );


        bodyComp.body = BodyGenerator.bodyHelper(
                entity,
                position,
                new Vector2(16, 16),
                Gdx.files.internal("entities/bodies/slime.json"),
                PhysicsConstants.ENEMY_BITS
        );

        bodyComp.flippable = true;


        statsComp.health    = 50;
        statsComp.damage    = 10;
        statsComp.health    = 10;
        statsComp.maxHealth = 50;

        transfComp.width  = 16;
        transfComp.height = 16;
        transfComp.position.set(position.x, position.y, 2);

        typeComp.type = TypeComponent.ENEMY;


        entity.add(animComp)
                .add(bodyComp)
                .add(collComp)
                .add(statsComp)
                .add(textComp)
                .add(transfComp)
                .add(typeComp)
        ;//.add(pathComp);

        agentComp = new EnemyAgentComponent(entity, new GroundSteering(entity));
        agentComp.stateMachine = new DefaultStateMachine<EnemyAgentComponent, EnemyState>(agentComp, EnemyState.IDLE);
        EntityManager.add(agentComp);
        entity.add(agentComp);


        engine.addEntity(entity);

        return entity;
    }

    public static Entity createWitch(Vector2 pos){
        Entity entity = engine.createEntity();

        AnimationComponent animComp  = engine.createComponent(AnimationComponent.class);
        BodyComponent      bodyComp  = engine.createComponent(BodyComponent.class);
        StatsComponent     statsComp = engine.createComponent(StatsComponent.class);
        TextureComponent   textComp  = engine.createComponent(TextureComponent.class);
        TransformComponent transComp = engine.createComponent(TransformComponent.class);
        TypeComponent      typeComp  = engine.createComponent(TypeComponent.class);

        Texture region = assets.manager.get(GameAssetsManager.witchTexture, Texture.class);

        TextureRegion[][] regions = TextureRegion.split(region, 20 ,200);

        animComp.add(EnemyState.Attacking, frameDuration, Animation.PlayMode.NORMAL,
                regions[0][0],
                regions[0][1],
                regions[0][2],
                regions[0][3],
                regions[0][4],
                regions[0][5],
                regions[0][6],
                regions[0][7],
                regions[0][8],
                regions[0][9]
        );

         transComp.position.x = pos.x;
         transComp.position.y = pos.y;
         transComp.position.z = 1;
         transComp.width = 20;
         transComp.height = 20;

         textComp.region = regions[0][0];

        // typeComp.type = TypeComponent.ENEMY;

         entity//.add(animComp)
                // .add(bodyComp)
                 //.add(statsComp)
                 .add(textComp)
                 .add(transComp);
                 //.add(typeComp);

         engine.addEntity(entity);


        return entity;
    }


    public static void createDropper(float odd, float x, float y, float width, float height){
        Entity dropper = engine.createEntity();

        DropperComponent dc = engine.createComponent(DropperComponent.class);

        dc.odd = odd;
        dc.tile = 0;
        dc.width = width;
        dc.height = height;
        dc.originX = x;
        dc.originY = y;

        dropper.add(dc);

        engine.addEntity(dropper);
    }

    public static void createDrop(float x, float y, float width, float height, Entity dropper) {
        Entity drop = engine.createEntity();

        DropComponent dc = engine.createComponent(DropComponent.class);
        BodyComponent bc = engine.createComponent(BodyComponent.class);
        TransformComponent tfc = engine.createComponent(TransformComponent.class);
        TextureComponent tc = engine.createComponent(TextureComponent.class);

        tc.region = new TextureRegion(new Texture("sprites/rock.png"));

        tfc.width = width;
        tfc.height = height;

        dc.dropper = dropper;

        bc.body = BodyFactory.makeBox(x, y ,width, height, BodyDef.BodyType.DynamicBody,true );
        bc.body.setUserData(drop);

        drop.add(dc).add(tfc).add(bc).add(tc);

        engine.addEntity(drop);
    }
}
