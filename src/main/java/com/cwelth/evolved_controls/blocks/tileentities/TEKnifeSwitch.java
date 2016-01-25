package com.cwelth.evolved_controls.blocks.tileentities;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.MBlockKnifeSwitch;
import com.cwelth.evolved_controls.blocks.guis.GFancyButton;
import com.cwelth.evolved_controls.blocks.guis.GKnifeSwitch;
import com.cwelth.evolved_controls.utils.Utilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.inventory.IInventoryProvider;
import net.malisis.core.inventory.MalisisInventory;
import net.malisis.core.inventory.MalisisInventoryContainer;
import net.malisis.core.inventory.MalisisSlot;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by ZtH on 24.10.2015.
 */
public class TEKnifeSwitch extends TEGenericControl implements IInventoryProvider {

    public MalisisInventory inventory;
    public SolidSlot plateCamo;
    public SolidSlot handleCamo;

    public TEKnifeSwitch(){
        plateCamo = new SolidSlot(0);
        handleCamo = new SolidSlot(1);
        inventory = new MalisisInventory(this, new MalisisSlot[] { plateCamo, handleCamo });
        inventory.setInventoryStackLimit(1);
    }

    @Override
    public MalisisInventory[] getInventories(Object... data) {
        return new MalisisInventory[]{inventory};
    }

    @Override
    public MalisisInventory[] getInventories(ForgeDirection side, Object... data) {
        return getInventories(data);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public MalisisGui getGui(MalisisInventoryContainer container) {
        return new GKnifeSwitch(container, this);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(getState() == State.TURNINGON && worldObj.isRemote)
            ModMain.proxy.startParticles(this);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1)
    {
        super.writeToNBT(par1);
        par1.setInteger("direction", Utilities.dirToMeta(this.direction));
        par1.setInteger("state", state.ordinal());
        par1.setBoolean("moving", this.moving);
        par1.setLong("timeStart", this.timeStart);

        NBTTagList list = new NBTTagList();


        if(plateCamo.getItemStack() != null) {
            NBTTagCompound item = new NBTTagCompound();
            item.setByte("slotIs", (byte) 0);
            plateCamo.getItemStack().writeToNBT(item);
            list.appendTag(item);
        }
        if(handleCamo.getItemStack() != null) {
            NBTTagCompound item = new NBTTagCompound();
            item.setByte("slotIs", (byte) 1);
            handleCamo.getItemStack().writeToNBT(item);
            list.appendTag(item);
        }

        par1.setTag("slotContents", list);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1) {
        super.readFromNBT(par1);
        this.direction = Utilities.metaToDir(par1.getInteger("direction"));
        this.state = State.values()[par1.getInteger("state")];
        this.moving = par1.getBoolean("moving");
        this.timeStart = par1.getLong("timeStart");

        NBTTagList list = par1.getTagList("slotContents", 10);
        for(int i=0; i<list.tagCount(); i++) {
            byte slotNum = list.getCompoundTagAt(i).getByte("slotIs");
            if(slotNum == 0)
                this.plateCamo.setItemStack(ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i)));
            else if(slotNum == 1)
                this.handleCamo.setItemStack(ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i)));
        }
    }

}
