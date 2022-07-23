package com.edgarsilva.pixelgame.engine.utils.generators;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;

public class StatsGenerator {

    public static void generateStats(FileHandle handle, StatsComponent stats){
        String     rawJson = handle.readString();
        JsonReader reader  = new JsonReader();
        JsonValue  root    = reader.parse(rawJson);

        stats.damage    = root.getShort("damage");
        stats.armor     = root.getShort("armor");
        stats.magic     = root.getShort("magic");
        stats.health    = root.getShort("health");
        stats.maxHealth = root.getShort("maxHealth");
    }
}
