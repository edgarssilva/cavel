package com.edgarsilva.pixelgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;

import java.util.HashMap;
import java.util.Map;

public class LoginManager {

    public static final int WRONG_CREDENTIALS = 0;
    public static final int SUCCESS = 1;
    public static final int NO_DATA = 2;

    public static final Net.HttpResponseListener DEFAULT_LISTENER = new Net.HttpResponseListener() {
        @Override
        public void handleHttpResponse(Net.HttpResponse httpResponse) {
            int value = Integer.parseInt(httpResponse.getResultAsString());

            switch (value) {
                case LoginManager.NO_DATA:
                    System.out.println("Username or Password not set");
                    break;
                case LoginManager.SUCCESS:
                    System.out.println("Success");
                    break;
                case LoginManager.WRONG_CREDENTIALS:
                    System.out.println("Wrong Credentials");
                    break;
                default:
                    System.out.println("Error");
                    break;
            }

        }

        @Override
        public void failed(Throwable t) {
            System.out.println("Connection Failed");

        }

        @Override
        public void cancelled() {
            System.out.println("Connection Canceled");
        }
    };


    public static void login(String user, String password, Net.HttpResponseListener httpResponseListener ) {
        Map parameters = new HashMap();

        parameters.put("username", user);
        parameters.put("password", password);

        Net.HttpRequest httpGet = new Net.HttpRequest(Net.HttpMethods.POST);
        // httpGet.setHeader();
        httpGet.setUrl("http://altiagofaria.000webhostapp.com/inGame/login.php");
        httpGet.setContent(HttpParametersUtils.convertHttpParameters(parameters));

        Gdx.net.sendHttpRequest(httpGet, httpResponseListener);

    }

}
