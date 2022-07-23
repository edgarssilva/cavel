package com.edgarsilva.pixelgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;

import java.util.HashMap;
import java.util.Map;

public class LoginManager {

    public static final int SUCCESS           = 0;
    public static final int FAILED_SAVING     = 1;
    public static final int WRONG_CREDENTIALS = 2;
    public static final int WRONG_DATA        = 3;



    public static final Net.HttpResponseListener DEFAULT_LISTENER = new Net.HttpResponseListener() {
        @Override
        public void handleHttpResponse(Net.HttpResponse httpResponse) {
            System.out.println(httpResponse.getResultAsString());
            int value = Integer.parseInt(httpResponse.getResultAsString());

            switch (value) {
                case LoginManager.WRONG_DATA:
                    System.out.println("Username or Password not set !");
                    break;
                case LoginManager.SUCCESS:
                    System.out.println("Success !");
                    break;
                case LoginManager.WRONG_CREDENTIALS:
                    System.out.println("Wrong Credentials !");
                    break;
                case LoginManager.FAILED_SAVING:
                    System.out.println("Attempt to Login Failed !");
                    break;
                default:
                    System.out.println("Error Connection with Server !");
                    break;
            }

        }

        @Override
        public void failed(Throwable t) {
            System.out.println(t.getCause());
            System.out.println("Connection Failed");

        }

        @Override
        public void cancelled() {
            System.out.println("Connection Canceled");
        }
    };


    public static void login(String user, String password, String save, Net.HttpResponseListener httpResponseListener ) {
        Map parameters = new HashMap();

        parameters.put("username", user);
        parameters.put("password", password);
        parameters.put("save", save);

        Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
        httpPost.setHeader("Access-Control-Allow-Methods","POST");
        httpPost.setHeader("Access-Control-Allow-Origin", "*");
        httpPost.setUrl("https://papspaghetti.000webhostapp.com/inGame/save.php");
        httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));

        Gdx.net.sendHttpRequest(httpPost, httpResponseListener);
    }

}
