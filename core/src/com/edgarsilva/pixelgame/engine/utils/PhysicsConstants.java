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

    public final static short   FRIENDLY_BITS                 = 0x0001;
    public final static short   ENEMY_BITS                    = 0x0002;
    public final static short   LEVEL_BITS                    = 0x0004;
    public final static short   NEUTRAL_BITS                  = 0x0008;

    public final static short ATTACK_SENSOR = 0x0050;
    public final static short   FOOT_SENSOR                   = 0x0010;
    public final static short   RIGHT_WALL_SENSOR             = 0x0020;
    public final static short   LEFT_WALL_SENSOR              = 0x0040;

}
