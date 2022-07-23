package com.edgarsilva.pixelgame.engine.utils;

public class PhysicsConstants {

    // These are the numbers that get sent so entities know how to react to collisions
    public final static short   COL_PLAYER              = -1;
    public final static short   COL_ENEMY               = -2;
    public final static short   COL_LEVEL               = -3;
    public final static short   COL_FRIENDLY_BULLET     = -4;
    public final static short   COL_ENEMY_BULLET        = -5;
    public final static short   COL_MONEY               = -6;
    public final static short   COL_HEALTH              = -7;
    public final static short   COL_GUN                 = -8;
    public final static short   COL_GRENADE             = -9;

    public final static short   FRIENDLY_BITS           = 1;
    public final static short   ENEMY_BITS              = 2;
    public final static short   LEVEL_BITS              = 4;
    public static final short   OBSTACLE_BITS           = 12;
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
}
