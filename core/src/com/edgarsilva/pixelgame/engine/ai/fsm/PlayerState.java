package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum PlayerState implements State<PlayerAgent> {

    Idle() {
        @Override
        public void enter(PlayerAgent agent) {
            super.enter(agent);
        }

        @Override
        public void update(PlayerAgent agent) {
            agent.moveOnGround();
            if (!agent.isTouchingGround) {
                if (agent.body.getLinearVelocity().y < -0.05)
                    PlayerAgent.stateMachine.changeState(Falling);
                else
                    PlayerAgent.stateMachine.changeState(Jumping);
            } else {
                if (agent.body.getLinearVelocity().x != 0)
                    PlayerAgent.stateMachine.changeState(Walking);
            }
        }
    },

    Walking() {
        @Override
        public void enter(PlayerAgent agent) {
            super.enter(agent);
        }

        @Override
        public void update(PlayerAgent agent) {
            agent.moveOnGround();
            if (!agent.isTouchingGround) {
                if (agent.body.getLinearVelocity().y < -0.05)
                    PlayerAgent.stateMachine.changeState(Falling);
                else
                    PlayerAgent.stateMachine.changeState(Jumping);
            } else {
                if (agent.body.getLinearVelocity().x == 0)
                    PlayerAgent.stateMachine.changeState(Idle);
            }
        }
    },

    Jumping() {
        @Override
        public void enter(PlayerAgent agent) {
            super.enter(agent);
        }

        @Override
        public void update(PlayerAgent agent) {
            agent.moveOnAir();
            if (agent.body.getLinearVelocity().y < 0)
                PlayerAgent.stateMachine.changeState(Falling);
            /* else if (agent.jumpOnAir())
                agent.stateMachine.changeState(DoubleJumping);*/
        }
    },

    DoubleJumping() {
        @Override
        public void enter(PlayerAgent agent) {
            super.enter(agent);
        }

        @Override
        public void update(PlayerAgent agent) {
            agent.moveOnAir();
            if (agent.body.getLinearVelocity().y < 0)
                PlayerAgent.stateMachine.changeState(Falling);
        }
    },

    Falling() {
        @Override
        public void enter(PlayerAgent agent) {
            super.enter(agent);
        }

        @Override
        public void update(PlayerAgent agent) {
            agent.moveOnAir();
            if (agent.isTouchingGround) {
                PlayerAgent.stateMachine.changeState(Idle);
            } else {
                if (PlayerAgent.stateMachine.getPreviousState() != DoubleJumping) {
                    if (agent.jumpOnAir())
                        PlayerAgent.stateMachine.changeState(DoubleJumping);
                }
            }
        }
    };

    @Override
    public void enter(PlayerAgent agent) {
        if (!PlayerAgent.attacking) PlayerAgent.timer = 0f;
    }

    @Override
    public void update(PlayerAgent agent) {
    }

    @Override
    public void exit(PlayerAgent agent) {

    }

    @Override
    public boolean onMessage(PlayerAgent agent, Telegram telegram) {
        return false;
    }
}