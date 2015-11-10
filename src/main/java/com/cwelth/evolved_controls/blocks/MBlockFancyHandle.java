package com.cwelth.evolved_controls.blocks;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.renders.SparksEntityFX;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyHandle;
import com.cwelth.evolved_controls.blocks.tileentities.TEKnifeSwitch;
import com.cwelth.evolved_controls.utils.Utilities;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.malisis.core.inventory.IInventoryProvider;
import net.malisis.core.inventory.MalisisInventory;
import net.malisis.core.util.AABBUtils;
import net.malisis.core.util.TileEntityUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
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
public class MBlockFancyHandle extends MBlockGenericControl implements ITileEntityProvider {

    public static int renderId = -1;

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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack){

        ForgeDirection dir = Utilities.metaToDir(world.getBlockMetadata(x, y, z));
        TEFancyHandle te = TileEntityUtils.getTileEntity(TEFancyHandle.class, world, x, y, z);
        if (te != null)
            te.setDirection(dir);
        else
            System.out.println("TE is NULL! :(");
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
            if(!world.isRemote)
            {
                IInventoryProvider te = TileEntityUtils.getTileEntity(IInventoryProvider.class, world, x, y, z);
                MalisisInventory.open((EntityPlayerMP) player, te);
            }

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

    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int p_149709_5_)
    {
        return ((TEFancyHandle)blockAccess.getTileEntity(x, y, z)).getState() == TEFancyHandle.State.ON ? 15 : 0;
    }

    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return ((TEFancyHandle)blockAccess.getTileEntity(x, y, z)).getState() == TEFancyHandle.State.ON ? 15 : 0;
    }

    @Override
    public int getRenderType () {
        return renderId;
    }

    @SideOnly(Side.CLIENT)
    public void generateParticles(World worldObj, int xCoord, int yCoord, int zCoord, ForgeDirection direction)
    {
        if(!worldObj.isRemote)return;
        double motionX = worldObj.rand.nextGaussian() * 0.02D;
        double motionY = worldObj.rand.nextGaussian() * 0.02D;
        double motionZ = worldObj.rand.nextGaussian() * 0.02D;
        double x = xCoord + 0.25 + worldObj.rand.nextFloat() / 2;
        double y = yCoord + 0.25 + worldObj.rand.nextFloat() / 2;
        double z = zCoord + 0.25 + worldObj.rand.nextFloat() / 2;

        double offset = 0.1;

        if(direction == ForgeDirection.NORTH)
            z = zCoord + offset;
        if(direction == ForgeDirection.SOUTH)
            z = zCoord + 1 + offset;
        if(direction == ForgeDirection.EAST)
            x = xCoord + 1 + offset;
        if(direction == ForgeDirection.WEST)
            x = xCoord + offset;

        SparksEntityFX fx = new SparksEntityFX(worldObj, x, y, z, motionX, motionY, motionZ, Minecraft.getMinecraft().effectRenderer);

        Minecraft.getMinecraft().effectRenderer.addEffect(fx);
        FMLLog.info("generateParticles called!");
    }
}
