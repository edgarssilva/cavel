package com.edgarsilva.pixelgame.engine.ai.pfa;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.edgarsilva.pixelgame.engine.utils.managers.LevelManager;


public class GraphImp implements IndexedGraph<Node> {
    protected Array<Node> nodes = new Array<Node>();
    protected int capacity;

    public GraphImp() {
    }

    public GraphImp(int capacity) {
        this.capacity = capacity;
    }


    public GraphImp(Array<Node> nodes) {
        this.nodes = nodes;

        // speedier than indexOf()
        for (int x = 0; x < nodes.size; ++x) {
            nodes.get(x).index = x;
        }
    }

    public Node getNodeByXY(int x, int y) {
        int modX = x / LevelManager.tilePixelWidth;
        int modY = y / LevelManager.tilePixelHeight;

        return nodes.get(LevelManager.lvlTileWidth * modY + modX);
    }

    public Node getNodeByXY(Vector2 pos){
        return getNodeByXY((int) pos.x, (int) pos.y);
    }

    public Vector2 getPositionByNode(Node node){
        for (int y = 0; y < LevelManager.lvlTileHeight; y++) {
            for (int x = 0; x < LevelManager.lvlTileWidth; x++) {
                if (node.equals(nodes.get(LevelManager.lvlTileWidth * y + x)))
                    return new Vector2(x * LevelManager.tilePixelWidth, y * LevelManager.tilePixelHeight);
            }
        }
        return null;
    }

    public Node getClosestNode(int x, int y, int type) {

        Node node = getNodeByXY(x, y);
        // System.out.println("Connections: "+ node.getConnections().size);
        if (node.getConnections().size > 0)
            return  node.getConnections().get(0).getToNode();

       /* int modX = x / LevelManager.tilePixelWidth;
        int modY = y / LevelManager.tilePixelHeight;

        if (type == Node.Type.RIGHT) {

            for (int i = modX; i < LevelManager.lvlTileWidth; i++) {
                Node node = nodes.get(LevelManager.lvlTileWidth * modY + i);
                if (node.type == Node.Type.RIGHT)
                    return node;
            }

        } else if (type == Node.Type.LEFT) {
            for (int i = modX; i > 0; i--) {
                Node node = nodes.get(LevelManager.lvlTileWidth * modY + i);
                if (node.type == Node.Type.LEFT)
                    return node;
            }

        }
*/

        return null;
    }

    @Override
    public int getIndex(Node node) {
        return nodes.indexOf(node, true);
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.getConnections();
    }

    public Node get(int index) {
        return nodes.get(index);
    }
}
