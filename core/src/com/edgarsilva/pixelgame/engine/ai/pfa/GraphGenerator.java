package com.edgarsilva.pixelgame.engine.ai.pfa;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.edgarsilva.pixelgame.engine.utils.managers.LevelManager;


public class GraphGenerator {



    public static GraphImp gereneratePlatformGraph(TiledMapTileLayer tiles) {
        Array<Node> nodes = new Array<Node>();

        int mapWidth = LevelManager.lvlTileWidth;
        int mapHeight = LevelManager.lvlTileHeight;

        for (int y = 0; y < mapHeight; ++y) {
            for (int x = 0; x < mapWidth; ++x) {

                Node node = new Node();

                TiledMapTileLayer.Cell target = tiles.getCell(x, y);
                TiledMapTileLayer.Cell down = tiles.getCell(x, y-1);
                TiledMapTileLayer.Cell left = tiles.getCell(x - 1, y);
                TiledMapTileLayer.Cell leftDown = tiles.getCell(x - 1, y - 1);
                TiledMapTileLayer.Cell leftUp = tiles.getCell(x - 1, y + 1);
                TiledMapTileLayer.Cell right = tiles.getCell(x + 1, y - 1);
                TiledMapTileLayer.Cell rightDown = tiles.getCell(x + 1, y - 1);
                TiledMapTileLayer.Cell rightUp = tiles.getCell(x + 1, y + 1);

                if (target == null){

                    if(down != null) {
                        if (leftDown == null || (left != null && leftUp != null)) {
                            node.type = Node.Type.LEFT;
                        }else if (rightDown == null || (right != null && rightUp != null)) {
                            node.type = Node.Type.RIGHT;
                        } else {
                            node.type = Node.Type.GROUND;
                        }
                    }else{
                        node.type = Node.Type.AIR;
                    }
                }else{
                    node.type = Node.Type.WALL;
                }

                // nodes.put(mapWidth * y + x, node);
                nodes.add(node);
            }
        }


        for (int y = 0; y < mapHeight; ++y) {
            for (int x = 0; x < mapWidth; ++x) {

                Node target = nodes.get(mapWidth * y + x);

                if (target.type == Node.Type.LEFT) {
                    for (int i = x; i < mapWidth; i++) {
                        Node node = nodes.get(mapWidth * y + i);
                        if (node.type == Node.Type.RIGHT)
                            target.createConnection(node, i - x);
                    }

                } else if (target.type == Node.Type.RIGHT) {
                    for (int i = x; i > 0; i--) {
                        Node node = nodes.get(mapWidth * y + i);
                        if (node.type == Node.Type.LEFT)
                            target.createConnection(node, x - i);
                    }
                }

            }
        }

        return new GraphImp(nodes);
    }



    /*public static GraphImp generateGroundGraph(TiledMap map) {
        Array<Node> nodes = new Array<Node>();

        TiledMapTileLayer tiles = (TiledMapTileLayer) map.getLayers().get("Walls");
        int mapHeight = LevelManager.lvlTileHeight;
        int mapWidth = LevelManager.lvlTileWidth;

        // Loops over the tiles in the map, starting from bottom left corner
        // iterating left to right, then down to up
        for (int y = 0; y < mapHeight; ++y) {
            for (int x = 0; x < mapWidth; ++x) {
                // generate a node for each tile so that they all exist when we create connections
                Node node = new Node();
                node.type = Node.Type.REGULAR;
                nodes.add(node);
            }
        }

        for (int y = 0; y < mapHeight; ++y) {
            for (int x = 0; x < mapWidth; ++x) {


                TiledMapTileLayer.Cell left = tiles.getCell(x - 1, y);
                TiledMapTileLayer.Cell leftDown = tiles.getCell(x - 1, y - 1);
                TiledMapTileLayer.Cell right = tiles.getCell(x + 1, y - 1);
                TiledMapTileLayer.Cell rightDown = tiles.getCell(x + 1, y - 1);
/*
                if (left != null || leftDown == null)
                    node.type = Node.Type.LEFT;
                else if(right != null || rightDown == null)
                    node.type = Node.Type.RIGHT;
                else
                    node.type = Node.Type.REGULAR;
*/ /*

                TiledMapTileLayer.Cell target = tiles.getCell(x, y);

               // TiledMapTileLayer.Cell left = tiles.getCell(x - 1, y);
               // TiledMapTileLayer.Cell right = tiles.getCell(x + 1, y);
                TiledMapTileLayer.Cell down = tiles.getCell(x, y - 1);

                Node targetNode = nodes.get(mapWidth * y + x);

                if (target == null) {
                    if (y != 0) {
                        Node downNode = nodes.get(mapWidth * (y - 1) + x);
                        targetNode.createConnection(downNode, 1);
                    }

                    if (x != 0 && left == null && down != null) {
                        Node leftNode = nodes.get(mapWidth * y + x - 1);
                        targetNode.createConnection(leftNode, 1);
                    }

                    if (x != mapWidth - 1 && right == null && down != null) {
                        Node rightNode = nodes.get(mapWidth * y + x + 1);
                        targetNode.createConnection(rightNode, 1);
                    }
                    /*
                    if (y != mapHeight - 1 && up == null) {
                        Node upNode = nodes.get(mapWidth * (y + 1) + x);
                        targetNode.createConnection(upNode, 1);
                    }
*/ /*
                }
            }
        }

        return new GraphImp(nodes);
    }
*/


/*
    public static GraphImp generateAirGraph(TiledMap map) {
        Array<Node> nodes = new Array<Node>();

        TiledMapTileLayer tiles = (TiledMapTileLayer)map.getLayers().get(0);
        int mapHeight = LevelManager.lvlTileHeight;
        int mapWidth = LevelManager.lvlTileWidth;

        // Loops over the tiles in the map, starting from bottom left corner
        // iterating left to right, then down to up
        for (int y = 0; y < mapHeight; ++y) {
            for (int x = 0; x < mapWidth; ++x) {
                // generate a node for each tile so that they all exist when we create connections
                Node node = new Node();
                node.type = Node.Type.REGULAR;
                nodes.add(node);
            }
        }

        Gdx.app.log("Size: ", "" + nodes.size);

        for (int y = 0; y < mapHeight; ++y) {
            for (int x = 0; x < mapWidth; ++x) {
                TiledMapTileLayer.Cell target = tiles.getCell(x, y);
                TiledMapTileLayer.Cell up = tiles.getCell(x, y+1);
                TiledMapTileLayer.Cell upLeft = tiles.getCell(x-1, y+1);
                TiledMapTileLayer.Cell upRight = tiles.getCell(x+1, y+1);
                TiledMapTileLayer.Cell left = tiles.getCell(x-1, y);
                TiledMapTileLayer.Cell right = tiles.getCell(x+1, y);
                TiledMapTileLayer.Cell down = tiles.getCell(x, y-1);
                TiledMapTileLayer.Cell downLeft = tiles.getCell(x-1, y-1);
                TiledMapTileLayer.Cell downRight = tiles.getCell(x+1, y-1);

                Node targetNode = nodes.get(mapWidth * y + x);
                if (target == null) {
                    if (y != 0 && down == null) {
                        Node downNode = nodes.get(mapWidth * (y - 1) + x);
                        targetNode.createConnection(downNode, 1);
                    }
                    if (x != 0 && y != 0 && downLeft == null) {
                        Node downLeftNode = nodes.get(mapWidth * (y - 1) + (x - 1));
                        targetNode.createConnection(downLeftNode, 1.7f);
                    }
                    if (x != mapWidth - 1 && y != 0 && downRight == null) {
                        Node downRightNode = nodes.get(mapWidth * (y - 1) + (x + 1));
                        targetNode.createConnection(downRightNode, 1.7f);
                    }
                    if (x != 0 && left == null) {
                        Node leftNode = nodes.get(mapWidth * y + x - 1);
                        targetNode.createConnection(leftNode, 1);
                    }
                    if (x != mapWidth - 1 && right == null) {
                        Node rightNode = nodes.get(mapWidth * y + x + 1);
                        targetNode.createConnection(rightNode, 1);
                    }
                    if (y != mapHeight - 1 && up == null) {
                        Node upNode = nodes.get(mapWidth * (y + 1) + x);
                        targetNode.createConnection(upNode, 1);
                    }
                    if (x != 0 && y != mapHeight - 1 && upLeft == null) {
                        Node upLeftNode = nodes.get(mapWidth * (y + 1) + (x - 1));
                        targetNode.createConnection(upLeftNode, 1.7f);
                    }
                    if (x != mapWidth - 1 && y != mapHeight - 1 && upRight == null) {
                        Node upRightNode = nodes.get(mapWidth * (y + 1) + (x + 1));
                        targetNode.createConnection(upRightNode, 1.7f);
                    }
                }
            }
        }

        return new GraphImp(nodes);
    }
    */
}
