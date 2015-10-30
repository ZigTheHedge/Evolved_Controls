package com.cwelth.evolved_controls.blocks;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.renders.MRendererFancyButton;
import com.cwelth.evolved_controls.blocks.renders.MRendererFancyHandle;
import com.cwelth.evolved_controls.blocks.renders.MRendererKnifeSwitch;
import com.cwelth.evolved_controls.blocks.renders.MRendererSwitchButton;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyHandle;
import com.cwelth.evolved_controls.blocks.tileentities.TEKnifeSwitch;
import com.cwelth.evolved_controls.blocks.tileentities.TESwitchButton;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;


/**
 * Created by zth on 13/10/15.
 */
public class Blocks {

    public static MBlockFancyButton fancyButton = new MBlockFancyButton("fancy_button", Material.rock);
    public static MBlockFancyHandle fancyHandle = new MBlockFancyHandle("fancy_handle", Material.rock);
    public static MBlockKnifeSwitch knifeSwitch = new MBlockKnifeSwitch("knife_switch", Material.rock);
    public static MBlockSwitchButton switchButton = new MBlockSwitchButton("switch_button", Material.rock);

    public static final void init()
    {
        GameRegistry.registerBlock(fancyButton, "fancy_button");
        GameRegistry.registerTileEntity(TEFancyButton.class, "fancy_button");

        GameRegistry.registerBlock(fancyHandle, "fancy_handle");
        GameRegistry.registerTileEntity(TEFancyHandle.class, "fancy_handle");

        GameRegistry.registerBlock(knifeSwitch, "knife_switch");
        GameRegistry.registerTileEntity(TEKnifeSwitch.class, "knife_switch");

        GameRegistry.registerBlock(switchButton, "switch_button");
        GameRegistry.registerTileEntity(TESwitchButton.class, "switch_button");

        initClient();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient()
    {
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

        System.out.println("initClient Called!");
    }

}
