package com.edgarsilva.pixelgame.engine.ai.pfa;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;


public class Node {
    private Array<Connection<Node>> connections = new Array<Connection<Node>>();
    public int type;
    public int index;

    public int getIndex() {
        return index;
    }

    public Array<Connection<Node>> getConnections() {
        return connections;
    }

    public void createConnection(Node toNode, float cost) {
        connections.add(new ConnectionImp(this, toNode, cost));
    }

    public static class Type {
        public static final int AIR = 1;
        public static final int LEFT = 2;
        public static final int RIGHT = 3;
        public static final int GROUND = 4;
        public static final int WALL = 5;
    }
}
