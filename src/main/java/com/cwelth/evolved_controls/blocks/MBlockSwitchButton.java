package com.cwelth.evolved_controls.blocks;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.tileentities.TESwitchButton;
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


/**
 * Created by ZtH on 24.10.2015.
 */
public class MBlockSwitchButton extends MBlockGenericControl implements ITileEntityProvider {

    public static int renderId = -1;


    public MBlockSwitchButton(String unlocalizedName, Material material)
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
        TESwitchButton te = TileEntityUtils.getTileEntity(TESwitchButton.class, world, x, y, z);
        if (te != null)
            te.setDirection(dir);
        else
            System.out.println("TE is NULL! :(");
    }


    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TESwitchButton();
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {

        if(player.isSneaking())
        {
            if(!world.isRemote)
            {
                IInventoryProvider te = TileEntityUtils.getTileEntity(IInventoryProvider.class, world, x, y, z);
                MalisisInventory.open((EntityPlayerMP) player, te);
            }
        } else {
            TESwitchButton te = TileEntityUtils.getTileEntity(TESwitchButton.class, world, x, y, z);
            if (te == null)
                return true;

            if (te.isMoving())
                return true;

            te.pushMe();
        }
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z){

        TESwitchButton te = (TESwitchButton)blockAccess.getTileEntity(x,y,z);
        if (te != null) {
            ForgeDirection dir = te.getDirection();
            te.setDirection(dir);
            setBlockBounds(AABBUtils.rotate(defaultBoundingBox.copy(), Utilities.dirToMeta(dir)));
        }

    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
    }

    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int p_149709_5_)
    {
        return ((TESwitchButton)blockAccess.getTileEntity(x, y, z)).getState() == TESwitchButton.State.ON ? 15 : 0;
    }

    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return ((TESwitchButton)blockAccess.getTileEntity(x, y, z)).getState() == TESwitchButton.State.ON ? 15 : 0;
    }

    @Override
    public int getRenderType () {
        return renderId;
    }


}
