package com.cwelth.evolved_controls;

import com.cwelth.evolved_controls.blocks.renders.*;
import com.cwelth.evolved_controls.blocks.tileentities.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by zth on 17/12/15.
 */
public class ECClientProxy extends ECCommonProxy {

    @Override
    public void initClient()
    {
        System.out.println("client::initClient called");

        MRendererFancyButton rendererFancyButton = new MRendererFancyButton();
        rendererFancyButton.registerFor(TEFancyButton.class);
        rendererFancyButton.registerFor(Item.getItemFromBlock(fancyButton));

        MRendererFancyHandle rendererFancyHandle = new MRendererFancyHandle();
        rendererFancyHandle.registerFor(TEFancyHandle.class);
        rendererFancyHandle.registerFor(Item.getItemFromBlock(fancyHandle));

        MRendererKnifeSwitch rendererKnifeSwitch = new MRendererKnifeSwitch();
        rendererKnifeSwitch.registerFor(TEKnifeSwitch.class);
        rendererKnifeSwitch.registerFor(Item.getItemFromBlock(knifeSwitch));

        MRendererSwitchButton rendererSwitchButton = new MRendererSwitchButton();
        rendererSwitchButton.registerFor(TESwitchButton.class);
        rendererSwitchButton.registerFor(Item.getItemFromBlock(switchButton));

        MRendererStationaryHandle rendererStationaryHandle = new MRendererStationaryHandle();
        rendererStationaryHandle.registerFor(TEStationaryHandle.class);
        rendererStationaryHandle.registerFor(Item.getItemFromBlock(stationaryHandle));

        /*
        MRendererResistiveLever rendererResistiveLever = new MRendererResistiveLever();
        rendererResistiveLever.registerFor(TEResistiveLever.class);
        rendererResistiveLever.registerFor(Item.getItemFromBlock(resistiveLever));
        */

    }

    @Override
    public void startParticles(TEGenericControl theEntity)
    {
        if(theEntity == null)return;
        double motionX = theEntity.getWorld().rand.nextGaussian() * 0.02D;
        double motionY = theEntity.getWorld().rand.nextGaussian() * 0.02D;
        double motionZ = theEntity.getWorld().rand.nextGaussian() * 0.02D;
        double x = theEntity.xCoord + 0.25 + theEntity.getWorld().rand.nextFloat() / 2;
        double y = theEntity.yCoord + 0.25 + theEntity.getWorld().rand.nextFloat() / 2;
        double z = theEntity.zCoord + 0.25 + theEntity.getWorld().rand.nextFloat() / 2;

        double offset = 0.1;

        if(theEntity.getDirection() == ForgeDirection.NORTH)
            z = theEntity.zCoord + offset;
        if(theEntity.getDirection() == ForgeDirection.SOUTH)
            z = theEntity.zCoord + 1 - offset;
        if(theEntity.getDirection() == ForgeDirection.EAST)
            x = theEntity.xCoord + 1 - offset;
        if(theEntity.getDirection() == ForgeDirection.WEST)
            x = theEntity.xCoord + offset;

        SparksEntityFX fx = new SparksEntityFX(theEntity.getWorld(), x, y, z, motionX, motionY, motionZ, Minecraft.getMinecraft().effectRenderer);

        Minecraft.getMinecraft().effectRenderer.addEffect(fx);
    }

}
