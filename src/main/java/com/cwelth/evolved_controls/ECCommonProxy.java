package com.cwelth.evolved_controls;

import com.cwelth.evolved_controls.blocks.*;
import com.cwelth.evolved_controls.blocks.tileentities.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Created by zth on 17/12/15.
 */
public class ECCommonProxy {

    public static MBlockFancyButton fancyButton = new MBlockFancyButton("fancy_button", Material.rock);
    public static MBlockFancyHandle fancyHandle = new MBlockFancyHandle("fancy_handle", Material.rock);
    public static MBlockKnifeSwitch knifeSwitch = new MBlockKnifeSwitch("knife_switch", Material.rock);
    public static MBlockSwitchButton switchButton = new MBlockSwitchButton("switch_button", Material.rock);
    public static MBlockStationaryHandle stationaryHandle = new MBlockStationaryHandle("stationary_handle", Material.rock);
    //public static MBlockResistiveLever resistiveLever = new MBlockResistiveLever("resistive_lever", Material.rock);

    public void init()
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

        //GameRegistry.registerBlock(resistiveLever, "resistive_lever");
        //GameRegistry.registerTileEntity(TEResistiveLever.class, "resistive_lever");

        registerRecipes();
        System.out.println("common::init called");
    }

    public void registerRecipes()
    {
        GameRegistry.addRecipe(new ItemStack(fancyButton), "AAA", " B ", "   ", 'A', Blocks.stone_slab, 'B', Blocks.stone_button);
        GameRegistry.addRecipe(new ItemStack(fancyHandle), "AAA", " B ", "   ", 'A', Blocks.stone_slab, 'B', Blocks.lever);
        GameRegistry.addRecipe(new ItemStack(knifeSwitch), "AAA", " B ", " B ", 'A', Blocks.stone_slab, 'B', Blocks.lever);
        GameRegistry.addRecipe(new ItemStack(switchButton), "AAA", "DBD", " C ", 'A', Blocks.stone_slab, 'B', Blocks.stone_button, 'C', Blocks.lever, 'D', Items.glowstone_dust);
        GameRegistry.addRecipe(new ItemStack(stationaryHandle), "AAA", " B ", " B ", 'A', Blocks.stone, 'B', Blocks.lever);
        System.out.println("common::registerRecipes called");
    }

    public void initClient()
    {
        System.out.println("common::initClient called");
    }

    public void startParticles(TEGenericControl theEntity)
    {

    }

}
