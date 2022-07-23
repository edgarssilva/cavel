package com.edgarsilva.pixelgame.engine.ai.fsm.boss;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;

public enum WitchState implements State<WitchAgent> {


    Attacking(){
        @Override
        public void enter(WitchAgent agent) {
            super.enter(agent);
        }

        @Override
        public void update(WitchAgent agent) {
            if (agent.finishedAnimation) {
                EntityManager.setToDestroy(agent.attack);
                agent.timer = 0f;
                agent.attack();
            }
        }
    }
;

    @Override
    public void enter(WitchAgent agent) {
        agent.timer = 0;
    }

    @Override
    public void update(WitchAgent agent) {
    }

    @Override
    public void exit(WitchAgent agent) {

    }

    @Override
    public boolean onMessage(WitchAgent agent, Telegram telegram) {
        return false;
    }
}
