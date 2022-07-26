package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.edgarsilva.pixelgame.engine.utils.controllers.Controller;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.screens.PlayScreen;

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
        Sound sound;
        @Override
        public void enter(PlayerAgent agent) {
            //  sound = PlayScreen.getGame().assets.manager.get(GameAssetsManager.)
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
            Sound sound = PlayScreen.getGame().assets.manager.get(GameAssetsManager.jump, Sound.class);

            sound.play(PlayScreen.getGame().getPreferences().sound.getSoundVolume());
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
            Sound sound = PlayScreen.getGame().assets.manager.get(GameAssetsManager.doublejump, Sound.class);

            sound.play(PlayScreen.getGame().getPreferences().sound.getSoundVolume());
        }

        @Override
        public void update(PlayerAgent agent) {
            agent.moveOnAir();
            if (agent.body.getLinearVelocity().y < 0) {
                if(PlayerAgent.fallAttack) agent.attackStateMachine.changeState(PlayerAttackState.FallingAttack);
                PlayerAgent.stateMachine.changeState(Falling);
            }

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
    },

    Dying(){
        @Override
        public void enter(PlayerAgent agent) {
            super.enter(agent);
            for (Fixture fix : agent.body.getFixtureList())
                fix.setUserData(null);

            agent.body.setLinearVelocity(0,0);

            Controller.attack = false;
            Controller.left   = false;
            Controller.right  = false;
            Controller.down   = false;
            Controller.up     = false;
        }

        @Override
        public void update(PlayerAgent agent) {
            agent.die();
        }

        @Override
        public void exit(PlayerAgent agent) {
        }
    },

    Hit(){
        @Override
        public void enter(PlayerAgent agent) {
            super.enter(agent);
        }

        @Override
        public void update(PlayerAgent agent) {
            if (agent.finishedAnimation) {
                PlayerAgent.stateMachine.changeState(Idle);
            }
        }
    },
    ;

    @Override
    public void enter(PlayerAgent agent) {
        if (!PlayerAgent.attacking) agent.timer = 0f;
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