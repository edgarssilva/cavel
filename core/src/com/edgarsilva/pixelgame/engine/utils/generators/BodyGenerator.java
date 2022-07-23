package com.edgarsilva.pixelgame.engine.utils.generators;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.utils.PhysicsConstants;

public class BodyGenerator {
    private static World world;

    private static GlyphLayout glyphLayout;

    public static void registerWorld(World world) {
        BodyGenerator.world = world;
    }

    public static Body generateBody(Entity owner, Vector2 position, BitmapFont image, CharSequence msg, FileHandle handle, short filterCategory) {
        glyphLayout.setText(image, msg);
        return bodyHelper(owner, position, new Vector2(glyphLayout.width, glyphLayout.height), handle, filterCategory);
    }

    public static Body generateBody(Entity owner, Sprite image, FileHandle handle, short filterCategory) {
        return bodyHelper(owner, new Vector2(image.getX(), image.getY()), new Vector2(image.getWidth(), image.getHeight()), handle, filterCategory);
    }

    public static Body bodyHelper(Entity owner, Vector2 position, Vector2 dimensions, FileHandle handle, short filterCategory) {
        Body body;

        String     rawJson      = handle.readString();
        JsonReader jsonReader   = new JsonReader();
        JsonValue root         = jsonReader.parse(rawJson);

        //short maskingBits = (short) ((PhysicsConstants.FRIENDLY_BITS | PhysicsConstants.ENEMY_BITS | PhysicsConstants.NEUTRAL_BITS | PhysicsConstants.LEVEL_BITS) ^ filterCategory);

        BodyDef bodyDef = new BodyDef();

        String bodyType = root.get("BodyDef").getString("type");
        if (bodyType.equalsIgnoreCase("DynamicBody"))
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        else if (bodyType.equalsIgnoreCase("KinematicBody"))
            bodyDef.type = BodyDef.BodyType.KinematicBody;
        else if (bodyType.equalsIgnoreCase("StaticBody"))
            bodyDef.type = BodyDef.BodyType.StaticBody;
        else
            Gdx.app.log("WARNING", "Entity Box2D body type undefined - " + filterCategory);

        JsonValue jsonBody = root.get("BodyDef");

        bodyDef.bullet = jsonBody.getBoolean("bullet");
        bodyDef.fixedRotation = jsonBody.getBoolean("fixedRotation");
        bodyDef.gravityScale = jsonBody.getFloat("gravityScale");

        bodyDef.position.set(position.x + (dimensions.x / 2 * RenderSystem.PIXELS_TO_METERS) ,
                position.y + (dimensions.y / 2 * RenderSystem.PIXELS_TO_METERS));

        //  bodyDef.position.set(position.x * RenderSystem.PIXELS_TO_METERS, position.y * RenderSystem.PIXELS_TO_METERS);


        body = world.createBody(bodyDef);
        body.setUserData(owner);

        JsonValue fixtures = root.get("Fixtures");
        for (JsonValue fixture : fixtures) {

            String fixtureType = fixture.getString("type");
            Shape shape;

            if (fixtureType.equalsIgnoreCase("PolygonShape")) {
                shape = new PolygonShape();
                ((PolygonShape) shape).setAsBox(fixture.getFloat("width") * RenderSystem.PIXELS_TO_METERS,
                        fixture.getFloat("height") * RenderSystem.PIXELS_TO_METERS,
                        new Vector2((body.getLocalCenter().x + fixture.getFloat("x") * RenderSystem.PIXELS_TO_METERS),
                                (body.getLocalCenter().y + fixture.getFloat("y") * RenderSystem.PIXELS_TO_METERS)), 0f);

            } else if (fixtureType.equalsIgnoreCase("CircleShape")) {
                shape = new CircleShape();
                shape.setRadius(fixture.getFloat("radius") * RenderSystem.PIXELS_TO_METERS);
                ((CircleShape) shape).setPosition(new Vector2((body.getLocalCenter().x + fixture.getFloat("x")) * RenderSystem.PIXELS_TO_METERS,
                        (body.getLocalCenter().y + fixture.getFloat("y")) * RenderSystem.PIXELS_TO_METERS));
            } else {
                Gdx.app.log("WARNING", "Generated body shape was invalid");
                continue;
            }

            boolean isSensor = fixture.getBoolean("isSensor");

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = isSensor;
            fixtureDef.density = fixture.getFloat("density");

            if (isSensor) {
                fixtureDef.filter.categoryBits =  fixture.getShort("bitShifts");   //(short) (filterCategory << fixture.getShort("bitShifts"));
                fixtureDef.filter.maskBits = PhysicsConstants.LEVEL_BITS | PhysicsConstants.OBSTACLE_BITS;
            } else {
                fixtureDef.friction = fixture.getFloat("friction");
                fixtureDef.filter.categoryBits = filterCategory;

                switch (filterCategory) {
                    case PhysicsConstants.ENEMY_BITS:
                        fixtureDef.filter.maskBits =
                                PhysicsConstants.ATTACK_SENSOR | PhysicsConstants.LEVEL_BITS;
                        break;
                    case PhysicsConstants.FRIENDLY_BITS:
                        fixtureDef.filter.maskBits =
                                        PhysicsConstants.ENEMY_BITS |
                                        PhysicsConstants.LEVEL_BITS |
                                        PhysicsConstants.OBSTACLE_BITS |
                                        PhysicsConstants.FRIENDLY_BITS |
                                        PhysicsConstants.ENEMY_ATTACK_SENSOR |
                                        PhysicsConstants.MESSAGE_BITS |
                                        PhysicsConstants.COIN_BITS;
                }
            }
            body.createFixture(fixtureDef).setUserData(owner);
            shape.dispose();
        }

        return body;
    }


}