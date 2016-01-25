package com.cwelth.evolved_controls.blocks;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyHandle;
import com.cwelth.evolved_controls.blocks.tileentities.TEResistiveLever;
import com.cwelth.evolved_controls.utils.Utilities;
import net.malisis.core.inventory.IInventoryProvider;
import net.malisis.core.inventory.MalisisInventory;
import net.malisis.core.util.AABBUtils;
import net.malisis.core.util.TileEntityUtils;
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
 * Created by zth on 12/11/15.
 */
public class MBlockResistiveLever extends MBlockGenericControl implements ITileEntityProvider {

    public static int renderId = -1;

    protected MBlockResistiveLever(String unlocalizedName, Material material) {
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
        return new TEResistiveLever();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack){

        ForgeDirection dir = Utilities.metaToDir(world.getBlockMetadata(x, y, z));
        TEResistiveLever te = TileEntityUtils.getTileEntity(TEResistiveLever.class, world, x, y, z);
        if (te != null)
            te.setDirection(dir);
        else
            System.out.println("TE is NULL! :(");
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z){

        TEResistiveLever te = (TEResistiveLever)blockAccess.getTileEntity(x,y,z);
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
            if(!world.isRemote)
            {
                IInventoryProvider te = TileEntityUtils.getTileEntity(IInventoryProvider.class, world, x, y, z);
                MalisisInventory.open((EntityPlayerMP) player, te);
            }

        } else {

            System.out.println("HitPoint: X = "+String.valueOf(hitX)+", Y = "+String.valueOf(hitY)+", Y = "+String.valueOf(hitZ));

            /*
            TEResistiveLever te = TileEntityUtils.getTileEntity(TEResistiveLever.class, world, x, y, z);
            if (te == null)
                return true;

            if (te.getState() == TEResistiveLever.State.TURNINGOFF || te.getState() == TEResistiveLever.State.TURNINGON)
                return true;

            te.pushMe();
            */
        }
        return true;
    }

    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int p_149709_5_)
    {
        TEResistiveLever te = (TEResistiveLever)blockAccess.getTileEntity(x, y, z);
        return (te.getState() == TEResistiveLever.State.ON) ? te.strength : 0;
    }

    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TEResistiveLever te = (TEResistiveLever)blockAccess.getTileEntity(x, y, z);
        return (te.getState() == TEResistiveLever.State.ON) ? te.strength : 0;
    }

    @Override
    public int getRenderType () {
        return renderId;
    }
}
