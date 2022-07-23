package com.edgarsilva.pixelgame.engine.utils.controllers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

public class JoystickController extends Controller implements ControllerListener {

    /*
     * Square = 0
     * Cross = 1
     * Circle = 2
     * Triangle = 3
     * L1 = 4
     * R1 = 5
     * L2 = 6
     * R2 = 7
     * Share = 8
     * Options = 9
     * L3 = 10
     * R3 = 11
     * PS Button = 12
     * Touch Pad = 13
     */

    public static final int Square    = 0;
    public static final int Cross     = 1;
    public static final int Circle    = 2;
    public static final int Triangle  = 3;
    public static final int L1        = 4;
    public static final int R1        = 5;
    public static final int L2        = 6;
    public static final int R2        = 7;
    public static final int Share     = 8;
    public static final int Options   = 9;
    public static final int L3        = 10;
    public static final int R3        = 11;
    public static final int PS_Button = 12;
    public static final int Touch_Pad = 13;

    /*
     * Right Stick Up = Axis 0, Negative Value
     * Right Stick Down = Axis 0, Positive Value
     * Right Stick Left = Axis 1, Negative Value
     * Right Stick Right = Axis 1, Positive Value
     * Left Stick Up = Axis 2, Negative Value
     * Left Stick Down = Axis 2, Positive Value
     * Left Stick Left = Axis 3, Negative Value
     * Left Stick Right = Axis 3, Positive Value
     */

    public static final int Right_Stick_Up    = /* Axis */  0  /* -1 Value */;
    public static final int Right_Stick_Down  = /* Axis */  0  /* 1 Value */;
    public static final int Right_Stick_Left  = /* Axis */  1  /* -1 Value */;
    public static final int Right_Stick_Right = /* Axis */  2  /* 1 Value */;
    public static final int Left_Stick_Up     = /* Axis */  2  /* -1 Value */;
    public static final int Left_Stick_Down   = /* Axis */  2  /* 1 Value */;
    public static final int Left_Stick_Left   = /* Axis */  3  /* -1 Value */;
    public static final int Left_Stick_Right  = /* Axis */  3  /* 1 Value */;


    /*
     * D-Pad Up = North
     * D-Pad Down = South
     * D-Pad Left = West
     * D-Pad Right = East
     * Nothing down = Center
     * Can combine (eq Up + Left = Northwest, Down + Right = Southeast, etc)
     */

    public static final String D_Pad_Up     =  "  North ";
    public static final String D_Pad_Down   =  "  South ";
    public static final String D_Pad_Left   =  "  West  ";
    public static final String D_Pad_Right  =  "  East  ";
    public static final String Nothing_down =  " Center ";


    public JoystickController() {

        Controllers controllers = new Controllers();
        if (controllers.getControllers().size > 1);

    }

    @Override
    public void connected(com.badlogic.gdx.controllers.Controller controller) {
      if (controller != null) System.out.println("Connect "+ controller.getName());
    }

    @Override
    public void disconnected(com.badlogic.gdx.controllers.Controller controller) {
        if (controller != null) System.out.println("Disconnect "+ controller.getName());
    }

    @Override
    public boolean buttonDown(com.badlogic.gdx.controllers.Controller controller, int buttonCode) {
        switch (buttonCode) {
            case Square:
                attack = true;
                break;
            case Cross:
                up = true;
                break;
        }
        return true;
    }

    @Override
    public boolean buttonUp(com.badlogic.gdx.controllers.Controller controller, int buttonCode) {
        switch (buttonCode) {
            case Square:
                attack = false;
                break;
            case Cross:
                up = false;
                break;
        }
        return true;
    }

    @Override
    public boolean axisMoved(com.badlogic.gdx.controllers.Controller controller, int axisCode, float value) {
        return false;
    }

    @Override
    public boolean povMoved(com.badlogic.gdx.controllers.Controller controller, int povCode, PovDirection value) {
        System.out.println(value);
        return false;
    }

    @Override
    public boolean xSliderMoved(com.badlogic.gdx.controllers.Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(com.badlogic.gdx.controllers.Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(com.badlogic.gdx.controllers.Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }


    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }
}
