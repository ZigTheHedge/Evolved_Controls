package com.cwelth.evolved_controls.blocks;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
import com.cwelth.evolved_controls.utils.Utilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

import java.util.Random;

import static net.minecraftforge.common.util.ForgeDirection.*;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;


/**
 * Created by zth on 14/10/15.
 */
public class MBlockFancyButton extends Block implements ITileEntityProvider {

    public static int renderId = -1;
    private AxisAlignedBB defaultBoundingBox = AxisAlignedBB.getBoundingBox(0.1, 0.1, 0, 0.9, 0.9, 0.1);

    public MBlockFancyButton(String unlocalizedName, Material material)
    {
        super(material);

        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(ModMain.MODID + ":" + unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabRedstone);

        this.setHardness(3F);
        this.setResistance(7.0F);
        //this.setBlockBounds();

        this.setStepSound(soundTypeStone);
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
    public int onBlockPlaced(World world, int x, int y, int z, int side, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_){

        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
        int metadata = Utilities.dirToMeta(dir);
        world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
        return Utilities.dirToMeta(dir);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack){

        ForgeDirection dir = Utilities.metaToDir(world.getBlockMetadata(x, y, z));
        TEFancyButton te = TileEntityUtils.getTileEntity(TEFancyButton.class, world, x, y, z);
        if (te != null)
            te.setDirection(dir);
        else
            System.out.println("TE is NULL! :(");
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

    @Override
    public int getRenderType () {
        return renderId;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TEFancyButton();
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        //if(world.isRemote) return true;

        if(player.isSneaking())
        {
            if(!world.isRemote)
            {
                IInventoryProvider te = TileEntityUtils.getTileEntity(IInventoryProvider.class, world, x, y, z);
                MalisisInventory.open((EntityPlayerMP) player, te);
            }
        } else {

            TEFancyButton te = TileEntityUtils.getTileEntity(TEFancyButton.class, world, x, y, z);
            if (te == null)
                return true;

            if (te.getState() != TEFancyButton.State.IDLE)
                return true;

            te.pushMe();
            if (te.getState() == TEFancyButton.State.PUSHING) {
                world.scheduleBlockUpdate(x, y, z, this, te.getAnimationLengthTicks() + te.getPressDelay());
            }
        }
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z){

        TEFancyButton te = (TEFancyButton)blockAccess.getTileEntity(x,y,z);
        if (te != null) {
            ForgeDirection dir = te.getDirection();
            te.setDirection(dir);
            setBlockBounds(AABBUtils.rotate(defaultBoundingBox.copy(), Utilities.dirToMeta(dir)));
        }

    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        IInventoryProvider provider = TileEntityUtils.getTileEntity(IInventoryProvider.class, world, x, y, z);
        for (MalisisInventory inventory : provider.getInventories())
            inventory.breakInventory(world, x, y, z);
        super.breakBlock(world, x, y, z, block, metadata);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return (world.isSideSolid(x - 1, y, z, EAST)) ||
                (world.isSideSolid(x + 1, y, z, WEST)) ||
                (world.isSideSolid(x, y, z - 1, SOUTH)) ||
                (world.isSideSolid(x, y, z + 1, NORTH));
    }

    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int p_149709_5_)
    {
        return ((TEFancyButton)blockAccess.getTileEntity(x, y, z)).getState() == TEFancyButton.State.PUSHED ? 15 : 0;
    }

    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return ((TEFancyButton)blockAccess.getTileEntity(x, y, z)).getState() == TEFancyButton.State.PUSHED ? 15 : 0;
    }
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        TEFancyButton te = TileEntityUtils.getTileEntity(TEFancyButton.class, world, x, y, z);
        if (te == null)
            return;

        if (te.getState() != TEFancyButton.State.PUSHED)
            return;

        te.pushMe();
        te.notifyNeighbors();
        world.markBlockForUpdate(x, y, z);
    }
}
