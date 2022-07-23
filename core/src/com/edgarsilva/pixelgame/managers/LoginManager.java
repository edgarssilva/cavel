package com.edgarsilva.pixelgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.edgarsilva.pixelgame.PixelGame;
import com.edgarsilva.pixelgame.screens.LoginScreen;

import java.util.HashMap;
import java.util.Map;

public class LoginManager {

    public static final int SUCCESS           = 0;
    public static final int FAILED_SAVING     = 1;
    public static final int WRONG_CREDENTIALS = 2;
    public static final int WRONG_DATA        = 3;

    private static Dialog dialog;

    public static final Net.HttpResponseListener DEFAULT_LISTENER = new Net.HttpResponseListener() {
        @Override
        public void handleHttpResponse(Net.HttpResponse httpResponse) {
            String output = httpResponse.getResultAsString();
            System.out.println(output);
            dialog.remove();
            try {
                switch (Integer.parseInt(output)) {
                    case LoginManager.WRONG_DATA:
                        LoginScreen.errorDialog.text("Username or Password not set !");
                        LoginScreen.errorDialog.show(LoginScreen.stage);
                        System.out.println("Username or Password not set !");
                        break;
                    case LoginManager.SUCCESS:
                        System.out.println("Success !");
                        LoginScreen.successDialog.show(LoginScreen.stage);
                        break;
                    case LoginManager.WRONG_CREDENTIALS:
                        LoginScreen.errorDialog.text("Wrong Credentials !");
                        LoginScreen.errorDialog.show(LoginScreen.stage);
                        System.out.println("Wrong Credentials !");
                        break;
                    case LoginManager.FAILED_SAVING:
                        LoginScreen.errorDialog.text("Attempt to Login Failed !");
                        LoginScreen.errorDialog.show(LoginScreen.stage);
                        System.out.println("Attempt to Login Failed !");
                        break;
                    default:
                        System.out.println("Error Connection with Server !");
                        LoginScreen.errorDialog.text("Error Connection !");
                        LoginScreen.errorDialog.show(LoginScreen.stage);
                        break;
                }




            } catch (NumberFormatException ex) {
                System.out.println("Error Connection with Server !");
            }
        }

        @Override
        public void failed(Throwable t) {
            System.out.println(t);
            LoginScreen.errorDialog.text("Error");
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
        dialog = new Dialog("Login", PixelGame.assets.getSkin());
        dialog.scaleBy(2f);
        dialog.text("Loading");
        dialog.setMovable(false);
        // dialog.button("OK",true);
        // dialog.key(Input.Keys.ENTER, true);
        // dialog.pack();
        dialog.show(LoginScreen.stage);
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
