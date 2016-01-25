package com.cwelth.evolved_controls.blocks.tileentities;

import com.cwelth.evolved_controls.utils.Utilities;
import net.malisis.core.inventory.MalisisSlot;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by ZtH on 24.10.2015.
 */
public class TEGenericControl extends TileEntity {
    protected ForgeDirection direction = ForgeDirection.NORTH;
    protected long timeStart;
    public enum State
    {
        OFF, TURNINGON, ON, TURNINGOFF
    }
    protected State state = State.OFF;
    protected boolean moving = false;
    protected int animationLengthTicks = 6;

    public int getAnimationLengthTicks() {
        return animationLengthTicks;
    }

    public void setAnimationLengthTicks(int animationLengthTicks) {
        this.animationLengthTicks = animationLengthTicks;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getStart() {
        return timeStart;
    }

    public void setStart() {
        this.setStart(System.currentTimeMillis());
    }

    public void setStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public ForgeDirection getDirection() {
        return direction;
    }

    public void setDirection(ForgeDirection direction) {
        this.direction = direction;
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

        if (state == State.TURNINGOFF || state == State.TURNINGON)
        {
            moving = true;
        }
        else
        {
            moving = false;
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void pushMe()
    {
        State newState = (state == State.OFF) ? State.TURNINGON : State.TURNINGOFF;
        setStart();
        setNewState(newState);
    }

    @Override
    public void updateEntity()
    {
        if (!moving)
            return;

        long ticksPassed = Utilities.timeToTick(System.currentTimeMillis() - getStart());
        if (ticksPassed > animationLengthTicks) {
            setNewState(state == State.TURNINGON ? State.ON : State.OFF);
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

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.getNbtCompound();
        readFromNBT(tag);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
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
                return biq.isNormalCube();
            }
            return false;
        }
    }
}
