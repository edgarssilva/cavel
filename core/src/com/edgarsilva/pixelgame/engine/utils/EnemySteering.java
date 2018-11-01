package com.edgarsilva.pixelgame.engine.utils;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.edgarsilva.pixelgame.engine.ai.pfa.PathfindingDebugger;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;

public class EnemySteering implements Steerable<Vector2>, Updateable {

    private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);

    private SteeringBehavior<Vector2> steeringBehavior;
    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());

    private Body body, targetBody;
    private Vector2 position, linearVelocity;
    private boolean independentFacing;
    private float orientation, angularVelocity,
            maxLinearSpeed, maxLinearAceleration,
            maxAngularSpeed, maxAngularAceleration;

    Entity entity;


    public EnemySteering(Vector2 position) {
        this.position = position;
    }

    public EnemySteering(Entity entity) {
        this.entity = entity;
        independentFacing = true;
        maxLinearSpeed = 3f;
        maxLinearAceleration = 3f;
        maxAngularSpeed = 3f;
        maxAngularAceleration = 3f;

        linearVelocity = new Vector2();
        body = bm.get(entity).body;
        targetBody = bm.get(EntityManager.getPlayer()).body;
        //target = new EnemySteering(targetBody.getPosition().cpy().scl(RenderSystem.PPM));
        position = new Vector2(body.getPosition()).scl(RenderSystem.PPM);


        //  steeringBehavior = new Seek<Vector2>(this, target);


    }

    @Override
    public void update(float deltaTime) {

        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);

            applySteering(steeringOutput, deltaTime);
        }

        // body.setTransform(position, orientation);
        //  body.setLinearVelocity(linearVelocity);
        // body.setAngularVelocity(angularVelocity);


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
    private void applySteering(SteeringAcceleration<Vector2> steering, float delta) {
        //Neste caso específico é necessário aplicar forças a um corpo e não um ponto
        linearVelocity = body.getLinearVelocity();
        Vector2 acceleration = steering.linear;

        linearVelocity.mulAdd(acceleration, delta).limit(maxLinearSpeed);

        //float  desiredVelocityX = MathUtils.clamp(linearVelocity.x + acceleration.x,-maxLinearSpeed, maxLinearSpeed);
        //float  desiredVelocityY = MathUtils.clamp(linearVelocity.y + acceleration.y,-maxLinearSpeed, maxLinearSpeed);

        // if (linearVelocity.isZero(MathUtils.FLOAT_ROUNDING_ERROR))
        //   linearVelocity.x = linearVelocity.y = 0f;

        //if(acceleration.x == 0) linearVelocity.x = 0f;
        //if(acceleration.y == 0) linearVelocity.y = 0f;

        //float velocityX = desiredVelocityX - linearVelocity.x;
        //float velocityY = desiredVelocityY - linearVelocity.y;

        // Vector2 impulse = new Vector2(desiredVelocityX, desiredVelocityY);

        System.out.println("LinearVelocity: " + linearVelocity.toString());
        System.out.println("Mass: " + body.getMass());
        body.setLinearVelocity(linearVelocity);

        position.set(body.getPosition()).scl(RenderSystem.PPM);
        //position = body.getPosition().cpy().scl(RenderSystem.PPM);

        // position.x = RenderSystem.PPM * body.getPosition().x;
        //position.y = RenderSystem.PPM * body.getPosition().y;

        PathfindingDebugger.drawPoint2Point(position, targetBody.getPosition().cpy().scl(RenderSystem.PPM));

        //Exemplo dado na documentação
       /* this.position.mulAdd(linearVelocity, delta);
        this.linearVelocity.mulAdd(steering.linear, delta).limit(this.getMaxLinearSpeed());


        if (independentFacing) {
            this.orientation += angularVelocity * delta;
            this.angularVelocity += steering.angular * delta;
        } else {
            float newOrientation = calculateOrientationFromLinearVelocity(this);
            if (newOrientation != this.orientation) {
                this.angularVelocity = (newOrientation - this.orientation) * delta;
                this.orientation = newOrientation;
            }
        }
*/


    }

    /**
     * Verificar se é necessário calcular a orientação.
     * Se a velocidade linear for menor que
     * {@link MathUtils#FLOAT_ROUNDING_ERROR} = {@value MathUtils#FLOAT_ROUNDING_ERROR}
     * retorna a orientação atual se não retorna o angulo resultante da velocidade linear usando
     * {@link EnemySteering#vectorToAngle(Vector2)}.
     *
     * @param character {@link Steerable} a qual verificar a linear velocity
     * @return O ângulo derivado da velocidade linear do {@link Steerable}
     */
    public static float calculateOrientationFromLinearVelocity(Steerable<Vector2> character) {
        if (character.getLinearVelocity().isZero(MathUtils.FLOAT_ROUNDING_ERROR))
            return character.getOrientation();

        return character.vectorToAngle(character.getLinearVelocity());
    }


    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public float getBoundingRadius() {
        return 0;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {

    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularAceleration = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAceleration = maxAngularAcceleration;
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
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.y = -(float) Math.cos(angle);
        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return null;
    }


}
