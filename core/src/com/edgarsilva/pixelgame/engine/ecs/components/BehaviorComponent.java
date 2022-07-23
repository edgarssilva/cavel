package com.edgarsilva.pixelgame.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.BehaviorTree;

public class BehaviorComponent implements Component {
    public BehaviorTree<Entity> bTree;
}
