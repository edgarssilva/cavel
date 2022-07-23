package com.edgarsilva.pixelgame.engine.utils.generators;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyState;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StateAnimationComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;

public class Generator {

    public static void generateStats(JsonValue root, StatsComponent stats){
        stats.damage    = root.getShort("damage");
        stats.health    = root.getShort("health");
        stats.maxHealth = root.getShort("maxHealth");
    }

    public static void generateTransform(JsonValue root, TransformComponent trans){
        trans.width         = root.getFloat("width");
        trans.height        = root.getFloat("height");
        trans.paddingLeft   = root.getFloat("left");
        trans.paddingRight  = root.getFloat("right");
        trans.paddingTop    = root.getFloat("top");
        trans.paddingBottom = root.getFloat("bottom");
        trans.scale.set(root.getFloat("scaleX"), root.getFloat("scaleY"));
    }

    public static void generateBody(JsonValue root,  Entity entity, BodyComponent bodyComp, Vector2 position,  Vector2 dimensions, World world){
        BodyDef bodyDef = new BodyDef();

        String bodyType = root.get("BodyDef").getString("type");

        if (bodyType.equalsIgnoreCase("DynamicBody"))
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        else if (bodyType.equalsIgnoreCase("KinematicBody"))
            bodyDef.type = BodyDef.BodyType.KinematicBody;
        else if (bodyType.equalsIgnoreCase("StaticBody"))
            bodyDef.type = BodyDef.BodyType.StaticBody;
        else
            System.out.println("Couldn't find a body type !");

        JsonValue jsonBody = root.get("BodyDef");

        bodyDef.bullet        = jsonBody.getBoolean("bullet");
        bodyDef.fixedRotation = jsonBody.getBoolean("fixedRotation");
        bodyDef.gravityScale  = jsonBody.getFloat("gravityScale");

        bodyDef.position.set(
                position.x + (dimensions.x / 2 * RenderSystem.PIXELS_TO_METERS),
                position.y + (dimensions.y / 2 * RenderSystem.PIXELS_TO_METERS)
        );


        bodyComp.body = world.createBody(bodyDef);
        bodyComp.body.setUserData(entity);


        JsonValue fixtures = root.get("Fixtures");

        for (JsonValue fixture : fixtures) {

            Shape shape;
            String fixtureType = fixture.getString("type");

            if (fixtureType.equalsIgnoreCase("PolygonShape")) {
                shape = new PolygonShape();

                ((PolygonShape) shape).setAsBox(
                        fixture.getFloat("width")  * RenderSystem.PIXELS_TO_METERS,
                        fixture.getFloat("height") * RenderSystem.PIXELS_TO_METERS,
                        new Vector2(
                                (bodyComp.body.getLocalCenter().x + fixture.getFloat("x") * RenderSystem.PIXELS_TO_METERS),
                                (bodyComp.body.getLocalCenter().y + fixture.getFloat("y") * RenderSystem.PIXELS_TO_METERS)),
                        0f);

            } else if (fixtureType.equalsIgnoreCase("CircleShape")) {
                shape = new CircleShape();

                shape.setRadius(fixture.getFloat("radius") * RenderSystem.PIXELS_TO_METERS);

                ((CircleShape) shape).setPosition(new Vector2(
                        (bodyComp.body.getLocalCenter().x + fixture.getFloat("x")) * RenderSystem.PIXELS_TO_METERS,
                        (bodyComp.body.getLocalCenter().y + fixture.getFloat("y")) * RenderSystem.PIXELS_TO_METERS)
                );

            } else {
                System.out.println("Couldn't find a body shape !");
                continue;
            }

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape      = shape;
            fixtureDef.isSensor   = fixture.getBoolean("isSensor");
            fixtureDef.density    = fixture.getFloat("density");
            fixtureDef.friction   = fixture.getFloat("friction");

            short maskBits = 0;

            for (JsonValue mask : fixture.get("mask"))
                if (maskBits == 0) maskBits = mask.asShort();
                else               maskBits |= mask.asShort();

            fixtureDef.filter.maskBits     =  maskBits;
            fixtureDef.filter.categoryBits = fixture.getShort("category");

            bodyComp.body.createFixture(fixtureDef).setUserData(entity);
            shape.dispose();

            bodyComp.flippable = root.getBoolean("flippable");
        }
    }

    public static void generateAnimation(JsonValue root, StateAnimationComponent anim, TextureAtlas atlas){
        for (JsonValue animation : root) {

            Array<TextureRegion> regions = new Array<TextureRegion>();
            for (JsonValue region : animation.get("frames")) regions.addAll(atlas.findRegions(region.asString()));


            TextureRegion[] regs = new TextureRegion[regions.size];
            for (int i = 0; i < regions.size; i++) regs[i] = regions.get(i);

            anim.add(
                    EnemyState.valueOf(animation.getString("state")),
                    animation.getFloat("duration"),
                    Animation.PlayMode.valueOf(animation.getString("playMode")),
                    regs
            );
        }
    }
}
