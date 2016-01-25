package com.cwelth.evolved_controls.blocks.renders;

import net.malisis.core.renderer.MalisisRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.RenderType;
import net.malisis.core.renderer.animation.AnimationRenderer;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.model.MalisisModel;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by ZtH on 24.10.2015.
 */
public abstract class MGenericControlRenderer extends MalisisRenderer {
    protected ForgeDirection dir;
    protected ResourceLocation rl;
    protected AnimationRenderer ar = new AnimationRenderer();
    protected RenderParameters rp;

    @Override
    protected void initialize () {
        rp = new RenderParameters();


        rp.renderAllFaces.set(false);
        rp.calculateAOColor.set(false);
        rp.useBlockBounds.set(false);
        rp.useEnvironmentBrightness.set(false);
        rp.brightness.reset();
        rp.calculateBrightness.set(false);
        rp.interpolateUV.set(false);
        rp.useWorldSensitiveIcon.set(false);
        rp.useNormals.set(true);

    }

    @Override
    public void render () {
        if (renderType == RenderType.ISBRH_WORLD)
            return;

        if (renderType == RenderType.TESR_WORLD) {
            //rp.direction.set(dir);

            renderTileEntity();
        }

        if (renderType == RenderType.ITEM_INVENTORY)
            renderInventory();

    }

    protected abstract void renderTileEntity();
    protected abstract void renderInventory();

    protected void setupRotation (MalisisModel forForm) {
        forForm.resetState();
        for(Shape singleShape: forForm)
            setupRotation(singleShape);
    }

    protected void setupRotation (Shape forForm) {
        forForm.resetState();
        if (dir == ForgeDirection.SOUTH) {
            forForm.rotate(180, 0, 1, 0, 0, 0, 0);
        }
        else if (dir == ForgeDirection.EAST) {
            forForm.rotate(-90, 0, 1, 0, 0, 0, 0);
        }
        else if (dir == ForgeDirection.WEST) {
            forForm.rotate(90, 0, 1, 0, 0, 0, 0);
        }
    }

    protected int getColor(Block block)
    {
        if (block == Blocks.grass)
            return 0xFFFFFF;
        return renderType == RenderType.TESR_WORLD ? block.colorMultiplier(world, x, y, z) : block.getBlockColor();
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

}
