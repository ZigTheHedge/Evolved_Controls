package com.cwelth.evolved_controls.blocks.tileentities;

import com.cwelth.evolved_controls.utils.Direction;
import com.cwelth.evolved_controls.utils.Utilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by ZtH on 21.10.2015.
 */
public class TEFancyHandle extends TileEntity {

    private Direction direction = new Direction(ForgeDirection.NORTH);
    protected long timeStart;

    public enum State
    {
        OFF, TURNINGON, ON, TURNINGOFF
    }
    protected State state = State.OFF;
    protected boolean moving = false;
    private int animationLengthTicks = 6;

    public Direction getDirection()
    {
        return this.direction;
    }

    public long getStart() {
        return this.timeStart;
    }

    public int getAnimationLengthTicks() {
        return this.animationLengthTicks;
    }

    public State getState() {
        return this.state;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setStart()
    {
        this.setStart(System.currentTimeMillis());
    }

    public void setStart(long timeStart)
    {
        this.timeStart = timeStart;
    }

    public void pushMe()
    {
        State newState = (state == State.OFF) ? State.TURNINGON : State.TURNINGOFF;
        setStart();
        setNewState(newState);

    }

    public boolean isMoving()
    {
        return this.moving;
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

    @Override
    public void updateEntity()
    {
        if (!moving)
            return;

        long ticksPassed = Utilities.timeToTick(System.currentTimeMillis() - getStart());
        if (ticksPassed > animationLengthTicks) {
            setNewState(state == State.TURNINGON ? State.ON : State.OFF);
            //this.notifyNeighbors();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1)
    {
        super.writeToNBT(par1);
        par1.setInteger("direction", direction.getMeta());
        par1.setInteger("state", state.ordinal());
        par1.setBoolean("moving", this.moving);
        par1.setLong("timeStart", this.timeStart);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1) {
        super.readFromNBT(par1);
        this.direction = new Direction(par1.getInteger("direction"));
        this.state = State.values()[par1.getInteger("state")];
        this.moving = par1.getBoolean("moving");
        this.timeStart = par1.getLong("timeStart");
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

}
