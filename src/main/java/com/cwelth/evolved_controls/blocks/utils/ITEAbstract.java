package com.cwelth.evolved_controls.blocks.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

/**
 * Created by ZtH on 16.10.2015.
 */
public interface ITEAbstract {

    public void writeToNBT(NBTTagCompound par1);
    public void readFromNBT(NBTTagCompound par1);
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt);
    public Packet getDescriptionPacket();

}
