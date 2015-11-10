package com.cwelth.evolved_controls.blocks;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.renders.*;
import com.cwelth.evolved_controls.blocks.tileentities.*;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


/**
 * Created by zth on 13/10/15.
 */
public class ModBlocks {

    public static MBlockFancyButton fancyButton = new MBlockFancyButton("fancy_button", Material.rock);
    public static MBlockFancyHandle fancyHandle = new MBlockFancyHandle("fancy_handle", Material.rock);
    public static MBlockKnifeSwitch knifeSwitch = new MBlockKnifeSwitch("knife_switch", Material.rock);
    public static MBlockSwitchButton switchButton = new MBlockSwitchButton("switch_button", Material.rock);
    public static MBlockStationaryHandle stationaryHandle = new MBlockStationaryHandle("stationary_handle", Material.rock);

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

        GameRegistry.registerBlock(stationaryHandle, "stationary_handle");
        GameRegistry.registerTileEntity(TEStationaryHandle.class, "stationary_handle");

        registerRecipes();
        initClient();
    }

    public static void registerRecipes()
    {
        GameRegistry.addRecipe(new ItemStack(fancyButton), "AAA", " B ", "   ", 'A', Blocks.stone_slab, 'B', Blocks.stone_button);
        GameRegistry.addRecipe(new ItemStack(fancyHandle), "AAA", " B ", "   ", 'A', Blocks.stone_slab, 'B', Blocks.lever);
        GameRegistry.addRecipe(new ItemStack(knifeSwitch), "AAA", " B ", " B ", 'A', Blocks.stone_slab, 'B', Blocks.lever);
        GameRegistry.addRecipe(new ItemStack(switchButton), "AAA", "DBD", " C ", 'A', Blocks.stone_slab, 'B', Blocks.stone_button, 'C', Blocks.lever, 'D', Items.glowstone_dust);
        GameRegistry.addRecipe(new ItemStack(stationaryHandle), "AAA", " B ", " B ", 'A', Blocks.stone, 'B', Blocks.lever);
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

        MRendererStationaryHandle rendererStationaryHandle = new MRendererStationaryHandle();
        rendererStationaryHandle.registerFor(TEStationaryHandle.class);
        rendererStationaryHandle.registerFor(Item.getItemFromBlock(stationaryHandle));

        System.out.println("initClient Called!");
    }

}
