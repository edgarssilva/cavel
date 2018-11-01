package com.edgarsilva.pixelgame.engine.ai.pfa;

import com.badlogic.gdx.ai.pfa.PathFinderRequest;

public interface Pather<N> {
    void acceptPath(PathFinderRequest<N> request);
}
