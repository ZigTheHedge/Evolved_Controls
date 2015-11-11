package com.cwelth.evolved_controls.utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by zth on 14/10/15.
 */
public class Utilities {
    public static final int DIR_WEST = 3;
    public static final int DIR_NORTH = 0;
    public static final int DIR_EAST = 1;
    public static final int DIR_SOUTH = 2;

    public static final int SIDE_NORTH = 2;
    public static final int SIDE_SOUTH = 3;
    public static final int SIDE_WEST = 4;
    public static final int SIDE_EAST = 5;

    public static int dirToMeta(ForgeDirection dir)
    {
        if (dir == ForgeDirection.NORTH)
            return DIR_NORTH;
        else if (dir == ForgeDirection.EAST)
            return DIR_EAST;
        else if (dir == ForgeDirection.SOUTH)
            return DIR_SOUTH;
        return DIR_WEST;
    }

    public static int dirToSide(ForgeDirection dir)
    {
        if (dir == ForgeDirection.NORTH)
            return SIDE_NORTH;
        else if (dir == ForgeDirection.EAST)
            return SIDE_EAST;
        else if (dir == ForgeDirection.SOUTH)
            return SIDE_SOUTH;
        return SIDE_WEST;
    }

    public static ForgeDirection metaToDir(int metadata)
    {
        if (metadata == DIR_NORTH)
            return ForgeDirection.NORTH;
        else if (metadata == DIR_EAST)
            return ForgeDirection.EAST;
        else if (metadata == DIR_SOUTH)
            return ForgeDirection.SOUTH;
        return ForgeDirection.WEST;
    }

    public static ForgeDirection orientationHelper(EntityLivingBase entity)
    {
        ForgeDirection ret;
        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        ret = Utilities.metaToDir(l);
        return ret;
    }

    public static long timeToTick(long time)
    {
        return time * 20 / 1000;
    }

    public static long tickToTime(long ticks)
    {
        return ticks * 1000 / 20;
    }
}
