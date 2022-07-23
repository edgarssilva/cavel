package com.edgarsilva.pixelgame.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgentComponent;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAgent;
import com.edgarsilva.pixelgame.engine.ecs.components.PlayerCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.CoinSystem;
import com.edgarsilva.pixelgame.preferences.GameSave;
import com.edgarsilva.pixelgame.screens.PlayScreen;

import java.util.HashMap;
import java.util.Map;

public class Save{

    public Array<PlayerSave> playerSaves = new Array<PlayerSave>();
    public Array<EnemySave> enemySaves = new Array<EnemySave>();

    public int coins;
    public  String map;

    Save(){}

    public Save(PlayScreen screen) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.minimal);

        ImmutableArray<Entity> players = screen.getEngine().getEntitiesFor(Family.all(PlayerCollisionComponent.class).get());

        for (Entity player : players) {
            TransformComponent tfc = player.getComponent(TransformComponent.class);
            playerSaves.add(new PlayerSave(
                    tfc.position.x,
                    tfc.position.y,
                    tfc.flipX,
                    player.getComponent(StatsComponent.class),
                    PlayerAgent.getCurrentState().name(),
                    PlayerAgent.getAttackState().name()
            ));
        }

        ImmutableArray<Entity> enemies = screen.getEngine().getEntitiesFor(Family.all(EnemyAgentComponent.class).get());

        for (Entity enemy : enemies) {
            TransformComponent  tfc = enemy.getComponent(TransformComponent.class);
            EnemyAgentComponent ec  = enemy.getComponent(EnemyAgentComponent.class);

            enemySaves.add(new EnemySave(
                    tfc.position.x,
                    tfc.position.y,
                    tfc.flipX,
                    enemy.getComponent(StatsComponent.class),
                    ec.moveToLeft,
                    ec.stateMachine.getCurrentState().name(),
                    ec.enemyType.name()
            ));
        }

        coins = CoinSystem.coins;
        map   = screen.getMap();
        String output = json.toJson(this, Save.class);

        GameSave.save(output);

        Map parameters = new HashMap();
        output = Base64Coder.encodeString(output);
        parameters.put("username", "admin");
        parameters.put("password", "admin");
        parameters.put("save", output);

        Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
      //  httpPost.setHeader("Access-Control-Allow-Methods","GET");
        httpPost.setHeader("Access-Control-Allow-Origin", "*");
        httpPost.setUrl("http://papspaghetti.000webhostapp.com/inGame/save.php");//?username=admin&password=admin&save="+output);
        httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));

        Gdx.net.sendHttpRequest(httpPost, LoginManager.DEFAULT_LISTENER);

    }

    public static Save load(){
        System.out.println(new Json().prettyPrint(GameSave.load()));
        return new Json().fromJson(Save.class, GameSave.load());
    }

}


