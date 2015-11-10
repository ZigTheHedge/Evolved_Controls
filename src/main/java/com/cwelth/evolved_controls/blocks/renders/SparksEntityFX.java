package com.cwelth.evolved_controls.blocks.renders;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFireworkSparkFX;
import net.minecraft.world.World;

/**
 * Created by ZtH on 31.10.2015.
 */
public class SparksEntityFX extends EntityFireworkSparkFX {

    public SparksEntityFX(World p_i1207_1_, double p_i1207_2_, double p_i1207_4_, double p_i1207_6_, double p_i1207_8_, double p_i1207_10_, double p_i1207_12_, EffectRenderer p_i1207_14_) {
        super(p_i1207_1_, p_i1207_2_, p_i1207_4_, p_i1207_6_, p_i1207_8_, p_i1207_10_, p_i1207_12_, p_i1207_14_);
        setRBGColorF(0.5F, 0, 0);
        this.particleMaxAge = 10 + this.rand.nextInt(12);
    }
}
