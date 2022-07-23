package com.edgarsilva.pixelgame.engine.ai.fsm.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;

public enum WitchState implements State<WitchAgent> {


    Reloading(){
        private float wait = 3f;

        @Override
        public void enter(WitchAgent agent) {
            super.enter(agent);
            wait = 3f;
        }

        @Override
        public void update(WitchAgent agent) {
            super.update(agent);
            if (wait <= 0f){
                agent.stateMachine.changeState(Attacking);
            }else{
                wait -= Gdx.graphics.getDeltaTime();
            }
        }

        @Override
        public void exit(WitchAgent agent) {
            super.exit(agent);
        }
    },

    Walking(){

        @Override
        public void enter(WitchAgent agent) {
            super.enter(agent);
        }

        @Override
        public void update(WitchAgent agent) {
            super.update(agent);

        }

        @Override
        public void exit(WitchAgent agent) {
            super.exit(agent);
        }

    },

    Attacking(){
        private boolean finished = false;

        @Override
        public void enter(WitchAgent agent) {
            super.enter(agent);
            agent.attack();
        }

        @Override
        public void update(WitchAgent agent) {
            if (agent.finishedAnimation && !finished) {
                finished = true;
                EntityManager.setToDestroy(agent.attack);
                agent.timer = 0f;
                agent.stateMachine.changeState(Walking);
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
