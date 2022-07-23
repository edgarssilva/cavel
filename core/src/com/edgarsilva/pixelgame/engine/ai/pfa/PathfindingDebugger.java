package com.edgarsilva.pixelgame.engine.ai.pfa;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;
import com.edgarsilva.pixelgame.engine.utils.managers.LevelManager;

import java.util.Iterator;


public class PathfindingDebugger {
    private static OrthographicCamera camera;
    private static ShapeRenderer shapeRenderer;

    public static void setCamera(OrthographicCamera camera) {
        PathfindingDebugger.camera = camera;
        shapeRenderer = new ShapeRenderer();
    }

    public static void drawPoint2Point(Vector2 pos1, Vector2 pos2) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 1, 1);
        shapeRenderer.circle(RenderSystem.PixelsToMeters(pos1.x), RenderSystem.PixelsToMeters(pos1.y), 3 * RenderSystem.PIXELS_TO_METERS, 5);
        shapeRenderer.circle(RenderSystem.PixelsToMeters(pos2.x), RenderSystem.PixelsToMeters(pos2.y), 3 * RenderSystem.PIXELS_TO_METERS, 5);
//        shapeRenderer.line(pos1, pos2, 10);
        shapeRenderer.rectLine(pos1.scl(RenderSystem.PIXELS_TO_METERS), pos2.scl(RenderSystem.PIXELS_TO_METERS), 1 * RenderSystem.PIXELS_TO_METERS);
        shapeRenderer.end();
    }

    public static void drawPath(GraphPathImp path) {
        Iterator<Node> pathIterator = path.iterator();
        Node priorNode = null;

        while (pathIterator.hasNext()) {
            Node node = pathIterator.next();

            int index = node.getIndex();

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 0, 0, 1);
            shapeRenderer.circle(RenderSystem.PixelsToMeters(LevelManager.tilePixelWidth / 2 + (index % LevelManager.lvlTileWidth) * LevelManager.tilePixelWidth),
                    RenderSystem.PixelsToMeters(LevelManager.tilePixelHeight / 2 + (index / LevelManager.lvlTileWidth) * LevelManager.tilePixelHeight),
                    1 * RenderSystem.PIXELS_TO_METERS, 5);
            shapeRenderer.end();

            if (priorNode != null) {
                int index2 = priorNode.getIndex();

                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.line(RenderSystem.PixelsToMeters(LevelManager.tilePixelWidth / 2 + (index % LevelManager.lvlTileWidth) * LevelManager.tilePixelWidth),
                        RenderSystem.PixelsToMeters(LevelManager.tilePixelHeight / 2 + (index / LevelManager.lvlTileWidth) * LevelManager.tilePixelHeight),
                        RenderSystem.PixelsToMeters(LevelManager.tilePixelWidth / 2 + (index2 % LevelManager.lvlTileWidth) * LevelManager.tilePixelWidth),
                        RenderSystem.PixelsToMeters(LevelManager.tilePixelHeight / 2 + (index2 / LevelManager.lvlTileWidth) * LevelManager.tilePixelHeight));
                shapeRenderer.end();
            }

            priorNode = node;
        }
    }


    public static void drawNodes(GraphImp graph) {
        Iterator<Node> iterator = graph.nodes.iterator();
        shapeRenderer.setProjectionMatrix(camera.combined);


        while (iterator.hasNext()) {
            Node node = iterator.next();

            int type = node.type;
            int index = node.getIndex();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if (type == Node.Type.GROUND)
                shapeRenderer.setColor(10/255f, 200/255f, 1/255f, 1);
            else if (type == Node.Type.AIR)
                shapeRenderer.setColor(0/255f, 95/255f, 200/255f, 1);
            else if (type == Node.Type.WALL)
                shapeRenderer.setColor(200/255f, 0/255f, 0/255f, 1);
            else if (type == Node.Type.LEFT)
                shapeRenderer.setColor(255/255f, 250/255f, 7/255f, 1);
            else if (type == Node.Type.RIGHT)
                shapeRenderer.setColor(208/255f, 0/255f, 255/255f, 1);

            shapeRenderer.rect(
                    RenderSystem.PixelsToMeters(LevelManager.tilePixelWidth / 2f + (index % LevelManager.lvlTileWidth) * LevelManager.tilePixelWidth),
                    RenderSystem.PixelsToMeters(LevelManager.tilePixelHeight / 2f + (index / LevelManager.lvlTileWidth) * LevelManager.tilePixelHeight),
                    LevelManager.tilePixelWidth,
                    LevelManager.tilePixelHeight

            );
            shapeRenderer.end();
            if (node.getConnections().size > 0) {
                Connection<Node> conn = node.getConnections().get(0);
                Node from = conn.getFromNode();
                Node to = conn.getToNode();
                int fromIndex = from.getIndex();
                int toIndex = to.getIndex();
                drawPoint2Point(new Vector2(
                                LevelManager.tilePixelWidth / 2f + (fromIndex % LevelManager.lvlTileWidth) * LevelManager.tilePixelWidth,
                                LevelManager.tilePixelHeight / 2f + (fromIndex / LevelManager.lvlTileWidth) * LevelManager.tilePixelHeight
                        ),
                        new Vector2(
                                LevelManager.tilePixelWidth / 2f + (toIndex % LevelManager.lvlTileWidth) * LevelManager.tilePixelWidth,
                                LevelManager.tilePixelHeight / 2f + (toIndex / LevelManager.lvlTileWidth) * LevelManager.tilePixelHeight
                        )
                );
            }

        }

    }

}
