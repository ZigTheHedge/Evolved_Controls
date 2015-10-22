package com.cwelth.evolved_controls.blocks.tileentities;

import com.cwelth.evolved_controls.blocks.guis.GFancyButton;
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
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by zth on 14/10/15.
 */
public class TEFancyButton extends TileEntity implements IInventoryProvider {

    private ForgeDirection direction = ForgeDirection.NORTH;
    protected long timeStart;
    public MalisisInventory inventory;
    public SolidSlot plateCamo;
    public SolidSlot buttonCamo;

    public enum State
    {
        PUSHED, PUSHING, IDLE, IDELING
    }
    protected State state = State.IDLE;
    protected boolean moving = false;
    private int animationLengthTicks = 6;
    private int pressDelay = 32;
    public ItemStack isPlateCamo;
    public ItemStack isButtonCamo;


    public TEFancyButton(){
        plateCamo = new SolidSlot(0);
        buttonCamo = new SolidSlot(1);
        inventory = new MalisisInventory(this, new MalisisSlot[] { plateCamo, buttonCamo });
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
        return new GFancyButton(container, this);
    }

    public ForgeDirection getDirection()
    {
        return direction;
    }

    public void setDirection(ForgeDirection newDir)
    {
        direction = newDir;
    }

    public void setStart()
    {
        this.setStart(System.currentTimeMillis());
    }

    public void setStart(long timeStart)
    {
        this.timeStart = timeStart;
    }

    public long getStart()
    {
        return timeStart;
    }

    public int getAnimationLengthTicks() { return animationLengthTicks; }

    public int getPressDelay() { return pressDelay; }

    public void setPressDelay(int newDelay) { this.pressDelay = newDelay; }

    public State getState()
    {
        return state;
    }

    public boolean isMoving()
    {
        return moving;
    }

    public void pushMe()
    {
        State newState = (state == State.IDLE) ? State.PUSHING : State.IDELING;
        setStart();
        setNewState(newState);

    }

    public World getWorld()
    {
        return this.worldObj;
    }

    public void setNewState(State newState)
    {
        if (state == newState)
            return;

        state = newState;
        if (worldObj == null) {
            return;
        }

        if (state == State.IDELING || state == State.PUSHING)
        {
                moving = true;
        }
        else
        {
            moving = false;
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1)
    {
        super.writeToNBT(par1);
        par1.setInteger("direction", Utilities.dirToMeta(this.direction));
        par1.setInteger("state", state.ordinal());
        par1.setBoolean("moving", this.moving);
        par1.setLong("timeStart", this.timeStart);
        par1.setInteger("pressDelay", this.pressDelay);

        NBTTagList list = new NBTTagList();


        if(plateCamo.getItemStack() != null) {
            NBTTagCompound item = new NBTTagCompound();
            item.setByte("slotIs", (byte) 0);
            plateCamo.getItemStack().writeToNBT(item);
            list.appendTag(item);
        }
        if(buttonCamo.getItemStack() != null) {
            NBTTagCompound item = new NBTTagCompound();
            item.setByte("slotIs", (byte) 1);
            buttonCamo.getItemStack().writeToNBT(item);
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
        this.pressDelay = par1.getInteger("pressDelay");

        NBTTagList list = par1.getTagList("slotContents", 10);
        for(int i=0; i<list.tagCount(); i++) {
            byte slotNum = list.getCompoundTagAt(i).getByte("slotIs");
            if(slotNum == 0)
                this.plateCamo.setItemStack(ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i)));
            else if(slotNum == 1)
                this.buttonCamo.setItemStack(ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i)));
        }

    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.getNbtCompound();
        readFromNBT(tag);
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }


    @Override
    public void updateEntity()
    {
        if (!moving)
            return;

        long ticksPassed = Utilities.timeToTick(System.currentTimeMillis() - getStart());
        if (ticksPassed > animationLengthTicks) {
            setNewState(state == State.PUSHING ? State.PUSHED : State.IDLE);
            this.notifyNeighbors();
        }
    }


    public void notifyNeighbors()
    {
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
        if(getDirection() == ForgeDirection.NORTH)
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord-1, getBlockType());
        if(getDirection() == ForgeDirection.EAST)
            worldObj.notifyBlocksOfNeighborChange(xCoord+1, yCoord, zCoord, getBlockType());
        if(getDirection() == ForgeDirection.SOUTH)
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord+1, getBlockType());
        if(getDirection() == ForgeDirection.WEST)
            worldObj.notifyBlocksOfNeighborChange(xCoord-1, yCoord, zCoord, getBlockType());
    }

    public class SolidSlot extends MalisisSlot
    {
        public SolidSlot(int index)
        {
            super(index);
        }

        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            Item iStack = itemStack.getItem();
            if(iStack != null) {
                Block biq = Block.getBlockFromItem(iStack);
                if (biq == null) return false;
                return biq.isBlockNormalCube();
            }
            return false;
        }
    }
}
