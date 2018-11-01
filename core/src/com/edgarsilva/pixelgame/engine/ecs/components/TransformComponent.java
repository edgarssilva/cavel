package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

public class TransformComponent implements Component, Pool.Poolable {
    public Vector3 position = new Vector3(0f, 0f, 0f);

    public Vector2 scale = new Vector2(1.0f, 1.0f);

    public float rotation = 0.0f;

    public boolean isHidden = false;
    public boolean flipX = false;

    public float width = 16;
    public float height = 16;

    public float paddingTop = 0f;
    public float paddingBottom = 0f;
    public float paddingLeft = 0f;
    public float paddingRight = 0f;

    @Override
    public void reset() {
       //this.position.set(0f,0f,0f);
       //this.scale.set(0f, 0f);
       this.rotation = 0f;
       this.isHidden = this.flipX = false;
       this.width = this.height = 16;
       this.paddingTop = this.paddingBottom = this.paddingLeft = this.paddingRight = 0f;
    }
}