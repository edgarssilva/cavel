package com.edgarsilva.pixelgame.engine.utils.objects;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.utils.GroundSteering;

public abstract class Steering implements Steerable<Vector2>, Updateable {
    protected ComponentMapper<TransformComponent> transformMap = ComponentMapper.getFor(TransformComponent.class);
    protected ComponentMapper<BodyComponent>     bodyMap      = ComponentMapper.getFor(BodyComponent.class);

    protected static final SteeringAcceleration<Vector2> steeringOutput =
            new SteeringAcceleration<Vector2>(new Vector2());

    protected Vector2 position;
    protected Vector2 linearVelocity;
    protected float orientation;
    protected float angularVelocity;

    protected float maxLinearSpeed;
    protected float minLinearSpeed;
    protected float maxLinearAcceleration;
    protected float maxAngularSpeed;
    protected float maxAngularAcceleration;

    protected boolean independentFacing;
    protected BodyComponent bodyCom;
    protected Entity entity;
    protected Steerable<Vector2> target;

    protected SteeringBehavior<Vector2> steeringBehavior;

    public Steering() {}

    public Steering(Vector2 position) {
        this.position = position;
    }

    public Steering(Entity entity) {
        independentFacing = true;

        this.entity = entity;

        bodyCom         =  bodyMap.get(entity);
        position        =  new Vector2(bodyCom.body.getPosition().x, bodyCom.body.getPosition().y);
        linearVelocity  =  new Vector2(bodyCom.body.getLinearVelocity().x, bodyCom.body.getLinearVelocity().y);
    }


    @Override
    public float vectorToAngle (Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector (Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    /**
     * Verificar se é necessário calcular a orientação.
     * Se a velocidade linear for menor que
     * {@link MathUtils#FLOAT_ROUNDING_ERROR} = {@value MathUtils#FLOAT_ROUNDING_ERROR}
     * retorna a orientação atual se não retorna o angulo resultante da velocidade linear usando
     * {@link GroundSteering#vectorToAngle(Vector2)}.
     *
     * @param character {@link Steerable} a qual verificar a linear velocity
     * @return O ângulo derivado da velocidade linear do {@link Steerable}
     */
    public static float calculateOrientationFromLinearVelocity(Steerable<Vector2> character) {
        // Se a velocidade for nula não calcular o angular
        if (character.getLinearVelocity().isZero(MathUtils.FLOAT_ROUNDING_ERROR))
            return character.getOrientation();

        return character.vectorToAngle(character.getLinearVelocity());
    }

    public void update (float delta) {
        if (steeringBehavior != null) {
            // Calcular a aceleração
            steeringBehavior.calculateSteering(steeringOutput);

            // Applicar a aceleração
            applySteering(steeringOutput, delta);
        }
    }

    protected abstract void applySteering (SteeringAcceleration<Vector2> steering, float delta);


    public void setSteeringBehavior(Class<?> behaviorType) {
        if (behaviorType == Arrive.class) {
            steeringBehavior = new Arrive<Vector2>(this, target).setDecelerationRadius(10).setArrivalTolerance(3);
        } else if (behaviorType == Flee.class) {
            steeringBehavior = new Flee<Vector2>(this, target);
        } else if (behaviorType == Seek.class) {
            steeringBehavior = new Seek<Vector2>(this, target);
        }
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setTarget(Steerable<Vector2> target) {
        this.target = target;
        if (steeringBehavior == null) {
            steeringBehavior = new Arrive<Vector2>(this, target).setDecelerationRadius(10).setArrivalTolerance(3);
        }
    }

    public Steerable<Vector2> getTarget() {
        return target;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getBoundingRadius() {
        return 0;
    }

    @Override
    public float getAngularVelocity() {
        return 0;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {}

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    public float getMinLinearSpeed() { return minLinearSpeed; }

    public void setMinLinearSpeed(float minLinearSpeed) { this.minLinearSpeed = minLinearSpeed; }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }
}
