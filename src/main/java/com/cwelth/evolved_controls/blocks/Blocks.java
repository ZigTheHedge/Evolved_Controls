package com.cwelth.evolved_controls.blocks;

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


/**
 * Created by zth on 13/10/15.
 */
public class Blocks {

    public static MBlockFancyButton fancyButton;

    public static final void init()
    {
        GameRegistry.registerBlock(new MBlockFancyButton("fancy_button", Material.rock), "fancy_button");
        GameRegistry.registerTileEntity(TEFancyButton.class, "fancy_button");

        GameRegistry.registerBlock(new MBlockFancyHandle("fancy_handle", Material.rock), "fancy_handle");
        GameRegistry.registerTileEntity(TEFancyHandle.class, "fancy_handle");

        GameRegistry.registerBlock(new MBlockKnifeSwitch("knife_switch", Material.rock), "knife_switch");
        GameRegistry.registerTileEntity(TEKnifeSwitch.class, "knife_switch");

        GameRegistry.registerBlock(new MBlockSwitchButton("switch_button", Material.rock), "switch_button");
        GameRegistry.registerTileEntity(TESwitchButton.class, "switch_button");

        initClient();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient()
    {
        MRendererFancyButton rendererFancyButton = new MRendererFancyButton();
        rendererFancyButton.registerFor(MBlockFancyButton.class, TEFancyButton.class);

        MRendererFancyHandle rendererFancyHandle = new MRendererFancyHandle();
        rendererFancyHandle.registerFor(MBlockFancyHandle.class, TEFancyHandle.class);

        MRendererKnifeSwitch rendererKnifeSwitch = new MRendererKnifeSwitch();
        rendererKnifeSwitch.registerFor(MBlockKnifeSwitch.class, TEKnifeSwitch.class);

        MRendererSwitchButton rendererSwitchButton = new MRendererSwitchButton();
        rendererSwitchButton.registerFor(MRendererSwitchButton.class, TESwitchButton.class);

        System.out.println("initClient Called!");
    }

}
