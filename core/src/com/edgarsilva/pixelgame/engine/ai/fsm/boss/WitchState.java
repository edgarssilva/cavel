package com.edgarsilva.pixelgame.engine.ai.fsm.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.edgarsilva.pixelgame.engine.ai.fsm.Enemies;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.utils.factories.EntitiesFactory;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public enum WitchState implements State<WitchAgent> {

    Walking(){
        float timer;

        @Override
        public void enter(WitchAgent agent) {
            super.enter(agent);
            agent.leftSide =! agent.leftSide;
            timer = agent.random.nextInt(3) + 3;
        }

        @Override
        public void update(WitchAgent agent) {
            super.update(agent);
            if (agent.leftSide) {
                agent.body.setLinearVelocity(-1.5f,0);
            } else{
                agent.body.setLinearVelocity(1.5f, 0);
            }
            timer -= Gdx.graphics.getDeltaTime();

            if (timer <= 0) {
                agent.stateMachine.changeState(Attacking);
            }
        }

        @Override
        public void exit(WitchAgent agent) {
            super.exit(agent);
        }

    },

    Attacking(){
        float timer = 0f;
        boolean attack = false;
        @Override
        public void enter(WitchAgent agent) {
            super.enter(agent);
            timer = 0f;
            attack = false;
        }

        @Override
        public void update(WitchAgent agent) {
            agent.tfComp.flipX = !agent.leftSide;

            if (timer > .7f && !attack) {
                agent.attack();
                attack = true;
            }

            if (agent.finishedAnimation) {
                EntityManager.setToDestroy(agent.attack);
                agent.stateMachine.changeState(Walking);
            }


            if(!PlayScreen.paused){
                timer += Gdx.graphics.getDeltaTime();
                if (timer % 2 < 0.03) {
                    EntitiesFactory.createEnemy(
                            Enemies.SKELETON,
                            agent.body.getPosition().add(
                                    RenderSystem.PixelsToMeters(5 * agent.random.nextInt(4)-2),
                                    RenderSystem.PixelsToMeters(-2)
                            )
                    );
                }
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
