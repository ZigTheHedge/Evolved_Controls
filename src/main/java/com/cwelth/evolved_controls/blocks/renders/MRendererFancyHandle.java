package com.cwelth.evolved_controls.blocks.renders;


import com.cwelth.evolved_controls.blocks.tileentities.TEFancyHandle;
import com.cwelth.evolved_controls.utils.Direction;
import net.malisis.core.renderer.MalisisRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.RenderType;
import net.malisis.core.renderer.animation.AnimationRenderer;
import net.malisis.core.renderer.animation.transformation.Rotation;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.element.shape.Cube;
import net.malisis.core.renderer.model.MalisisModel;

/**
 * Created by ZtH on 21.10.2015.
 */
public class MRendererFancyHandle extends MalisisRenderer {

    private TEFancyHandle te;
    private Shape handlePlate;
    private Shape shapeShield;

    private MalisisModel handleItself;
    private AnimationRenderer ar = new AnimationRenderer();
    protected RenderParameters rp;

    @Override
    protected void initialize () {

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

    @Override
    public void render () {
        if (super.tileEntity == null)
            return;

        //rp.interpolateUV.set(true);

        if (renderType == RenderType.ISBRH_WORLD)
            return;

        te = (TEFancyHandle) super.tileEntity;
        Direction dir = te.getDirection();
        dir.setupRotation(handlePlate);
        dir.setupRotation(shapeShield);
        dir.setupRotation(handleItself);

        rp.direction.set(dir.getForgeDirection());

        if (renderType == RenderType.TESR_WORLD)
            renderTileEntity();
    }

    private void renderTileEntity () {
        ar.setStartTime(te.getStart());

        drawShape(handlePlate, rp);
        drawShape(shapeShield, rp);

        Rotation rotateHandle = new Rotation(-50, 0, 0, 1, 0F, -0.3F, 0).forTicks(te.getAnimationLengthTicks(), 0);
        rotateHandle.reversed(te.getState() == TEFancyHandle.State.TURNINGOFF);

        if(te.isMoving() || te.getState() == TEFancyHandle.State.ON)
            ar.animate(handleItself, rotateHandle);

        handleItself.render(this, rp);
        //ar.animate(buttonItself, pushTranslation);

        // Render handle itself
        //drawShape(buttonItself, rp);

    }
}
