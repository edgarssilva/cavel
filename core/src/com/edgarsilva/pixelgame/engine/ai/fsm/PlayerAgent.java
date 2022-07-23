package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.PlayerCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StateAnimationComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.utils.controllers.Controller;
import com.edgarsilva.pixelgame.engine.utils.factories.EntitiesFactory;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.engine.utils.managers.LevelManager;
import com.edgarsilva.pixelgame.screens.PlayScreen;

/**
 * Classe responsável por instanciar as stateMachines do Player.
 * Aqui encontar-se-ão as funções responsáveis pelas ações do Player.
 *
 * @autor: Edgar Silva
 */
public class PlayerAgent extends Agent {

    protected Body body;

    private TransformComponent transform;
    public PlayerCollisionComponent sensors;
    public StateAnimationComponent animComp;
    public static StatsComponent statsComp;
    public BodyComponent bodyComp;

    private ComponentMapper<StatsComponent> statsCompMap;
    private ComponentMapper<BodyComponent> bodyCompMap;

    protected static StateMachine<PlayerAgent, PlayerState> stateMachine;
    protected static StateMachine<PlayerAgent, PlayerAttackState> attackStateMachine;

    public boolean isTouchingGround = true;
    public boolean isTouchingWallLeft = false;
    public boolean isTouchingWallRight = false;
    public static boolean attacking = false;

    public long lastAttack = 0l;
    public static boolean fallAttack = false;

    public Entity attack;

    public float deltaTime;

    public PlayerAgent() {
    }

    public PlayerAgent(Entity player, PlayerState state, PlayerAttackState attackState) {
        this.owner = player;

        statsCompMap = ComponentMapper.getFor(StatsComponent.class);
        bodyComp = player.getComponent(BodyComponent.class);

        body = bodyComp.body;
        transform = player.getComponent(TransformComponent.class);
        sensors = player.getComponent(PlayerCollisionComponent.class);
        animComp = player.getComponent(StateAnimationComponent.class);
        statsComp = statsCompMap.get(player);

        stateMachine = new DefaultStateMachine<PlayerAgent, PlayerState>(this, state);
        attackStateMachine = new DefaultStateMachine<PlayerAgent, PlayerAttackState>(this, attackState);
        lastAttack = 0;
    }


    @Override
    public void update(float deltaTime) {
        this.deltaTime = deltaTime;


        isTouchingGround = (sensors.numFoot > 0);
        isTouchingWallLeft = (sensors.numLeftWall > 0);
        isTouchingWallRight = (sensors.numRightWall > 0);

        stateMachine.update();
        attackStateMachine.update();

        if (attackStateMachine.getCurrentState() != PlayerAttackState.NONE)
            animationState = attackStateMachine.getCurrentState();
        else
            animationState = stateMachine.getCurrentState();

        if (bodyComp == null && bodyCompMap.has(owner)) bodyComp = bodyCompMap.get(owner);
        if (body.getPosition().x > LevelManager.lvlMeterWidth) PlayScreen.levelComplete();
    }

    public static void reset(Vector2 position) {
        EntityManager.getPlayer().getComponent(BodyComponent.class).body.setTransform(position, 0f);
        stateMachine.setGlobalState(PlayerState.Walking);
        attackStateMachine.setGlobalState(PlayerAttackState.NONE);
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

    public static void hit(StatsComponent enemyStats) {
        //Proteção para não contar o ataque mais que uma vez
          if (stateMachine.isInState(PlayerState.Hit) || stateMachine.isInState(PlayerState.Dying)) {
              if (/*timer < 0.05f || */stateMachine.isInState(PlayerState.Dying)) return;
          }
        if (statsComp.health - enemyStats.damage <= 0) {
            stateMachine.changeState(PlayerState.Dying);
        }else{
            stateMachine.changeState(PlayerState.Hit);
            statsComp.attack(enemyStats);
        }
    }


    public static void kill() {
        stateMachine.changeState(PlayerState.Dying);
    }

    public boolean moveOnGround() {
        float speedX = body.getLinearVelocity().x;
        float desiredSpeedX = 0f;
        float impulseY = 0f;


        if (Controller.left && !isTouchingWallLeft)
            desiredSpeedX = -2f;
        if (Controller.right && !isTouchingWallRight)
            desiredSpeedX = 2f;
        if (attackStateMachine.getCurrentState() != PlayerAttackState.NONE)
            desiredSpeedX *= 0.1f;


        if (Controller.up && attackStateMachine.getCurrentState() != PlayerAttackState.FallAttack) {
            impulseY = 5f * body.getMass();
            Controller.up = false;
        }

        float speedChange = desiredSpeedX - speedX;
        float impulse = body.getMass() * speedChange;

        body.applyLinearImpulse(new Vector2(impulse, impulseY), body.getWorldCenter(), true);

        return (Controller.left || Controller.right);
    }

    public boolean moveOnAir() {
        float speedX = body.getLinearVelocity().x;
        float desiredSpeedX = 0f;

        if (Controller.left && !isTouchingWallLeft)
            desiredSpeedX = -1.5f;
        if (Controller.right && !isTouchingWallRight)
            desiredSpeedX =  1.5f;

        float speedChange = desiredSpeedX - speedX;
        float impulse = body.getMass() * speedChange;

        body.applyLinearImpulse(new Vector2(impulse, 0f), body.getWorldCenter(), true);

        return (Controller.left || Controller.right);
    }

    public boolean jumpOnAir() {
        if (Controller.up) {
            body.applyLinearImpulse(0, 4.5f * body.getMass(), body.getWorldCenter().x, body.getWorldCenter().y, true);
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

    public void die() {
        statsComp.health = 0;
    }

    @Override
    public void hit() {
        if (stateMachine.isInState(PlayerState.Hit) || stateMachine.isInState(PlayerState.Dying)) {
            if (timer < 0.05f || stateMachine.isInState(PlayerState.Dying)) return;
        }
        if (statsComp.health - 1 <= 0) {
            statsComp.health -= 1;
            stateMachine.changeState(PlayerState.Dying);
        }else{
            stateMachine.changeState(PlayerState.Hit);
            statsComp.health -= 1;
        }
    }
}
