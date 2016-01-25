package com.cwelth.evolved_controls.blocks.renders;

import com.cwelth.evolved_controls.ModMain;
import com.cwelth.evolved_controls.blocks.tileentities.TEResistiveLever;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.font.FontRenderOptions;
import net.malisis.core.renderer.font.MalisisFont;
import net.malisis.core.renderer.model.MalisisModel;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by zth on 12/11/15.
 */
public class MRendererResistiveLever extends MGenericControlRenderer {
    private TEResistiveLever te;
    private Shape handle;
    private Shape plate;
    private Shape informer;
    private Shape button;

    private MalisisModel control;

    @Override
    protected void initialize () {
        super.initialize();

        rl = new ResourceLocation(ModMain.MODID, "models/resistor_lever.obj");
        if(rl == null)
            System.out.println("rl is null!");

        control = new MalisisModel(rl);
        plate = control.getShape("plate");
        if(plate == null)
            System.out.println("plate is null!");
        handle = control.getShape("handle");
        if(handle == null)
            System.out.println("handle is null!");
        informer = control.getShape("informer");
        if(informer == null)
            System.out.println("informer is null!");
        button = control.getShape("button");
        if(button == null)
            System.out.println("button is null!");

    }

    protected void renderTileEntity () {
        if (super.tileEntity == null)
            return;


        te = (TEResistiveLever) super.tileEntity;
        dir = te.getDirection();
        setupRotation(plate);
        setupRotation(informer);
        setupRotation(handle);
        setupRotation(button);

        rp.direction.set(te.getDirection());

        //ar.setStartTime(te.getStart());

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

        next(GL11.GL_POLYGON);
        drawShape(plate, rp);

        //drawShape(informer, rp);

/*
        if(te.isMoving() || te.getState() == TEFancyHandle.State.ON)
            ar.animate(handleItself, rotateHandle);
*/

        if(te.handleCamo.getItemStack() != null) {
            Block block = Block.getBlockFromItem(te.handleCamo.getItemStack().getItem());
            int metadata = te.handleCamo.getItemStack().getMetadata();
            rp.icon.set(block.getIcon(2, metadata));
            rp.colorMultiplier.set(getColor(block));
        } else {
            rp.icon.reset();
            rp.colorMultiplier.reset();
        }


        //drawShape(handle, rp);

        //drawShape(button, rp);

        //drawText(null, "XX", 0.37F, 0.66F, 0.074F, fro);

        MalisisFont mf = MalisisFont.minecraftFont;
        Shape infoText = mf.getShape("XX", .2F);
        infoText.translate(0.37F, 0.66F, 0.074F);
        infoText.storeState();
        setupRotation(infoText);

        drawShape(infoText, rp);

    }


    @Override
    protected void renderInventory() {

        handle.resetState();
        plate.resetState();
        handle.translate(0, -0.1F, 0.7F);
        plate.translate(0, -0.1F, 0.7F);
        handle.scale(1.5F);
        plate.scale(1.5F);
        next(GL11.GL_POLYGON);
        rp.icon.set(Blocks.planks.getIcon(2, 0));
        drawShape(plate, rp);
        rp.icon.set(Blocks.cobblestone.getIcon(2, 0));
        drawShape(handle, rp);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }
}
