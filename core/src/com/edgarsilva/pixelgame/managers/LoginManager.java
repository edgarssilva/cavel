package com.edgarsilva.pixelgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.edgarsilva.pixelgame.screens.LoginScreen;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase responsável por fazer a coneção entre o jogo e o website.
 */

public class LoginManager {

    public static final int SUCCESS           = 0;
    public static final int FAILED_SAVING     = 1;
    public static final int WRONG_CREDENTIALS = 2;
    public static final int WRONG_DATA        = 3;


    public static final Net.HttpResponseListener DEFAULT_LISTENER = new Net.HttpResponseListener() {
        @Override
        public void handleHttpResponse(Net.HttpResponse httpResponse) {
            String output = httpResponse.getResultAsString();
            System.out.println(output);
            LoginScreen.loadingDialog.hide();
            try {
                switch (Integer.parseInt(output)) {
                   /* case LoginManager.WRONG_DATA:
                        LoginScreen.errorDialog.text("Username or Password not set !");
                        LoginScreen.errorDialog.show(LoginScreen.stage);
                        System.out.println("Username or Password not set !");
                        break;*/
                    case LoginManager.SUCCESS:
                        System.out.println("Success !");
                        LoginScreen.successDialog.show(LoginScreen.stage);
                        break;
                   /* case LoginManager.WRONG_CREDENTIALS:
                        LoginScreen.errorDialog.text("Wrong Credentials !");
                        LoginScreen.errorDialog.show(LoginScreen.stage);
                        System.out.println("Wrong Credentials !");
                        break;
                    case LoginManager.FAILED_SAVING:
                        LoginScreen.errorDialog.text("Attempt to Login Failed !");
                        LoginScreen.errorDialog.show(LoginScreen.stage);
                        System.out.println("Attempt to Login Failed !");
                        break;*/
                    default:
                        System.out.println("Error Connection with Server !");
                        LoginScreen.errorDialog.show(LoginScreen.stage);
                        break;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error Connection with Server !");
                LoginScreen.errorDialog.show(LoginScreen.stage);
            }
        }

        @Override
        public void failed(Throwable t) {
            System.out.println(t);
            LoginScreen.loadingDialog.hide();
            LoginScreen.errorDialog.text("Error Connection with Server !");
            LoginScreen.errorDialog.show(LoginScreen.stage);
        }

        @Override
        public void cancelled() {
            System.out.println("Connection Canceled");
        }
    };


    public static void login(String user, String password, Net.HttpResponseListener httpResponseListener ) {
        HashMap<String, String> parameters = new HashMap<String, String>();

        parameters.put("username", user);
        parameters.put("password", password);

        Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
        httpPost.setHeader("Access-Control-Allow-Methods","POST");
        httpPost.setHeader("Access-Control-Allow-Origin", "*");
        httpPost.setUrl("https://papspaghetti.000webhostapp.com/inGame/login.php");
        httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));

        Gdx.net.sendHttpRequest(httpPost, httpResponseListener);

        LoginScreen.loadingDialog.show(LoginScreen.stage);
    }

    public static void save(String user, String password, String save, Net.HttpResponseListener httpResponseListener ) {
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
