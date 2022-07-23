package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.edgarsilva.pixelgame.engine.utils.managers.CameraManager;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;

public enum EnemyState implements State<EnemyAgentComponent> {

    IDLE(){
        @Override
        public void enter(EnemyAgentComponent agent) {
            super.enter(agent);
        }

        @Override
        public void update(EnemyAgentComponent agent) {
        }
    },

    Seeking(){
        @Override
        public void enter(EnemyAgentComponent agent) {
            super.enter(agent);
        }

        @Override
        public void update(EnemyAgentComponent agent) {

            if (agent.attackableEntities.size > 0) agent.stateMachine.changeState(Attacking);

            if  (agent.isTouchingWallRight  || !agent.hasGroundRight ){
                agent.moveToLeft = true;
            }else if  (agent.isTouchingWallLeft || !agent.hasGroundLeft){
                agent.moveToLeft = false;
            }

            if (agent.moveToLeft){
                agent.moveLeft();
            }else{
                agent.moveRight();
            }

            /*if (agent.target == null) return;
            if (agent.target.type == Node.Type.LEFT) {
                agent.moveLeft();
            }if (agent.target.type == Node.Type.RIGHT) {
                agent.moveRight();
            }*/
        }
    },

    Attacking(){
        boolean attack = false;

        @Override
        public void enter(EnemyAgentComponent agent) {
            super.enter(agent);
        }

        @Override
        public void update(EnemyAgentComponent agent) {
            if (agent.anim.isAnimationFinished(agent.timer * 2)) {
                if (!attack){ agent.attack(); attack = true; }
                if (agent.anim.isAnimationFinished(agent.timer)) {
                    if (agent.attackableEntities.size > 0) {
                        attack = false;
                        agent.timer = 0;
                    } else agent.stateMachine.changeState(Seeking);
                }
            }
        }

        @Override
        public void exit(EnemyAgentComponent agent) {

        }
    },

    Dying(){
        @Override
        public void enter(EnemyAgentComponent agent) {
            super.enter(agent);
            CameraManager.shake(250, 450);
        }

        @Override
        public void update(EnemyAgentComponent agent) {
            if (agent.anim.isAnimationFinished(agent.timer)) {
                agent.stateMachine.changeState(IDLE);
                EntityManager.setToDestroy(agent.entity);
            }
        }

        @Override
        public void exit(EnemyAgentComponent agent) {
        }
    },

    Hit(){
        @Override
        public void enter(EnemyAgentComponent agent) {
            super.enter(agent);
        }

        @Override
        public void update(EnemyAgentComponent agent) {
            if (agent.anim.isAnimationFinished(agent.timer)) {
                agent.stateMachine.changeState(Seeking);
            }
        }
    },
    ;

    @Override
    public void enter(EnemyAgentComponent agent) {
        agent.timer = 0f;
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
