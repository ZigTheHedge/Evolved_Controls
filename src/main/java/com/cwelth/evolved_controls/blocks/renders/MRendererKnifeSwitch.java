package com.cwelth.evolved_controls.blocks.renders;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
import com.cwelth.evolved_controls.blocks.tileentities.TEGenericControl;
import com.cwelth.evolved_controls.blocks.tileentities.TEKnifeSwitch;
import cpw.mods.fml.common.FMLLog;
import net.malisis.core.renderer.animation.transformation.Rotation;
import net.malisis.core.renderer.animation.transformation.Translation;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.element.shape.Cube;
import net.malisis.core.renderer.model.MalisisModel;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

/**
 * Created by ZtH on 24.10.2015.
 */
public class MRendererKnifeSwitch extends MGenericControlRenderer {

    private TEKnifeSwitch te;
    private Shape switchPlate;
    private Shape switchHandle;
    private MalisisModel switchModel;

    @Override
    protected void initialize () {
        super.initialize();

        rl = new ResourceLocation(ModMain.MODID, "models/knife_switch.obj");
        if(rl == null)
            System.out.println("rl is null!");

        switchModel = new MalisisModel(rl);
        switchPlate = switchModel.getShape("plate");
        switchPlate.interpolateUV();
        switchPlate.storeState();
        if(switchPlate == null)
            System.out.println("switchPlate is null!");
        switchHandle = switchModel.getShape("handle");

        //rp.interpolateUV.set(true);


    }

    @Override
    protected void renderTileEntity() {
        if (super.tileEntity == null)
            return;
        te = (TEKnifeSwitch) super.tileEntity;
        dir = te.getDirection();
        setupRotation(switchPlate);
        setupRotation(switchHandle);
        rp.direction.set(te.getDirection());

        ar.setStartTime(te.getStart());

        Rotation switchLever = new Rotation(180, 1, 0, 0, 0, 0.01F, -0.411F).forTicks(te.getAnimationLengthTicks(), 0);
        switchLever.reversed(te.getState() == TEGenericControl.State.TURNINGOFF);

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

        drawShape(switchPlate, rp);



        if(te.isMoving() || te.getState() == TEFancyButton.State.ON) {
            ar.animate(switchHandle, switchLever);
        }

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
    }

    @Override
    protected void renderInventory() {
        switchPlate.resetState();
        switchHandle.resetState();
        switchPlate.translate(0, 0, 0.7F);
        switchHandle.translate(0, 0, 0.7F);
        switchPlate.scale(1.5F);
        switchHandle.scale(1.5F);
        drawShape(switchPlate, rp);
        drawShape(switchHandle, rp);

    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }
}
