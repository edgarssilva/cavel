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
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.engine.ai.fsm.EnemyAgent;
import com.edgarsilva.pixelgame.engine.ai.fsm.PlayerAgent;
import com.edgarsilva.pixelgame.engine.ecs.components.PlayerCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.TransformComponent;
import com.edgarsilva.pixelgame.engine.ecs.systems.CoinSystem;
import com.edgarsilva.pixelgame.preferences.GameSave;
import com.edgarsilva.pixelgame.screens.PlayScreen;

import java.util.HashMap;

public class Save{

    public Array<PlayerSave> playerSaves = new Array<PlayerSave>();
    public Array<EnemySave> enemySaves = new Array<EnemySave>();

    public int coins;
    public String map;

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

        ImmutableArray<Entity> enemies = screen.getEngine().getEntitiesFor(Family.all(EnemyAgent.class).get());

        for (Entity enemy : enemies) {
            TransformComponent  tfc = enemy.getComponent(TransformComponent.class);
            EnemyAgent ec  = enemy.getComponent(EnemyAgent.class);

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

        HashMap<String, String> parameters = new HashMap<String, String>();

        output = Base64Coder.encodeString(output);

        parameters.put("username", screen.getGame().getPreferences().account.getUser());
        parameters.put("password", screen.getGame().getPreferences().account.getPass());
        parameters.put("save", output);

        Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
        httpPost.setHeader("Access-Control-Allow-Methods","POST");
        httpPost.setHeader("Access-Control-Allow-Origin", "*");
        httpPost.setUrl("http://papspaghetti.000webhostapp.com/inGame/save.php");
        httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));

        Gdx.net.sendHttpRequest(httpPost, LoginManager.DEFAULT_LISTENER);
    }

    public static Save load(String serialization){
        return new Json().fromJson(Save.class, serialization);
    }

    public static boolean loadFromServer(PixelGame game){
        HashMap<String, String> parameters = new HashMap<String, String>();

        parameters.put("username", game.getPreferences().account.getUser());
        parameters.put("password", game.getPreferences().account.getPass());

        Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
        httpPost.setHeader("Access-Control-Allow-Methods","POST");
        httpPost.setHeader("Access-Control-Allow-Origin", "*");
        httpPost.setUrl("http://papspaghetti.000webhostapp.com/inGame/load.php");
        httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));

        Gdx.net.sendHttpRequest(httpPost, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                String result = httpResponse.getResultAsString();

                if (result.equals("2") || result.equals("3")) {
                    System.out.println(result);
                    return;
                }
                final String serilization = Base64Coder.decodeString(result);

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        PixelGame.serverSave = load(serilization);
                    }
                });

            }

            @Override
            public void failed(Throwable t) {
                if(t.getCause() != null)
                    System.out.println(t.getMessage());
            }

            @Override
            public void cancelled() {

            }
        });
        return true;
    }
}


