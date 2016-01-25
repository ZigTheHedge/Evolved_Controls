package com.cwelth.evolved_controls.blocks;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.renders.SparksEntityFX;
import com.cwelth.evolved_controls.blocks.tileentities.TEGenericControl;
import com.cwelth.evolved_controls.blocks.tileentities.TEKnifeSwitch;
import com.cwelth.evolved_controls.blocks.tileentities.TEStationaryHandle;
import com.cwelth.evolved_controls.utils.Utilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.malisis.core.inventory.IInventoryProvider;
import net.malisis.core.inventory.MalisisInventory;
import net.malisis.core.util.AABBUtils;
import net.malisis.core.util.MultiBlock;
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
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static net.minecraftforge.common.util.ForgeDirection.*;
import static net.minecraftforge.common.util.ForgeDirection.EAST;

/**
 * Created by ZtH on 30.10.2015.
 */
public class MBlockStationaryHandle extends MBlockGenericControl implements ITileEntityProvider {
    public static int renderId = -1;

    public MBlockStationaryHandle(String unlocalizedName, Material material)
    {
        super(material);

        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(ModMain.MODID + ":" + unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabRedstone);

        this.setHardness(3F);
        this.setResistance(7.0F);
        //this.setBlockBounds();

        this.setStepSound(soundTypeStone);

        this.defaultBoundingBox = AxisAlignedBB.getBoundingBox(0.1, 0, 0, 0.9, 2, 1);

    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_){
        return p_149660_9_;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack){
        ForgeDirection dir = Utilities.orientationHelper(player).getOpposite();
        TEStationaryHandle te = TileEntityUtils.getTileEntity(TEStationaryHandle.class, world, x, y, z);
        if (te != null)
            te.setDirection(dir);
        else
            System.out.println("TE is NULL! :(");
        world.setBlock(x, y + 1, z, this, 8, 2);
    }

    @Override
    public boolean canPlaceBlockOnSide (World world, int x, int y, int z, int side) {
        return (world.isSideSolid(x, y-1, z, UP) && world.isAirBlock(x, y + 1, z));

    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        //System.out.println("Metadata: "+String.valueOf(metadata));
        if( metadata == 8 ){ return null; }
        //if( metadata == 8 ){ defaultBoundingBox = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0); return null; }
        return new TEStationaryHandle();
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(world.getBlockMetadata(x, y, z) == 8) {
            onBlockActivated(world, x, y-1, z, player, side, hitX, hitY, hitZ);
            return true;
        }
        if(player.isSneaking())
        {
            if(!world.isRemote)
            {
                IInventoryProvider te = TileEntityUtils.getTileEntity(IInventoryProvider.class, world, x, y, z);
                MalisisInventory.open((EntityPlayerMP) player, te);
            }
        } else {
            TEStationaryHandle te = TileEntityUtils.getTileEntity(TEStationaryHandle.class, world, x, y, z);
            if (te == null)
                return true;

            if (te.isMoving())
                return true;

            te.pushMe();
            if(te.getState() == TEGenericControl.State.TURNINGON)
                world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
            else
                world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
        }
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z){
        TEGenericControl te;
        if(blockAccess.getBlockMetadata(x, y, z) == 8)
        {
            this.defaultBoundingBox = AxisAlignedBB.getBoundingBox(0.1, -1, 0, 0.9, 1, 1);
            te = (TEGenericControl)blockAccess.getTileEntity(x,y-1,z);
        } else
        {
            this.defaultBoundingBox = AxisAlignedBB.getBoundingBox(0.1, 0, 0, 0.9, 2, 1);
            te = (TEGenericControl)blockAccess.getTileEntity(x,y,z);
        }
        if (te != null) {
            ForgeDirection dir = te.getDirection();
            te.setDirection(dir);
            setBlockBounds(AABBUtils.rotate(defaultBoundingBox.copy(), Utilities.dirToMeta(dir)));
        }

    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        if (world.getBlockMetadata(x, y, z) != 8)
        {
            if (world.getBlock(x, y + 1, z) != this)
                world.setBlockToAir(x, y, z);

            if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z))
            {
                world.setBlockToAir(x, y, z);
                if (world.getBlock(x, y + 1, z) == this)
                    world.setBlockToAir(x, y + 1, z);
                if (!world.isRemote)
                    dropBlockAsItem( world, x, y, z, 0, 0 );
            }

        }
        else
        {
            if (world.getBlock(x, y - 1, z) != this)
                world.setBlockToAir(x, y, z);

            if (block != this)
                onNeighborBlockChange(world, x, y - 1, z, block);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        if(world.getBlockMetadata(x, y, z) != 8) {
            IInventoryProvider provider = TileEntityUtils.getTileEntity(IInventoryProvider.class, world, x, y, z);
            if(provider != null)
                for (MalisisInventory inventory : provider.getInventories())
                    if(inventory != null)
                        inventory.breakInventory(world, x, y, z);
        }
        super.breakBlock(world, x, y, z, block, metadata);
    }

    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int p_149709_5_)
    {
        if(blockAccess.getBlockMetadata(x, y, z) == 8)return 0;
        return ((TEStationaryHandle)blockAccess.getTileEntity(x, y, z)).getState() == TEStationaryHandle.State.ON ? 15 : 0;
    }

    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        if(blockAccess.getBlockMetadata(x, y, z) == 8)return 0;
        return ((TEStationaryHandle)blockAccess.getTileEntity(x, y, z)).getState() == TEStationaryHandle.State.ON ? 15 : 0;
    }

    @Override
    public int getRenderType () {
        return renderId;
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public void generateParticles(World worldObj, int xCoord, int yCoord, int zCoord, ForgeDirection direction)
    {
        if(!worldObj.isRemote)return;
        double motionX = worldObj.rand.nextGaussian() * 0.02D;
        double motionY = worldObj.rand.nextGaussian() * 0.02D;
        double motionZ = worldObj.rand.nextGaussian() * 0.02D;
        double x = xCoord + 0.25 + worldObj.rand.nextFloat() / 2;
        double y = yCoord + 0.25 + worldObj.rand.nextFloat() / 5;
        double z = zCoord + 0.25 + worldObj.rand.nextFloat() / 2;

        SparksEntityFX fx = new SparksEntityFX(worldObj, x, y, z, motionX, motionY, motionZ, Minecraft.getMinecraft().effectRenderer);

        Minecraft.getMinecraft().effectRenderer.addEffect(fx);

    }
    */
}
