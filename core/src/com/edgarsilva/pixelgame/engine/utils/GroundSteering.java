package com.edgarsilva.pixelgame.engine.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.utils.objects.Steering;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;

public class GroundSteering extends Steering implements Steerable<Vector2>, Updateable{

    public GroundSteering(Vector2 position) {
        super(position);
    }

    public GroundSteering(Entity entity) {
        super(entity);
    }

    public GroundSteering() {
        super();
    }

    /**
     * Atualizar a posição conforme a velocidade linear, atualizar a velocidade linear conforme
     * o output do behavior e limitar a velociade linear ao seu máximo.
     * <p>
     * Em seguida, atualizar a velociade angular, como a linear, para apontar a location a dar
     * steering ou à velocidade linear dependendo do valor da variavel {@code "independentFacing"}.
     *
     * @param steering Aceleração resultante do output do behavior
     * @param delta    Tempo em milisegundos após o ultimo frame. Usado para dar scale nas velocidades.
     */
    @Override
    protected void applySteering (SteeringAcceleration<Vector2> steering, float delta) {
        this.linearVelocity.mulAdd(steering.linear, delta).limit(this.getMaxLinearSpeed());

        Body body = bodyCom.body;
        body.setLinearVelocity(linearVelocity);
        linearVelocity = body.getLinearVelocity();
        float   velocityX = linearVelocity.x;

        float desiredVelocity = Math.max(velocityX - maxLinearSpeed,
                Math.min(Math.signum(steering.linear.x) * maxLinearAcceleration, velocityX + maxLinearSpeed));

        desiredVelocity = Math.min(desiredVelocity, maxLinearSpeed);

        float velocityChange = desiredVelocity - velocityX;
        float impulse = body.getMass() * velocityChange;
        body.applyLinearImpulse(impulse, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
       // System.out.println(desiredVelocity);
       // body.setTransform(body.getPosition().x + ((target.getPosition().x - body.getPosition().x) * delta), body.getPosition().y, 0);

        position.x = body.getPosition().x * RenderSystem.PPM;
        position.y = body.getPosition().y * RenderSystem.PPM;

        // Update orientation and angular velocity
        if (independentFacing) {
            this.orientation += angularVelocity * delta;
            this.angularVelocity += steering.angular * delta;
        } else {
            // For non-independent facing we have to align orientation to linear velocity
            float newOrientation = calculateOrientationFromLinearVelocity(this);
            if (newOrientation != this.orientation) {
                this.angularVelocity = (newOrientation - this.orientation) * delta;
                this.orientation = newOrientation;
            }
        }

    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public void setOrientation(float orientation) {
    }

    @Override
    public Location<Vector2> newLocation() {
        return null;
    }



}
