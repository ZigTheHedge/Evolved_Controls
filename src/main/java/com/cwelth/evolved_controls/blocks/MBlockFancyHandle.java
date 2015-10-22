package com.cwelth.evolved_controls.blocks;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyHandle;
import com.cwelth.evolved_controls.utils.Utilities;
import net.malisis.core.inventory.IInventoryProvider;
import net.malisis.core.inventory.MalisisInventory;
import net.malisis.core.util.AABBUtils;
import net.malisis.core.util.TileEntityUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static net.minecraftforge.common.util.ForgeDirection.*;
import static net.minecraftforge.common.util.ForgeDirection.EAST;

/**
 * Created by ZtH on 21.10.2015.
 */
public class MBlockFancyHandle extends Block implements ITileEntityProvider {

    public static int renderId = -1;
    private AxisAlignedBB defaultBoundingBox = AxisAlignedBB.getBoundingBox(0.1, 0.1, 0, 0.9, 0.9, 0.05);



    protected MBlockFancyHandle(String unlocalizedName, Material material) {
        super(material);

        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(ModMain.MODID + ":" + unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabRedstone);

        this.setHardness(3F);
        this.setResistance(7.0F);

        this.setStepSound(soundTypeStone);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TEFancyHandle();
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_){

        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
        int metadata = Utilities.dirToMeta(dir);
        world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
        return Utilities.dirToMeta(dir);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack){

        ForgeDirection dir = Utilities.metaToDir(world.getBlockMetadata(x, y, z));
        TEFancyHandle te = TileEntityUtils.getTileEntity(TEFancyHandle.class, world, x, y, z);
        if (te != null)
            te.setDirection(dir);
        else
            System.out.println("TE is NULL! :(");
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
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z){

        TEFancyHandle te = (TEFancyHandle)blockAccess.getTileEntity(x,y,z);
        if (te != null) {
            ForgeDirection dir = te.getDirection();
            te.setDirection(dir);
            setBlockBounds(AABBUtils.rotate(defaultBoundingBox.copy(), Utilities.dirToMeta(dir)));
        }

    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        //if(world.isRemote) return true;

        if(player.isSneaking())
        {
        } else {

            TEFancyHandle te = TileEntityUtils.getTileEntity(TEFancyHandle.class, world, x, y, z);
            if (te == null)
                return true;

            if (te.getState() == TEFancyHandle.State.TURNINGOFF || te.getState() == TEFancyHandle.State.TURNINGON)
                return true;

            te.pushMe();
        }
        return true;
    }

    protected AxisAlignedBB setBlockBounds(AxisAlignedBB aabb) {
        if(aabb == null) {
            aabb = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        }

        this.setBlockBounds((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ, (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
        return aabb;
    }

    @Override
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock () {
        return false;
    }

    @Override
    public int getRenderType () {
        return renderId;
    }

}
