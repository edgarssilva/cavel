package com.edgarsilva.pixelgame.engine.utils.factories;

import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.utils.PhysicsConstants;

public class BodyFactory {
    private static World world;

    public BodyFactory(World world) {
        BodyFactory.world = world;
    }

    public static Body makeBox(float x, float y, float width, float height, BodyDef.BodyType type, boolean isSensor){
        Body body;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.active = true;

        if (isSensor) bodyDef.gravityScale = 0.5f;
        bodyDef.position.set(x + (width / 2f * RenderSystem.PIXELS_TO_METERS),y + (height / 2f * RenderSystem.PIXELS_TO_METERS));
        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2f * RenderSystem.PIXELS_TO_METERS,height / 2f * RenderSystem.PIXELS_TO_METERS);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = box;
        fdef.isSensor = isSensor;
        fdef.filter.maskBits = PhysicsConstants.FRIENDLY_BITS;
        fdef.filter.categoryBits = PhysicsConstants.MESSAGE_BITS;


        body.createFixture(fdef);
        box.dispose();
        return body;
    }


    public static Shape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * RenderSystem.PIXELS_TO_METERS,
                (rectangle.y + rectangle.height * 0.5f) * RenderSystem.PIXELS_TO_METERS);
        polygon.setAsBox(rectangle.width * 0.5f * RenderSystem.PIXELS_TO_METERS,
                rectangle.height * 0.5f * RenderSystem.PIXELS_TO_METERS,
                size,
                0.0f);
        return  polygon;
    }

    public static Shape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius * RenderSystem.PIXELS_TO_METERS);
        circleShape.setPosition(new Vector2(circle.x * RenderSystem.PIXELS_TO_METERS, circle.y * RenderSystem.PIXELS_TO_METERS));
        return circleShape;
    }

    public static Shape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] * RenderSystem.PIXELS_TO_METERS;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    public static Shape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] * RenderSystem.PIXELS_TO_METERS;
            worldVertices[i].y = vertices[i * 2 + 1] * RenderSystem.PIXELS_TO_METERS;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }

}
