package com.edgarsilva.pixelgame.engine.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.edgarsilva.pixelgame.engine.ai.fsm.Enemies;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgent;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyState;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAttackState;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerState;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.utils.factories.EntitiesFactory;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.managers.EnemySave;
import com.edgarsilva.pixelgame.managers.PlayerSave;
import com.edgarsilva.pixelgame.managers.Save;
import com.edgarsilva.pixelgame.screens.PlayScreen;

public class GameLoader {

    public static void loadGame(Save save, PlayScreen playscreen){
        for (PlayerSave player : save.playerSaves) {
            EntitiesFactory.createPlayer(
                    new Vector2(player.x,player.y),
                    PlayerState.valueOf(player.stateName),
                    PlayerAttackState.valueOf(player.attackStateName)
            );

            Entity entity = EntityManager.getPlayer();
            StatsComponent stats = entity.getComponent(StatsComponent.class);
            stats.maxHealth = player.stats.maxHealth;
            stats.health = player.stats.health;
            stats.damage = player.stats.damage;
            entity.getComponent(TransformComponent.class).flipX = player.flipX;
        }

        for (EnemySave enemy : save.enemySaves) {
            if (enemy.enemyTypeName.equals(Enemies.SKELETON.name())) {
                Entity entity = EntitiesFactory.createEnemy(Enemies.SKELETON, new Vector2(enemy.x, enemy.y));

                StatsComponent stats = entity.getComponent(StatsComponent.class);
                stats.maxHealth = enemy.stats.maxHealth;
                stats.health = enemy.stats.health;
                stats.damage = enemy.stats.damage;

                entity.getComponent(TransformComponent.class).flipX = enemy.flipX;
                entity.getComponent(EnemyAgent.class).stateMachine.changeState(EnemyState.valueOf(enemy.stateName));
                entity.getComponent(EnemyAgent.class).moveToLeft = enemy.moveToLeft;
            }
        }
    }

}
