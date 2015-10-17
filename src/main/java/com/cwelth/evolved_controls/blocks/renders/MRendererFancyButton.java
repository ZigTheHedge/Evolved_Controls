package com.cwelth.evolved_controls.blocks.renders;

import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
import net.malisis.core.renderer.MalisisRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.RenderType;
import net.malisis.core.renderer.animation.AnimationRenderer;
import net.malisis.core.renderer.animation.transformation.ChainedTransformation;
import net.malisis.core.renderer.animation.transformation.Translation;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.element.shape.Cube;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by zth on 14/10/15.
 */
public class MRendererFancyButton extends MalisisRenderer {

    private TEFancyButton te;
    private ForgeDirection dir;
    private Shape buttonPlate;
    private Shape buttonItself;
    private AnimationRenderer ar = new AnimationRenderer();
    protected RenderParameters rp;

    @Override
    protected void initialize () {

        buttonPlate = new Cube();
        buttonPlate.setSize(0.8F, 0.8F, 0.05F);
        buttonPlate.interpolateUV();
        buttonPlate.translate(0.1F, 0.1F, 0F);
        buttonPlate.storeState();

        buttonItself = new Cube();
        buttonItself.setSize(0.6F, 0.6F, 0.05F);
        buttonItself.interpolateUV();
        buttonItself.translate(0.2F, 0.2F, 0.05F);
        buttonItself.storeState();


        rp = new RenderParameters();
        rp.useBlockBounds.set(false);
        rp.useEnvironmentBrightness.set(false);
        rp.calculateBrightness.set(false);
        rp.interpolateUV.set(false);
    }

    @Override
    public void render () {
        if (super.tileEntity == null)
            return;

        //rp.interpolateUV.set(true);

        if (renderType == RenderType.ISBRH_WORLD)
            return;

        te = (TEFancyButton) super.tileEntity;
        dir = te.getDirection();
        setupRotation(buttonPlate);
        setupRotation(buttonItself);
        rp.direction.set(te.getDirection());


        if (renderType == RenderType.TESR_WORLD)
            renderTileEntity();
    }

    private void renderTileEntity () {
        ar.setStartTime(te.getStart());
        Translation pushTranslation = new Translation(0, 0, 0, 0, 0, -0.049F).forTicks(te.getAnimationLengthTicks(), 0);
        pushTranslation.reversed(te.getState() == TEFancyButton.State.IDELING);

        if(te.plateCamo.getItemStack() != null) {
            Block block = Block.getBlockFromItem(te.plateCamo.getItemStack().getItem());
            int metadata = te.plateCamo.getItemStack().getMetadata();
            rp.icon.set(block.getIcon(2, metadata));
            rp.colorMultiplier.set(getColor(block));
        }
        else {
            rp.icon.reset();
            rp.colorMultiplier.reset();
        }

        drawShape(buttonPlate, rp);

        if(te.isMoving() || te.getState() == TEFancyButton.State.PUSHED) {
            ar.animate(buttonItself, pushTranslation);
        }

        if(te.buttonCamo.getItemStack() != null) {
            Block block = Block.getBlockFromItem(te.buttonCamo.getItemStack().getItem());
            int metadata = te.buttonCamo.getItemStack().getMetadata();
            rp.icon.set(block.getIcon(2, metadata));
            rp.colorMultiplier.set(getColor(block));
        } else {
            rp.icon.reset();
            rp.colorMultiplier.reset();
        }


        drawShape(buttonItself, rp);

    }

    private void setupRotation (Shape forForm) {
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

    private int getColor(Block block)
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
