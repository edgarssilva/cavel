package com.edgarsilva.pixelgame.engine.utils.managers;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.edgarsilva.pixelgame.engine.ai.pfa.GraphGenerator;
import com.edgarsilva.pixelgame.engine.ai.pfa.GraphImp;
import com.edgarsilva.pixelgame.engine.ai.pfa.Node;
import com.edgarsilva.pixelgame.engine.ecs.systems.RenderSystem;

/**
 * A class LevelManager é a responsavel por carregar um Level
 * Aqui estam guardadas constantes como o mapa, o renderer e algumas propriedades
 *
 * @author Edgar Silva
 */

public class LevelManager {

    public static TiledMap tiledMap;
    public static int lvlTileWidth;
    public static int lvlTileHeight;
    public static int lvlPixelWidth;
    public static int lvlPixelHeight;
    public static float lvlMeterWidth;
    public static float lvlMeterHeight;
    public static int tilePixelWidth;
    public static int tilePixelHeight;
    public static OrthogonalTiledMapRenderer renderer;

    public static GraphImp graph;
    public static IndexedAStarPathFinder<Node> pathFinder;

    public static void loadLevel(String filePath){
        tiledMap = new TmxMapLoader().load("maps/"+filePath+".tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap, RenderSystem.PIXELS_TO_METERS);

        MapProperties properties = tiledMap.getProperties();
        lvlTileWidth = properties.get("width", Integer.class);
        lvlTileHeight = properties.get("height", Integer.class);
        tilePixelWidth = properties.get("tilewidth", Integer.class);
        tilePixelHeight = properties.get("tileheight", Integer.class);
        lvlPixelWidth = lvlTileWidth * tilePixelWidth;
        lvlPixelHeight = lvlTileHeight * tilePixelHeight;
        lvlMeterWidth = lvlPixelWidth * RenderSystem.PIXELS_TO_METERS;
        lvlMeterHeight = lvlPixelHeight * RenderSystem.PIXELS_TO_METERS;

        graph = GraphGenerator.generateGroundGraph(LevelManager.tiledMap);
        pathFinder = new IndexedAStarPathFinder<Node>(graph, false);
    }

    public static void dispose(){
        tiledMap.dispose();
        renderer.dispose();
    }
}
