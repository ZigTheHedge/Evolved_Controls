package com.cwelth.evolved_controls.blocks.renders;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
import com.cwelth.evolved_controls.blocks.tileentities.TEGenericControl;
import cpw.mods.fml.common.FMLLog;
import net.malisis.core.renderer.MalisisRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.RenderType;
import net.malisis.core.renderer.animation.AnimationRenderer;
import net.malisis.core.renderer.animation.transformation.ChainedTransformation;
import net.malisis.core.renderer.animation.transformation.Translation;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.element.shape.Cube;
import net.malisis.core.renderer.model.MalisisModel;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by zth on 14/10/15.
 */
public class MRendererFancyButton extends MGenericControlRenderer {

    private TEFancyButton te;
    private Shape buttonPlate;
    private MalisisModel buttonModel;

    @Override
    protected void initialize () {
        super.initialize();

        buttonPlate = new Cube();
        buttonPlate.setSize(0.8F, 0.8F, 0.05F);
        //buttonPlate.interpolateUV();
        buttonPlate.translate(0.1F, 0.1F, 0F);
        buttonPlate.storeState();

        rl = new ResourceLocation(ModMain.MODID, "models/fancy_button.obj");
        buttonModel = new MalisisModel(rl);
        buttonModel.translate(0, 0, 0.05F);
        buttonModel.storeState();

    }

    protected void renderTileEntity() {
        if (super.tileEntity == null)
            return;
        te = (TEFancyButton) super.tileEntity;
        dir = te.getDirection();
        setupRotation(buttonPlate);
        setupRotation(buttonModel);
        rp.direction.set(te.getDirection());

        ar.setStartTime(te.getStart());
        Translation pushTranslation = new Translation(0, 0, 0, 0, 0, -0.049F).forTicks(te.getAnimationLengthTicks(), 0);
        pushTranslation.reversed(te.getState() == TEFancyButton.State.TURNINGOFF);

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

        if(te.isMoving() || te.getState() == TEFancyButton.State.ON) {
            ar.animate(buttonModel, pushTranslation);
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

        buttonModel.render(this, rp);

    }

    @Override
    protected void renderInventory() {
        buttonPlate.resetState();
        buttonModel.resetState();
        buttonPlate.translate(0, 0, 0.7F);
        buttonModel.translate(0, 0, 0.7F);
        buttonPlate.scale(1.5F);
        buttonModel.scale(1.5F, 1.5F, 1.5F, 0, 0, 0);
        drawShape(buttonPlate, rp);
        buttonModel.render(this, rp);

    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

}
