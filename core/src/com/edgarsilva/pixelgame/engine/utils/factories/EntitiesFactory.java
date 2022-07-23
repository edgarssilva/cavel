package com.edgarsilva.pixelgame.engine.utils.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.ai.fsm.Enemies;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgent;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAgent;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAttackState;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerState;
import com.edgarsilva.pixelgame.engine.ai.fsm.boss.WitchAgent;
import com.edgarsilva.pixelgame.engine.ai.fsm.boss.WitchState;
import com.edgarsilva.pixelgame.engine.ecs.components.AnimationComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.AttachedComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.AttackCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.BehaviorComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.BossComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.CoinComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.EnemyCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.HealthBarComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.LightComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.PlayerCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StateAnimationComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TextureComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.utils.PhysicsConstants;
import com.edgarsilva.pixelgame.engine.utils.generators.BodyEditorLoader;
import com.edgarsilva.pixelgame.engine.utils.generators.Generator;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.screens.PlayScreen;

import java.util.Random;

import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 * Classe responsável por criar as Entities utilizando métodos estáticos
 *
 * @autor: Edgar Silva
 */
public class EntitiesFactory {

    private static PooledEngine engine;
    private static GameAssetsManager assets;
    private static World world;
    private static RayHandler rayHandler;

    private static float frameDuration = 0.175f;
    private static float fastFrameDuration = 0.100f;

    private static Random random;

    private static TextureAtlas atlas;

    public EntitiesFactory(PlayScreen screen) {
        EntitiesFactory.engine = screen.getEngine();
        EntitiesFactory.world = screen.getWorld();
        EntitiesFactory.assets = screen.getGame().assets;
        EntitiesFactory.rayHandler = screen.getRayHandler();

        atlas = assets.manager.get(GameAssetsManager.atlas);

        random = new Random();
    }

    public static void createPlayer(Vector2 position, PlayerState state, PlayerAttackState attackState) {
        Entity entity = engine.createEntity();

        BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
        TransformComponent transform = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        StateAnimationComponent animation = engine.createComponent(StateAnimationComponent.class);
        StatsComponent sc = engine.createComponent(StatsComponent.class);
        PlayerCollisionComponent sensorComp = engine.createComponent(PlayerCollisionComponent.class);

        sc.maxHealth = PixelGame.hp;

        JsonReader reader = new JsonReader();
        JsonValue  root   = reader.parse(Gdx.files.internal("entities/player.json5").readString());

        if (root.has("transform")) {
            Generator.generateTransform(root.get("transform"), transform);
        }

        if (root.has("stats")) {
            Generator.generateStats(root.get("stats"), sc);
        }

        if (root.has("body")) {
            Generator.generateBody(
                    root.get("body"),
                    entity,  b2dbody, position,
                    new Vector2(transform.width, transform.height),
                    world
            );
        }


        for (JsonValue anim : root.get("animation")) {

            Array<TextureRegion> regions = new Array<TextureRegion>();
            for (JsonValue region : anim.get("frames")) regions.addAll(atlas.findRegions(region.asString()));


            TextureRegion[] regs = new TextureRegion[regions.size];
            for (int i = 0; i < regions.size; i++) regs[i] = regions.get(i);

            State st;
            try {
                st = PlayerState.valueOf(anim.getString("state"));
            } catch (IllegalArgumentException ex) {
                st = PlayerAttackState.valueOf(anim.getString("state"));
            }
            animation.add(
                    st,
                    anim.getFloat("duration"),
                    Animation.PlayMode.valueOf(anim.getString("playMode")),
                    regs
            );
        }

        // add the components to the entity
        entity.add(b2dbody)
                .add(transform)
                .add(texture)
                .add(animation)
                .add(sensorComp)
                .add(sc);
        PlayerAgent agent = new PlayerAgent(entity, state, attackState);
        entity.add(agent);
        // entity.add(stateCom)


        // add the entity to the engine
        engine.addEntity(entity);
        EntityManager.setPlayer(entity);
        EntityManager.add(agent);
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

        String name;

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

    public static Entity createCoin(Vector2 position, int value) {
        Entity entity = engine.createEntity();

        BodyComponent bc = engine.createComponent(BodyComponent.class);
        TransformComponent tfc = engine.createComponent(TransformComponent.class);
        TextureComponent tc = engine.createComponent(TextureComponent.class);
        CoinComponent cc = engine.createComponent(CoinComponent.class);
        AnimationComponent ac = engine.createComponent(AnimationComponent.class);

        Body body;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.active = true;
        bodyDef.position.set(position.x + (6.4f / 2f * RenderSystem.PIXELS_TO_METERS), position.y + (7.6f / 2f * RenderSystem.PIXELS_TO_METERS));


        PolygonShape box = new PolygonShape();
        box.setAsBox(6.4f / 2f * RenderSystem.PIXELS_TO_METERS, 7.6f / 2f * RenderSystem.PIXELS_TO_METERS);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = box;
        fdef.isSensor = false;
        fdef.restitution = 0.6f;
        fdef.density = 10;
        fdef.filter.categoryBits = PhysicsConstants.COIN_BITS;
        fdef.filter.maskBits = PhysicsConstants.FRIENDLY_BITS |
                PhysicsConstants.LEVEL_BITS |
                PhysicsConstants.OBSTACLE_BITS;


        body = world.createBody(bodyDef);
        body.setUserData(entity);
        body.createFixture(fdef).setUserData(entity);
        box.dispose();
        bc.body = body;

        tfc.width = 16f;
        tfc.height = 19f;

        cc.value = value;


        float randomX = random.nextInt(1) / 10f + 0.5f;
        float randomY = random.nextInt(2) / 10f + 2f;
        if (random.nextBoolean()) randomX = -randomX;
        bc.body.setLinearVelocity(randomX, randomY);


        ac.animation = new Animation<TextureRegion>(fastFrameDuration,
                atlas.findRegions("coin").toArray());

        ac.looping = true;

        entity.add(bc).add(tfc).add(tc).add(cc).add(ac);
        engine.addEntity(entity);
        return entity;
    }

    public static Entity createWitch(Vector2 pos) {
        Entity entity = engine.createEntity();

        StateAnimationComponent animComp     = engine.createComponent(StateAnimationComponent.class);
        BodyComponent           bodyComp     = engine.createComponent(BodyComponent.class);
        StatsComponent          statsComp    = engine.createComponent(StatsComponent.class);
        TextureComponent        textComp     = engine.createComponent(TextureComponent.class);
        TransformComponent      transComp    = engine.createComponent(TransformComponent.class);
        BossComponent           bossComp     = engine.createComponent(BossComponent.class);
        BehaviorComponent       behaviorComp = engine.createComponent(BehaviorComponent.class);
        WitchAgent              agentComp    = engine.createComponent(WitchAgent.class);

        Body body;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(pos);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(RenderSystem.PixelsToMeters(25), RenderSystem.PixelsToMeters(45));
        fdef.shape = shape;
        fdef.filter.categoryBits = PhysicsConstants.ENEMY_BITS;
        fdef.filter.maskBits = PhysicsConstants.LEVEL_BITS;

        body = world.createBody(bdef);
        body.setUserData(entity);
        body.createFixture(fdef).setUserData(entity);

        bodyComp.body = body;
        bodyComp.flippable = true;
        shape.dispose();

        animComp.animations.put(WitchState.Attacking, new Animation<TextureRegion>(
                fastFrameDuration, atlas.findRegions("witch_attack"), Animation.PlayMode.NORMAL
        ));
        animComp.animations.put(WitchState.Walking, new Animation<TextureRegion>(
                fastFrameDuration, atlas.findRegions("witch_slide"), Animation.PlayMode.NORMAL
        ));

        transComp.position.x = pos.x;
        transComp.position.y = pos.y;
        transComp.position.z = -1;
        transComp.width = 120;
        transComp.height = 120;
        transComp.paddingBottom = 5;
        transComp.flipX = true;

        //textComp.region = regions[0][0];
        //BehaviorTreeParser<Entity> parser = new BehaviorTreeParser<Entity>(BehaviorTreeParser.DEBUG_HIGH);
        // behaviorComp.bTree = parser.parse(Gdx.files.internal("entities/behavior/witch.tree").reader(), entity);
        // behaviorComp.bTree.start();

        //textComp.region = new TextureRegion(region);
        entity.add(animComp)
                .add(bodyComp)
                .add(statsComp)
                .add(textComp)
                .add(transComp)
                .add(bossComp)
                .add(behaviorComp);

        agentComp = new WitchAgent(entity);
        EntityManager.add(agentComp);
        entity.add(agentComp);

        engine.addEntity(entity);
        return entity;
    }

    public static Entity createWitchAttack(Entity witch){
        Entity attack = engine.createEntity();

        BodyComponent            bodyComp   = engine.createComponent(BodyComponent.class);
        AttackCollisionComponent collComp   = engine.createComponent(AttackCollisionComponent.class);
        AttachedComponent        attachComp = engine.createComponent(AttachedComponent.class);
        LightComponent           lightComp  = engine.createComponent(LightComponent.class);

        attachComp.attachTo = witch;

        Body body;

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 0f;
        bdef.active = true;
        body = world.createBody(bdef);

        FixtureDef f = new FixtureDef();
        //f.shape = shape;
        f.isSensor = false;
        f.filter.categoryBits = PhysicsConstants.ENEMY_ATTACK_SENSOR;
        f.filter.maskBits = PhysicsConstants.FRIENDLY_BITS;

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(
                RenderSystem.PixelsToMeters(24),
                RenderSystem.PixelsToMeters(24)
        );

        f.shape = shape;

        body.createFixture(f).setUserData(attack);
        body.setUserData(attack);
        shape.dispose();

        bodyComp.body = body;

        lightComp.light = new PointLight(rayHandler, 20, Color.LIGHT_GRAY, 5, body.getPosition().x, body.getPosition().y);
        lightComp.light.attachToBody(body);

        attack.add(bodyComp).add(collComp).add(attachComp).add(lightComp);
        engine.addEntity(attack);
        return attack;
    }

    public static Entity createEnemy(Enemies type, Vector2 pos) {
        Entity entity = engine.createEntity();

        StateAnimationComponent animComp = engine.createComponent(StateAnimationComponent.class);
        BodyComponent bodyComp = engine.createComponent(BodyComponent.class);
        EnemyCollisionComponent collComp = engine.createComponent(EnemyCollisionComponent.class);

        StatsComponent statsComp = engine.createComponent(StatsComponent.class);
        TextureComponent textComp = engine.createComponent(TextureComponent.class);
        TransformComponent transfComp = engine.createComponent(TransformComponent.class);
        EnemyAgent agentComp = engine.createComponent(EnemyAgent.class);
        HealthBarComponent healthComp = engine.createComponent(HealthBarComponent.class);


        healthComp.texture = new Texture("raw/healthbar.png");
        healthComp.damage = new Texture("raw/healthbar_damage.png");
        healthComp.background = new Texture("raw/healthbar_background.png");

        String handle = null;

        switch (type) {
            case SLIME:
                handle = "entities/slime.json5";
                break;
            case SKELETON:
                handle = "entities/skeleton.json5";
                break;
            case WIZARD:
                handle = "entities/wizard.json5";
                break;
        }

        JsonReader reader = new JsonReader();
        JsonValue  root   = reader.parse(Gdx.files.internal(handle).readString());

        if (root.has("transform")) {
            Generator.generateTransform(root.get("transform"), transfComp);
        }

        if (root.has("stats")) {
            Generator.generateStats(root.get("stats"), statsComp);
        }

        if (root.has("body")) {
            Generator.generateBody(
                    root.get("body"),
                    entity,  bodyComp, pos,
                    new Vector2(transfComp.width, transfComp.height),
                    world
            );
        }

        if (root.has("animation")) {
            Generator.generateAnimation(root.get("animation"), animComp, atlas);
        }

        entity.add(animComp)
                .add(bodyComp)
                .add(collComp)
                .add(statsComp)
                .add(textComp)
                .add(transfComp)
                .add(healthComp);

        agentComp = new EnemyAgent(entity, type);
        EntityManager.add(agentComp);
        entity.add(agentComp);

        engine.addEntity(entity);
        return entity;
    }
}
