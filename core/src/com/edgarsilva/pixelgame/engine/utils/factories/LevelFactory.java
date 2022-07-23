package com.edgarsilva.pixelgame.engine.utils.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAttackState;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerState;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.MessageComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.utils.PhysicsConstants;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.screens.PlayScreen;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LevelFactory {

    private static World world;
    private static PooledEngine engine;
    private static RayHandler rayHandler;
    public LevelFactory(PlayScreen screen) {
        world = screen.getWorld();
        engine = screen.getEngine();
        rayHandler = screen.getRayHandler();
    }


    public static void createPhysics(TiledMap map, String layerName) {
        MapLayer layer = map.getLayers().get(layerName);

        MapObjects objects = layer.getObjects();

        for(MapObject object : objects) {


            if (object instanceof TextureMapObject) {
                continue;
            }

            Shape shape;
            BodyDef bodyDef = new BodyDef();
            bodyDef.awake = false;
            bodyDef.type = BodyDef.BodyType.StaticBody;

            if (object instanceof RectangleMapObject) {
                shape = BodyFactory.getRectangle((RectangleMapObject) object);
            } else if (object instanceof PolygonMapObject) {
                shape = BodyFactory.getPolygon((PolygonMapObject) object);
            } else if (object instanceof PolylineMapObject) {
                shape = BodyFactory.getPolyline((PolylineMapObject) object);
            } else if (object instanceof CircleMapObject) {
                shape = BodyFactory.getCircle((CircleMapObject) object);
            } else {
                Gdx.app.log("Unrecognized shape", "" + object.toString());
                continue;
            }

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = PhysicsConstants.LEVEL_BITS;
            /*fixtureDef.filter.maskBits = (short) (PhysicsConstants.FRIENDLY_BITS |
                    PhysicsConstants.ENEMY_BITS |
                    PhysicsConstants.NEUTRAL_BITS |
                    PhysicsConstants.FOOT_SENSOR |
                    PhysicsConstants.RIGHT_WALL_SENSOR |
                    PhysicsConstants.LEFT_WALL_SENSOR);*/

            // All collisions need an entity, and all entities need a type to handle collisions
            Entity levelEntity = engine.createEntity();

            BodyComponent bodyComp = engine.createComponent(BodyComponent.class);

            bodyComp.body = world.createBody(bodyDef);
            bodyComp.body.setUserData(levelEntity);
            bodyComp.body.createFixture(fixtureDef).setUserData(levelEntity);

            levelEntity.add(bodyComp);
            engine.addEntity(levelEntity);

            fixtureDef.shape = null;
            shape.dispose();

        }
    }

    public static void makeEntities(TiledMap map, String layerName){

        MapObjects objects =  map.getLayers().get(layerName).getObjects();
        Vector2 position, dimension;
        for (MapObject object : objects) {
            position = getObjectPosition(object);
            dimension = getObjectDimension(object);

            String name = object.getName();

            if (name.equalsIgnoreCase("Player"))
                EntitiesFactory.createPlayer(position, PlayerState.Idle, PlayerAttackState.NONE);
            if (name.equalsIgnoreCase("Skeleton"))
                EntitiesFactory.createSkeleton(position);
            if (name.equalsIgnoreCase("Slime"))
                EntitiesFactory.createSlime(position);
        }

        if (EntityManager.getPlayer() == null)
            System.out.println("Player spawn point is undefined in the level !");
    }

    public static void makeObstacles(TiledMap map, String layerName) {
        MapObjects objects =  map.getLayers().get(layerName).getObjects();
        for (MapObject object : objects){
            if (object instanceof TextureMapObject) {
                continue;
            }

            Shape shape;
            BodyDef bodyDef = new BodyDef();
            bodyDef.awake = false;
            bodyDef.type = BodyDef.BodyType.StaticBody;

            if (object instanceof RectangleMapObject) {
                shape = BodyFactory.getRectangle((RectangleMapObject) object);
            } else if (object instanceof PolygonMapObject) {
                shape = BodyFactory.getPolygon((PolygonMapObject) object);
            } else if (object instanceof PolylineMapObject) {
                shape = BodyFactory.getPolyline((PolylineMapObject) object);
            } else if (object instanceof CircleMapObject) {
                shape = BodyFactory.getCircle((CircleMapObject) object);
            } else {
                Gdx.app.log("Unrecognized shape", "" + object.toString());
                continue;
            }

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = PhysicsConstants.OBSTACLE_BITS;
            fixtureDef.filter.maskBits = (short) (PhysicsConstants.FRIENDLY_BITS |
                    PhysicsConstants.ENEMY_BITS |
                    PhysicsConstants.NEUTRAL_BITS |
                    PhysicsConstants.FOOT_SENSOR |
                    PhysicsConstants.RIGHT_WALL_SENSOR |
                    PhysicsConstants.LEFT_WALL_SENSOR);

            // All collisions need an entity, and all entities need a type to handle collisions
            Entity levelEntity = engine.createEntity();

            BodyComponent bodyComp = engine.createComponent(BodyComponent.class);

            bodyComp.body = world.createBody(bodyDef);
            bodyComp.body.setUserData(levelEntity);
            bodyComp.body.createFixture(fixtureDef).setUserData(levelEntity);

            engine.addEntity(levelEntity);

            fixtureDef.shape = null;
            shape.dispose();
        }
    }


    public static void makeMessages(TiledMap tiledMap, String layerName) {
        MapObjects objects =  tiledMap.getLayers().get(layerName).getObjects();
        for (MapObject object : objects){
            if (object.getProperties().containsKey("message")) {

                Entity entity = engine.createEntity();

                BodyComponent      bc  = engine.createComponent(BodyComponent.class);
                MessageComponent   mc  = engine.createComponent(MessageComponent.class);

                Vector2 dimension = getObjectDimension(object);
                Vector2 position  = getObjectPosition(object);

                bc.body = BodyFactory.makeBox(position.x, position.y, dimension.x, dimension.y, BodyDef.BodyType.StaticBody, true);

                Filter filter = new Filter();
                filter.categoryBits = PhysicsConstants.MESSAGE_BITS;
                filter.maskBits = PhysicsConstants.FRIENDLY_BITS;

                for (Fixture fix : bc.body.getFixtureList()) {
                    fix.setFilterData(filter);
                    fix.setUserData(entity);
                }

                bc.body.setUserData(entity);

                String message = object.getProperties().get("message").toString();

                if (message.equalsIgnoreCase("movement")) {
                    mc.message = MessageComponent.TutorialMessages.Movement;
                }else if(message.equalsIgnoreCase("jump")){
                    mc.message = MessageComponent.TutorialMessages.Jump;
                }else if(message.equalsIgnoreCase("doublejump")){
                    mc.message = MessageComponent.TutorialMessages.DoubleJump;
                }else if(message.equalsIgnoreCase("attack")){
                    mc.message = MessageComponent.TutorialMessages.Attack;
                }else if(message.equalsIgnoreCase("fallattack")){
                    mc.message = MessageComponent.TutorialMessages.FallAttack;
                }else if(message.equalsIgnoreCase("wall")){
                    mc.message = MessageComponent.TutorialMessages.Wall;
                }

                entity.add(bc).add(mc);
                engine.addEntity(entity);
            }
        }
    }

    public static void makeHiddenWalls(TiledMap map, String layerName) {
        MapLayer layer = map.getLayers().get(layerName);

        MapObjects objects = layer.getObjects();

        for(MapObject object : objects) {


            if (object instanceof TextureMapObject) {
                continue;
            }

            Shape shape;
            BodyDef bodyDef = new BodyDef();
            bodyDef.awake = false;
            bodyDef.type = BodyDef.BodyType.StaticBody;

            if (object instanceof RectangleMapObject) {
                shape = BodyFactory.getRectangle((RectangleMapObject) object);
            } else if (object instanceof PolygonMapObject) {
                shape = BodyFactory.getPolygon((PolygonMapObject) object);
            } else if (object instanceof PolylineMapObject) {
                shape = BodyFactory.getPolyline((PolylineMapObject) object);
            } else if (object instanceof CircleMapObject) {
                shape = BodyFactory.getCircle((CircleMapObject) object);
            } else {
                Gdx.app.log("Unrecognized shape", "" + object.toString());
                continue;
            }

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = PhysicsConstants.HIDDEN_BITS;
            /*fixtureDef.filter.maskBits = (short) (PhysicsConstants.FRIENDLY_BITS |
                    PhysicsConstants.ENEMY_BITS |
                    PhysicsConstants.NEUTRAL_BITS |
                    PhysicsConstants.FOOT_SENSOR |
                    PhysicsConstants.RIGHT_WALL_SENSOR |
                    PhysicsConstants.LEFT_WALL_SENSOR);*/

            // All collisions need an entity, and all entities need a type to handle collisions
            Entity levelEntity = engine.createEntity();

            BodyComponent bodyComp = engine.createComponent(BodyComponent.class);

            bodyComp.body = world.createBody(bodyDef);
            bodyComp.body.setUserData(levelEntity);
            bodyComp.body.createFixture(fixtureDef).setUserData(levelEntity);

            engine.addEntity(levelEntity);

            fixtureDef.shape = null;
            shape.dispose();
        }
    }

    public static void makeLights(TiledMap tiledMap, String layerName){
        MapObjects objects =  tiledMap.getLayers().get(layerName).getObjects();

        rayHandler.removeAll();

        for (MapObject object : objects){
            PointLight light =  new PointLight(rayHandler,90);
            light.setColor(0.9f, 0.4f, 0.03f, 1f);
            light.setDistance(getObjectDimension(object).x / 1.75f);
            light.setPosition(getObjectPosition(object));
            light.setSoftnessLength(getObjectDimension(object).x / 6);
            light.setSoft(true);
        }
    }

    private static Vector2 getObjectPosition(MapObject object){
        float x = Float.parseFloat(object.getProperties().get("x").toString()) * RenderSystem.PIXELS_TO_METERS;
        float y = Float.parseFloat(object.getProperties().get("y").toString()) * RenderSystem.PIXELS_TO_METERS;
        return  new Vector2(x,y);
    }

    private static Vector2 getObjectDimension(MapObject object){
        float width = Float.parseFloat(object.getProperties().get("width").toString()) ;
        float height = Float.parseFloat(object.getProperties().get("height").toString());
        return  new Vector2(width, height);
    }


}
