package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.edgarsilva.pixelgame.engine.utils.managers.CameraManager;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;

public enum EnemyState implements State<EnemyAgent> {


    IDLE(){
        @Override
        public void enter(EnemyAgent agent) {
            super.enter(agent);
        }

        @Override
        public void update(EnemyAgent agent) {
        }
    },

    Seeking(){
        @Override
        public void enter(EnemyAgent agent) {
            super.enter(agent);
        }

        @Override
        public void update(EnemyAgent agent) {

            if (agent.attackable()) agent.stateMachine.changeState(Attacking);

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
        boolean attack;

        @Override
        public void enter(EnemyAgent agent) {
            super.enter(agent);
            attack = false;
        }

        @Override
        public void update(EnemyAgent agent) {
            if (agent.animationDuration * 0.7f <= agent.timer) {
                if (!attack) agent.attack(); attack = true;
                if (agent.finishedAnimation)
                    agent.stateMachine.changeState(Seeking);
            }
        }

        @Override
        public void exit(EnemyAgent agent) {

        }
    },

    Dying(){
        @Override
        public void enter(EnemyAgent agent) {
            super.enter(agent);
            CameraManager.shake(250, 450);
        }

        @Override
        public void update(EnemyAgent agent) {
            if (agent.finishedAnimation) {
                agent.stateMachine.changeState(IDLE);
                agent.spawnDropables();
                EntityManager.setToDestroy(agent.owner);
            }
        }

        @Override
        public void exit(EnemyAgent agent) {
        }
    },

    Hit(){
        @Override
        public void enter(EnemyAgent agent) {
            super.enter(agent);
        }

        @Override
        public void update(EnemyAgent agent) {
            if (agent.finishedAnimation) {
                agent.stateMachine.changeState(Seeking);
            }
        }
    },
    ;

    @Override
    public void enter(EnemyAgent agent) {
        agent.timer = 0f;
    }

    @Override
    public void update(EnemyAgent agent) {
       /* if (agent.node.type == Node.Type.LEFT || agent.node.type == Node.Type.RIGHT) {
            agent.target = agent.node.getConnections().get(0).getToNode();
            System.out.println("Entrou");
        }*/
    }

    @Override
    public void exit(EnemyAgent agent) {

    }

    @Override
    public boolean onMessage(EnemyAgent agent, Telegram telegram) {
        if (telegram.message == 0) {
            agent.attackableEntities.clear();
            return true;
        }

        return false;
    }
}
