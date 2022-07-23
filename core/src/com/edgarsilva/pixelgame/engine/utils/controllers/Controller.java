package com.edgarsilva.pixelgame.engine.utils.controllers;

import com.badlogic.gdx.InputProcessor;

public abstract class Controller {
    public static boolean left, right, up, down, attack;
    public abstract InputProcessor getInputProcessor();
}
