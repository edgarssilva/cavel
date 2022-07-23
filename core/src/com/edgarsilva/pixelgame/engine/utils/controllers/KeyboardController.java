package com.edgarsilva.pixelgame.engine.utils.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class KeyboardController extends Controller implements InputProcessor {

    public KeyboardController() {
        //Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean keyProcessed = false;

        switch (keycode) {
            case Input.Keys.A:
                left = true;
                keyProcessed = true;
                break;
            case Input.Keys.D:
                right = true;
                keyProcessed = true;
                break;
            case Input.Keys.S:
                down = true;
                keyProcessed = true;
                break;
            case Input.Keys.SPACE:
                up = true;
                keyProcessed = true;
                break;

            case Input.Keys.K:
                attack = true;
                keyProcessed = true;
                break;
        }
        return keyProcessed;
    }
    @Override
    public boolean keyUp(int keycode) {

        boolean keyProcessed = false;

        switch (keycode) {
            case Input.Keys.A:
                left = false;
                keyProcessed = true;
                break;
            case Input.Keys.D:
                right = false;
                keyProcessed = true;
                break;
            case Input.Keys.W:
                up = false;
                keyProcessed = true;
                break;
            case Input.Keys.S:
                down = false;
                keyProcessed = true;
                break;
            case Input.Keys.SPACE:
                up = false;
                keyProcessed = true;
                break;
            case Input.Keys.K:
                attack = false;
                keyProcessed = true;
                break;
        }
        return keyProcessed;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}
