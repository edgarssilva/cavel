package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum EnemyState implements State<EnemyAgentComponent> {

    IDLE(){
        @Override
        public void update(EnemyAgentComponent agent
        ) {

        }
    },
    Seeking,
    Attacking,
    Die,
    Hit,


    ;

    @Override
    public void enter(EnemyAgentComponent agent
    ) {

    }

    @Override
    public void update(EnemyAgentComponent agent
    ) {

    }

    @Override
    public void exit(EnemyAgentComponent agent
    ) {

    }

    @Override
    public boolean onMessage(EnemyAgentComponent agent, Telegram telegram) {
        return false;
    }
}
