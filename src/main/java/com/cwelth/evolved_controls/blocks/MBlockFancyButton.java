package com.cwelth.evolved_controls.blocks;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
import com.cwelth.evolved_controls.blocks.tileentities.TEGenericControl;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;



/**
 * Created by zth on 14/10/15.
 */
public class MBlockFancyButton extends MBlockGenericControl implements ITileEntityProvider {
    public static int renderId = -1;

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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack){

        ForgeDirection dir = Utilities.metaToDir(world.getBlockMetadata(x, y, z));
        TEFancyButton te = TileEntityUtils.getTileEntity(TEFancyButton.class, world, x, y, z);
        if (te != null)
            te.setDirection(dir);
        else
            System.out.println("TE is NULL! :(");
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

            if (te.getState() != TEFancyButton.State.OFF)
                return true;

            te.pushMe();
            world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
            if (te.getState() == TEFancyButton.State.TURNINGON) {
                world.scheduleBlockUpdate(x, y, z, this, te.getAnimationLengthTicks() + te.getPressDelay());
            }
        }
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z){

        TEGenericControl te = (TEGenericControl)blockAccess.getTileEntity(x,y,z);
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

    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int p_149709_5_)
    {
        return ((TEFancyButton)blockAccess.getTileEntity(x, y, z)).getState() == TEFancyButton.State.ON ? 15 : 0;
    }

    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return ((TEFancyButton)blockAccess.getTileEntity(x, y, z)).getState() == TEFancyButton.State.ON ? 15 : 0;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        TEFancyButton te = TileEntityUtils.getTileEntity(TEFancyButton.class, world, x, y, z);
        if (te == null)
            return;

        if (te.getState() != TEFancyButton.State.ON)
            return;

        te.pushMe();
        world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
        te.notifyNeighbors();
        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public int getRenderType () {
        return renderId;
    }

}
