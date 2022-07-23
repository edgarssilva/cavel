package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.audio.Sound;
import com.edgarsilva.pixelgame.engine.utils.controllers.Controller;
import com.edgarsilva.pixelgame.engine.utils.managers.CameraManager;
import com.edgarsilva.pixelgame.managers.GameAssetsManager;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public enum PlayerAttackState implements State<PlayerAgent> {


    NONE() {
        @Override
        public void enter(PlayerAgent agent) {
            Controller.attack = false;
            PlayerAgent.attacking = false;
            agent.lastAttack = System.currentTimeMillis();
        }

        @Override
        public void update(PlayerAgent agent) {
            if (Controller.attack) {
                PlayerAttackState nextState;
                PlayerAttackState previousState = PlayerAgent.attackStateMachine.getPreviousState();

                if (System.currentTimeMillis() - agent.lastAttack > 200 || previousState == null)
                    previousState = NONE;

                if (agent.isTouchingGround) {
                    switch (previousState) {
                        case Attack1:
                            nextState = Attack2;
                            break;
                        case Attack2:
                            nextState = Attack3;
                            break;
                        default:
                            nextState = Attack1;
                            break;
                    }
                } else {
                    switch (previousState) {
                        case AirAttack1:
                            nextState = AirAttack2;
                            break;
                        default:
                            nextState = AirAttack1;
                            break;
                    }

                }

                if (PlayerAgent.getCurrentState() == PlayerState.DoubleJumping)
                    return;
                else if (PlayerAgent.getCurrentState() == PlayerState.Falling
                        && PlayerAgent.getLastState() == PlayerState.DoubleJumping
                        && PlayerAgent.attackStateMachine.getPreviousState() != FallingAttack)
                    nextState = FallingAttack;

                agent.makeAttackFixture(nextState);
                PlayerAgent.timer = 0f;
                PlayerAgent.attacking = true;
                PlayerAgent.attackStateMachine.changeState(nextState);
            }
        }

    },

    Attack1() {
        @Override
        public void enter(PlayerAgent agent) {
           Sound sound = PlayScreen.getGame().assets.manager.get(GameAssetsManager.attack1, Sound.class);

           sound.play(PlayScreen.getGame().getPreferences().sound.getSoundVolume());
        }

        @Override
        public void update(PlayerAgent agent) {
            super.update(agent);
        }

    },

    Attack2() {
        @Override
        public void enter(PlayerAgent agent) {
            Sound sound = PlayScreen.getGame().assets.manager.get(GameAssetsManager.attack2, Sound.class);

            sound.play(PlayScreen.getGame().getPreferences().sound.getSoundVolume());
        }
        @Override
        public void update(PlayerAgent agent) {
            super.update(agent);
        }

    },

    Attack3() {
        @Override
        public void enter(PlayerAgent agent) {
            Sound sound = PlayScreen.getGame().assets.manager.get(GameAssetsManager.attack3, Sound.class);

            sound.play(PlayScreen.getGame().getPreferences().sound.getSoundVolume());
        }
        @Override
        public void update(PlayerAgent agent) {
            super.update(agent);
        }
    },

    AirAttack1() {
        @Override
        public void update(PlayerAgent agent) {
            super.update(agent);
        }
    },

    AirAttack2() {
        @Override
        public void update(PlayerAgent agent) {
            super.update(agent);
        }

    },

    FallingAttack() {
        @Override
        public void update(PlayerAgent agent) {
            if (!Controller.attack) PlayerAgent.attackStateMachine.changeState(NONE);

            if (agent.isTouchingGround) {
                PlayerAgent.timer = 0f;
                PlayerAgent.attackStateMachine.changeState(FallAttack);
            }
        }
    },

    FallAttack() {
        @Override
        public void enter(PlayerAgent agent) {
            agent.makeAttackFixture(FallAttack);
            CameraManager.shake(300, 300);
        }

        @Override
        public void update(PlayerAgent agent) {
            super.update(agent);
        }

    },
    ;

    @Override
    public void enter(PlayerAgent agent) {

    }

    @Override
    public void update(PlayerAgent agent) {
        if (PlayerAgent.finishedAnimation) {
            PlayerAgent.attackStateMachine.changeState(NONE);
            agent.destroyAttackFixture();
        }
    }

    @Override
    public void exit(PlayerAgent agent) {

    }

    @Override
    public boolean onMessage(PlayerAgent agent, Telegram telegram) {
        return false;
    }
}
