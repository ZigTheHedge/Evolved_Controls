package com.cwelth.evolved_controls;

/**
 * Created by zth on 13/10/15.
 */

import com.cwelth.evolved_controls.blocks.Blocks;
import com.cwelth.evolved_controls.network.NWFancyButton;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import net.malisis.core.IMalisisMod;
import net.malisis.core.MalisisCore;
import net.malisis.core.configuration.Settings;
import net.malisis.core.network.MalisisNetwork;

@Mod(modid = ModMain.MODID, name = ModMain.NAME, version = ModMain.VERSION, dependencies = "required-after:malisiscore;")
public class ModMain implements IMalisisMod {

    public static final String NAME = "Evolved Controls";
    public static final String MODID = "evolved_controls";
    public static final String VERSION = "0.1";

    @Mod.Instance("dt_tweaks")
    public static ModMain instance;

    public static MalisisNetwork network;

    public ModMain()
    {
        network = new MalisisNetwork(this);

        MalisisCore.registerMod(this);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
        Blocks.init();

        new NWFancyButton();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e)
    {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
    }


    @Override
    public String getModId() {
        return this.MODID;
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public String getVersion() {
        return this.VERSION;
    }

    @Override
    public Settings getSettings() {
        return null;
    }
}
