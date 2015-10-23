package com.cwelth.evolved_controls.utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by zth on 14/10/15.
 */
public class Utilities {
    public static long timeToTick(long time)
    {
        return time * 20 / 1000;
    }

    public static long tickToTime(long ticks)
    {
        return ticks * 1000 / 20;
    }
}
