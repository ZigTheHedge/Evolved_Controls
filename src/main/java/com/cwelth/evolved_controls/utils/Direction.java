package com.cwelth.evolved_controls.utils;

import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.model.MalisisModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by max on 22.10.15.
 */
public class Direction {
    public static final int DIR_WEST = 3;
    public static final int DIR_NORTH = 0;
    public static final int DIR_EAST = 1;
    public static final int DIR_SOUTH = 2;

    private ForgeDirection direction;
    public Direction(ForgeDirection direction) {
        this.direction = direction;
    }

    public Direction(EntityLivingBase entity) {
        int metadata = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        direction = metaToDir(metadata);
    }

    public Direction(int metadata) {
        direction = metaToDir(metadata);
    }

    private ForgeDirection metaToDir(int metadata)
    {
        if (metadata == DIR_NORTH)
            return ForgeDirection.NORTH;
        else if (metadata == DIR_EAST)
            return ForgeDirection.EAST;
        else if (metadata == DIR_SOUTH)
            return ForgeDirection.SOUTH;
        return ForgeDirection.WEST;
    }

    public ForgeDirection getForgeDirection() {
        return direction;
    }

    public int getMeta() {
        if (direction == ForgeDirection.NORTH)
            return DIR_NORTH;
        else if (direction == ForgeDirection.EAST)
            return DIR_EAST;
        else if (direction == ForgeDirection.SOUTH)
            return DIR_SOUTH;
        return DIR_WEST;
    }

    public int getOffsetX() {
        return direction.offsetX;
    }

    public int getOffsetY() {
        return direction.offsetY;
    }

    public int getOffsetZ() {
        return direction.offsetZ;
    }

    public boolean isVertical() {
        return getOffsetY()!=0;
    }

    public void setupRotation(Shape forForm) {
        forForm.resetState();
        if (direction == ForgeDirection.SOUTH) {
            forForm.rotate(180, 0, 1, 0, 0, 0, 0);
        }
        else if (direction == ForgeDirection.EAST) {
            forForm.rotate(-90, 0, 1, 0, 0, 0, 0);
        }
        else if (direction == ForgeDirection.WEST) {
            forForm.rotate(90, 0, 1, 0, 0, 0, 0);
        }
    }

    public void setupRotation (MalisisModel forForm) {
        forForm.resetState();
        for(Shape singleShape: forForm)
            setupRotation(singleShape);
    }

    public boolean isSideSolid(World world, int x, int y, int z) {
        return world.isSideSolid(x+getOffsetX(), y+getOffsetY(), z+getOffsetZ(), getForgeDirection().getOpposite());
    }
}
