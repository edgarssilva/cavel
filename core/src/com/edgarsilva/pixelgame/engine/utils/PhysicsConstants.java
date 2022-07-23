package com.edgarsilva.pixelgame.engine.utils;

public class PhysicsConstants {


    public final static short   FRIENDLY_BITS           = 1;
    public final static short   ENEMY_BITS              = 2;
    public final static short   LEVEL_BITS              = 4;
    public static final short   OBSTACLE_BITS           = 18;
    public static final short   MESSAGE_BITS            = 12;
    public final static short   NEUTRAL_BITS            = 8;

    //Player Sensors
    public final static short   FOOT_SENSOR             = 10;
    public final static short   RIGHT_WALL_SENSOR       = 20;
    public final static short   LEFT_WALL_SENSOR        = 40;
    public final static short   ATTACK_SENSOR           = 80;

    //Enemy Sensors
    public static final short   LEFT_GROUND_SENSOR      = 100;
    public static final short   RIGHT_GROUND_SENSOR     = 200;
    public static final short   WALL_LEFT_SENSOR        = 400;
    public static final short   WALL_RIGHT_SENSOR       = 800;
    public static final short   ENEMY_ATTACK_SENSOR     = 32;

    //Neutral
    public static final short   COIN_BITS               = 64;
}
