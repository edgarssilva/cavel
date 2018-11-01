package com.edgarsilva.pixelgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;

import java.util.HashMap;
import java.util.Map;

public class LoginManager {

    public static Net.HttpRequest login(String user, String password ) {
        Map parameters = new HashMap();

        parameters.put("username", user);
        parameters.put("password", password);

        Net.HttpRequest httpGet = new Net.HttpRequest(Net.HttpMethods.POST);
        // httpGet.setHeader();
        httpGet.setUrl("http://altiagofaria.000webhostapp.com/inGame/login.php");
        httpGet.setContent(HttpParametersUtils.convertHttpParameters(parameters));

        return httpGet;
    }

}
