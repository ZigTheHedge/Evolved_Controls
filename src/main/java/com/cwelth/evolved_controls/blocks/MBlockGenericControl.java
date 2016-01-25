package com.cwelth.evolved_controls.blocks;

import com.cwelth.evolved_controls.blocks.renders.SparksEntityFX;
import com.cwelth.evolved_controls.blocks.tileentities.TEGenericControl;
import com.cwelth.evolved_controls.utils.Utilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static net.minecraftforge.common.util.ForgeDirection.*;
import static net.minecraftforge.common.util.ForgeDirection.EAST;

/**
 * Created by ZtH on 25.10.2015.
 */
public class MBlockGenericControl extends Block {

    protected AxisAlignedBB defaultBoundingBox = AxisAlignedBB.getBoundingBox(0.1, 0.1, 0, 0.9, 0.9, 0.1);

    protected MBlockGenericControl(Material p_i45394_1_) {
        super(p_i45394_1_);
    }

    @Override
    public boolean canPlaceBlockOnSide (World world, int x, int y, int z, int side) {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        return (dir == NORTH && world.isSideSolid(x, y, z + 1, NORTH)) ||
                (dir == SOUTH && world.isSideSolid(x, y, z - 1, SOUTH)) ||
                (dir == WEST  && world.isSideSolid(x + 1, y, z, WEST)) ||
                (dir == EAST  && world.isSideSolid(x - 1, y, z, EAST));

    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        TEGenericControl te = (TEGenericControl)world.getTileEntity(x, y, z);
        if(te != null){
            ForgeDirection dir = te.getDirection();
            if(!canPlaceBlockOnSide(world, x, y, z, Utilities.dirToSide(dir.getOpposite()))) {
                world.setBlockToAir(x, y, z);
                if(!world.isRemote)
                    this.dropBlockAsItem(world, x, y, z, 0, 0);
            }
        }
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_){

        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
        int metadata = Utilities.dirToMeta(dir);
        world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
        return Utilities.dirToMeta(dir);
    }

    protected AxisAlignedBB setBlockBounds(AxisAlignedBB aabb) {
        if(aabb == null) {
            aabb = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        }

        this.setBlockBounds((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ, (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
        return aabb;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock () {
        return false;
    }


    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return (world.isSideSolid(x - 1, y, z, EAST)) ||
                (world.isSideSolid(x + 1, y, z, WEST)) ||
                (world.isSideSolid(x, y, z - 1, SOUTH)) ||
                (world.isSideSolid(x, y, z + 1, NORTH));
    }

    public boolean canProvidePower()
    {
        return true;
    }

}
