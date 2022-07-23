package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum EnemyState implements State<EnemyAgentComponent> {

    IDLE(){
        @Override
        public void update(EnemyAgentComponent agent) {

        }
    },
    Seeking(){
        @Override
        public void update(EnemyAgentComponent agent) {
            System.out.println(agent.node.type);
            /*if (agent.target == null) return;
            if (agent.target.type == Node.Type.LEFT) {
                agent.moveLeft();
            }if (agent.target.type == Node.Type.RIGHT) {
                agent.moveRight();
            }*/
        }
    },
    Attacking,
    Die,
    Hit,


    ;

    @Override
    public void enter(EnemyAgentComponent agent) {

    }

    @Override
    public void update(EnemyAgentComponent agent) {
       /* if (agent.node.type == Node.Type.LEFT || agent.node.type == Node.Type.RIGHT) {
            agent.target = agent.node.getConnections().get(0).getToNode();
            System.out.println("Entrou");
        }*/
    }

    @Override
    public void exit(EnemyAgentComponent agent) {

    }

    @Override
    public boolean onMessage(EnemyAgentComponent agent, Telegram telegram) {
        return false;
    }
}
