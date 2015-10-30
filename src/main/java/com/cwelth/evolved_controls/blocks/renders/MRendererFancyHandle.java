package com.cwelth.evolved_controls.blocks.renders;


import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyHandle;
import cpw.mods.fml.common.FMLLog;
import net.malisis.core.renderer.MalisisRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.RenderType;
import net.malisis.core.renderer.animation.AnimationRenderer;
import net.malisis.core.renderer.animation.transformation.Rotation;
import net.malisis.core.renderer.animation.transformation.Translation;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.element.shape.Cube;
import net.malisis.core.renderer.model.MalisisModel;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by ZtH on 21.10.2015.
 */
public class MRendererFancyHandle extends MGenericControlRenderer {

    private TEFancyHandle te;
    private Shape handlePlate;
    private Shape shapeShield;

    private MalisisModel handleItself;

    @Override
    protected void initialize () {
        super.initialize();

        handlePlate = new Cube();
        handlePlate.setSize(0.8F, 0.8F, 0.05F);
        handlePlate.interpolateUV();
        handlePlate.translate(0.1F, 0.1F, 0F);
        handlePlate.storeState();

        shapeShield = new Cube();
        shapeShield.setSize(0.6F, 0.3F, 0.15F);
        shapeShield.translate(0.2F, 0.2F, 0.05F);
        shapeShield.storeState();


        Shape shapeHandle = new Cube();
        shapeHandle.setSize(0.1F, 0.6F, 0.05F);
        shapeHandle.translate(0.45F, 0.25F, 0.1F);
        shapeHandle.storeState();

        Shape shapeKnob = new Cube();
        shapeKnob.setSize(0.2F, 0.2F, 0.08F);
        shapeKnob.translate(0.4F, 0.65F, 0.08F);
        shapeKnob.storeState();

        handleItself = new MalisisModel();
        handleItself.addShape(shapeHandle);
        handleItself.addShape(shapeKnob);
        //handleItself.translate(-0.5F, 0, 0);
        handleItself.rotate(25, 0, 0, 1, 0F, -0.3F, 0);
        handleItself.storeState();

        rp = new RenderParameters();
        rp.useBlockBounds.set(false);
        rp.useEnvironmentBrightness.set(false);
        rp.calculateBrightness.set(false);
        rp.interpolateUV.set(false);
    }

    protected void renderTileEntity () {
        if (super.tileEntity == null)
            return;

        te = (TEFancyHandle) super.tileEntity;
        dir = te.getDirection();
        setupRotation(handlePlate);
        setupRotation(shapeShield);
        setupRotation(handleItself);

        rp.direction.set(te.getDirection());

        ar.setStartTime(te.getStart());

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

        drawShape(handlePlate, rp);
        drawShape(shapeShield, rp);

        Rotation rotateHandle = new Rotation(-50, 0, 0, 1, 0F, -0.3F, 0).forTicks(te.getAnimationLengthTicks(), 0);
        rotateHandle.reversed(te.getState() == TEFancyHandle.State.TURNINGOFF);

        if(te.isMoving() || te.getState() == TEFancyHandle.State.ON)
            ar.animate(handleItself, rotateHandle);

        if(te.handleCamo.getItemStack() != null) {
            Block block = Block.getBlockFromItem(te.handleCamo.getItemStack().getItem());
            int metadata = te.handleCamo.getItemStack().getMetadata();
            rp.icon.set(block.getIcon(2, metadata));
            rp.colorMultiplier.set(getColor(block));
        } else {
            rp.icon.reset();
            rp.colorMultiplier.reset();
        }

        handleItself.render(this, rp);
    }


    @Override
    protected void renderInventory() {
        handlePlate.resetState();
        shapeShield.resetState();
        handleItself.resetState();
        handlePlate.translate(0, 0, 0.7F);
        shapeShield.translate(0, 0, 0.7F);
        handleItself.translate(0, 0, 0.7F);
        handlePlate.scale(1.5F);
        shapeShield.scale(1.5F);
        handleItself.scale(1.5F, 1.5F, 1.5F, 0, 0, 0);
        drawShape(handlePlate, rp);
        drawShape(shapeShield, rp);
        handleItself.render(this, rp);

    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

}
