package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;

public class PhysicsSystem extends IteratingSystem {

    private World world;

    private ComponentMapper<BodyComponent> bm;
    private ComponentMapper<TransformComponent> tm;


    public PhysicsSystem(World world) {
        super(Family.all(BodyComponent.class, TransformComponent.class).get(), -1);
        this.world = world;
        bm = ComponentMapper.getFor(BodyComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        world.step(deltaTime, 6, 2);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent tfm = tm.get(entity);
        BodyComponent bc = bm.get(entity);
        Body body = bc.body;
        Vector2 position = body.getPosition();

        tfm.position.x = position.x + RenderSystem.PixelsToMeters(tfm.paddingLeft) - RenderSystem.PixelsToMeters(tfm.paddingRight);
        tfm.position.y = position.y + RenderSystem.PixelsToMeters(tfm.paddingBottom) - RenderSystem.PixelsToMeters(tfm.paddingTop);
        tfm.rotation = body.getAngle() * MathUtils.radiansToDegrees;

        //Inverter a texture dependendo pra que lado está a andar
        if(body.getLinearVelocity().x != 0 && bc.flippable) {
            if (body.getLinearVelocity().x > 1) tfm.flipX = false;
            if (body.getLinearVelocity().x < -1) tfm.flipX = true;
        }


        //Remove the Entity in case it went over the ground
        if (body.getPosition().y < 0){
            EntityManager.setToDestroy(entity);}


    }

}