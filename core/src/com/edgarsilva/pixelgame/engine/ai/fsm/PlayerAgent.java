package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.edgarsilva.pixelgame.engine.ecs.components.AnimationComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.AttackComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.PlayerCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.utils.controllers.Controller;
import com.edgarsilva.pixelgame.engine.utils.factories.EntitiesFactory;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;

/**
 * Classe responsável por instanciar as stateMachines do Player.
 * Aqui encontar-se-ão as funções responsáveis pelas ações do Player.
 *
 * @autor: Edgar Silva
 */
public class PlayerAgent implements Updateable {


    protected Body body;
    private TransformComponent transform;
    public PlayerCollisionComponent sensors;
    public AttackComponent attackComp;
    public AnimationComponent animComp;

    protected static StateMachine<PlayerAgent, PlayerState> stateMachine;
    protected static StateMachine<PlayerAgent, PlayerAttackState> attackStateMachine;

    public boolean isTouchingGround = true;
    public boolean isTouchingWallLeft = false;
    public boolean isTouchingWallRight = false;
    public static boolean attacking = false;

    public static boolean finishedAnimation = false;
    public long lastAttack = 0;
    public static float timer = 0.0f;

    public Entity attack;

    public PlayerAgent(Entity player) {
        body = player.getComponent(BodyComponent.class).body;
        transform = player.getComponent(TransformComponent.class);
        sensors = player.getComponent(PlayerCollisionComponent.class);
        attackComp = player.getComponent(AttackComponent.class);
        animComp = player.getComponent(AnimationComponent.class);

        stateMachine = new DefaultStateMachine<PlayerAgent, PlayerState>(this, PlayerState.Idle);
        attackStateMachine = new DefaultStateMachine<PlayerAgent, PlayerAttackState>(this, PlayerAttackState.NONE);
        lastAttack = 0;
    }

    @Override
    public void update(float deltaTime) {
        isTouchingGround = (sensors.numFoot > 0);
        isTouchingWallLeft = (sensors.numLeftWall > 0);
        isTouchingWallRight = (sensors.numRightWall > 0);

        stateMachine.update();
        attackStateMachine.update();


        if (!Controller.left && !Controller.right)
            body.setLinearVelocity(MathUtils.lerp(body.getLinearVelocity().x, 0, 0.2f), body.getLinearVelocity().y);
    }


    public static PlayerState getCurrentState() {
        return stateMachine.getCurrentState();
    }

    public static PlayerAttackState getAttackState() {
        return attackStateMachine.getCurrentState();
    }

    public static PlayerState getLastState() {
        return stateMachine.getPreviousState();
    }


    public boolean moveOnGround() {
        //Verificar a tecla "up" está pressionada
        if (Controller.up) {
            body.applyLinearImpulse(0, 2.7f, body.getWorldCenter().x, body.getWorldCenter().y, true);
            Controller.up = false;
        }
        if (Controller.left && !isTouchingWallLeft)
            body.setLinearVelocity(MathUtils.lerp(body.getLinearVelocity().x, -3f, 0.1f), body.getLinearVelocity().y);
        if (Controller.right && !isTouchingWallRight)
            body.setLinearVelocity(MathUtils.lerp(body.getLinearVelocity().x, 3f, 0.1f), body.getLinearVelocity().y);


        return (Controller.left || Controller.right);
    }

    public boolean moveOnAir() {
        if (Controller.left && !isTouchingWallLeft) {
            body.setLinearVelocity(MathUtils.lerp(body.getLinearVelocity().x, -1.5f, 0.1f), body.getLinearVelocity().y);
            transform.flipX = true;
        }
        if (Controller.right && !isTouchingWallRight) {
            transform.flipX = false;
            body.setLinearVelocity(MathUtils.lerp(body.getLinearVelocity().x, 1.5f, 0.1f), body.getLinearVelocity().y);
        }
        return (Controller.left || Controller.right);
    }

    public boolean jumpOnAir() {
        if (Controller.up) {
            body.applyLinearImpulse(0, 2.5f, body.getWorldCenter().x, body.getWorldCenter().y, true);
            Controller.up = false;
            return true;
        }
        return false;
    }

    public void makeAttackFixture(PlayerAttackState state) {
        attack = EntitiesFactory.createAttack(state);
    }

    public void destroyAttackFixture() {
        if (attack != null)
            EntityManager.setToDestroy(attack);
    }

}
