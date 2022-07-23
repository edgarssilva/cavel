package com.edgarsilva.pixelgame.engine.utils.objects;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public interface CollisionComponent extends Component {

    void handleCollision(Entity owner, Entity collider);
    void endCollision(Entity owner, Entity collider);
}
