package com.edgarsilva.pixelgame.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ColorDrawer {


    public static void drawColor(ShapeRenderer renderer, int r, int g, int b, float alpha){
        if (alpha <= 0) return;
        if (alpha > 1) alpha = 1;
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setColor(r, g, b,alpha);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer.end();

    }

}
