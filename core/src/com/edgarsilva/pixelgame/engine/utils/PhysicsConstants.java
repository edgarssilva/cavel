package com.edgarsilva.pixelgame.engine.utils;

public class PhysicsConstants {


    public final static short   FRIENDLY_BITS           = 1;
    public final static short   ENEMY_BITS              = 2;
    public final static short   LEVEL_BITS              = 4;
    public final static short   NEUTRAL_BITS            = 8;
    public static final short   MESSAGE_BITS            = 10;
    public static final short   OBSTACLE_BITS           = 12;
    public static final short   HIDDEN_BITS             = 18;

    //Player Sensors
    public final static short   FOOT_SENSOR             = 20;
    public final static short   RIGHT_WALL_SENSOR       = 22;
    public final static short   LEFT_WALL_SENSOR        = 24;
    public final static short   ATTACK_SENSOR           = 28;

    //Enemy Sensors
    public static final short   LEFT_GROUND_SENSOR      = 30;
    public static final short   RIGHT_GROUND_SENSOR     = 32;
    public static final short   WALL_LEFT_SENSOR        = 34;
    public static final short   WALL_RIGHT_SENSOR       = 38;
    public static final short   ENEMY_ATTACK_SENSOR     = 40;

    //Neutral
    public static final short   HEART_BITS              = 44;
    public static final short   COIN_BITS               = 48;
}
