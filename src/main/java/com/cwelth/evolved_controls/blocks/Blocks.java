package com.cwelth.evolved_controls.blocks;

import com.cwelth.evolved_controls.blocks.renders.MRendererFancyButton;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
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

        initClient();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient()
    {
        MRendererFancyButton renderer = new MRendererFancyButton();
        renderer.registerFor(MBlockFancyButton.class, TEFancyButton.class);
        System.out.println("initClient Called!");
    }

}
