package com.cwelth.evolved_controls.blocks.renders;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.tileentities.TEGenericControl;
import com.cwelth.evolved_controls.blocks.tileentities.TEStationaryHandle;
import net.malisis.core.renderer.animation.transformation.ChainedTransformation;
import net.malisis.core.renderer.animation.transformation.Rotation;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.model.MalisisModel;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by ZtH on 30.10.2015.
 */
public class MRendererStationaryHandle extends MGenericControlRenderer {
    private TEStationaryHandle te;
    private Shape switchPlate;
    private Shape switchHandle;
    private Shape switchStopper;
    private MalisisModel switchModel;

    @Override
    protected void initialize () {
        super.initialize();

        rl = new ResourceLocation(ModMain.MODID, "models/stationary_handle.obj");
        if(rl == null)
            System.out.println("rl is null!");

        switchModel = new MalisisModel(rl);
        switchPlate = switchModel.getShape("base");
        switchPlate.storeState();
        if(switchPlate == null)
            System.out.println("switchPlate is null!");
        switchHandle = switchModel.getShape("handle");
        switchStopper = switchModel.getShape("stopper");

    }

    @Override
    protected void renderTileEntity() {
        if (super.tileEntity == null)
            return;
        te = (TEStationaryHandle) super.tileEntity;
        dir = te.getDirection();

        setupRotation(switchPlate);
        setupRotation(switchHandle);
        setupRotation(switchStopper);

        switchHandle.rotate(-10, 1, 0, 0, 0, -0.5F, 0);
        switchStopper.rotate(-10, 1, 0, 0, 0, -0.5F, 0);

        rp.direction.set(te.getDirection());

        ar.setStartTime(te.getStart());

        Rotation switchLever = new Rotation(20, 1, 0, 0, 0, -0.5F, 0).forTicks(te.getAnimationLengthTicks(), te.getAnimationLengthTicks());
        switchLever.reversed(te.getState() == TEGenericControl.State.TURNINGOFF);

        Rotation stopperTopPush = new Rotation(7, 1, 0, 0, 0, 0.70F, -0.1F).forTicks(te.getAnimationLengthTicks(), 0);
        Rotation stopperTopUpPush = new Rotation(-7, 1, 0, 0, 0, 0.70F, -0.1F).forTicks(te.getAnimationLengthTicks(), te.getAnimationLengthTicks()*2-2);
        ChainedTransformation animateAll = new ChainedTransformation(stopperTopPush);
        animateAll.addTransformations(stopperTopUpPush);


        if(te.isMoving() || te.getState() == TEGenericControl.State.ON) {
            ar.animate(switchHandle, switchLever);
            ar.animate(switchStopper, switchLever);
            ar.animate(switchStopper, animateAll);
        }

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
        //rp.setBrightness(15728880); //!!!!// !!!!s

        next(GL11.GL_POLYGON);
        drawShape(switchPlate, rp);

        if(te.handleCamo.getItemStack() != null) {
            Block block = Block.getBlockFromItem(te.handleCamo.getItemStack().getItem());
            int metadata = te.handleCamo.getItemStack().getMetadata();
            rp.icon.set(block.getIcon(2, metadata));
            rp.colorMultiplier.set(getColor(block));
        } else {
            rp.icon.reset();
            rp.colorMultiplier.reset();
        }

        drawShape(switchHandle, rp);
        drawShape(switchStopper, rp);
    }

    @Override
    protected void renderInventory() {
        switchPlate.resetState();
        switchHandle.resetState();
        switchPlate.scale(0.8F);
        switchHandle.scale(0.8F);
        switchPlate.translate(0, -0.3F, 0);
        switchHandle.translate(0, -0.3F, 0);
        rp.icon.set(Blocks.planks.getIcon(2, 0));
        next(GL11.GL_POLYGON);
        drawShape(switchPlate, rp);
        rp.icon.set(Blocks.cobblestone.getIcon(2, 0));
        drawShape(switchHandle, rp);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }
}
