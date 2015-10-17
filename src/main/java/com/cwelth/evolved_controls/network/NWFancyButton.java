package com.cwelth.evolved_controls.network;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.malisis.core.util.TileEntityUtils;
import net.minecraft.world.World;

/**
 * Created by ZtH on 16.10.2015.
 */
public class NWFancyButton implements IMessageHandler<NWFancyButton.Packet, IMessage> {

    public NWFancyButton()
    {
        ModMain.network.registerMessage(this, NWFancyButton.Packet.class, Side.SERVER);
    }

    @Override
    public IMessage onMessage(Packet message, MessageContext ctx) {
        World world = ctx.getServerHandler().playerEntity.worldObj;
        TEFancyButton te = TileEntityUtils.getTileEntity(TEFancyButton.class,world, message.x, message.y, message.z);
        if (te == null)
            return null;
        te.setPressDelay(message.pressDelay);
        return null;
    }

    public static void send(TEFancyButton te)
    {
        ModMain.network.sendToServer(new Packet(te));
    }

    public static class Packet implements IMessage
    {
        private int x, y, z;
        private int pressDelay;

        public Packet(TEFancyButton te)
        {
            x = te.xCoord;
            y = te.yCoord;
            z = te.zCoord;
            pressDelay = te.getPressDelay();
        }

        public Packet()
        {}

        @Override
        public void fromBytes(ByteBuf buf)
        {
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
            pressDelay = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
            buf.writeInt(pressDelay);
        }
    }
}
