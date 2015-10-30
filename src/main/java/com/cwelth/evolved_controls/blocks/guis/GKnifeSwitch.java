package com.cwelth.evolved_controls.blocks.guis;

import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
import com.cwelth.evolved_controls.blocks.tileentities.TEKnifeSwitch;
import com.cwelth.evolved_controls.network.NWFancyButton;
import com.google.common.base.Predicate;
import net.malisis.core.client.gui.Anchor;
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.UISlot;
import net.malisis.core.client.gui.component.container.UIPlayerInventory;
import net.malisis.core.client.gui.component.container.UIWindow;
import net.malisis.core.client.gui.component.decoration.UILabel;
import net.malisis.core.client.gui.component.interaction.UITextField;
import net.malisis.core.inventory.MalisisInventoryContainer;

/**
 * Created by ZtH on 24.10.2015.
 */
public class GKnifeSwitch extends MalisisGui {

    private TEKnifeSwitch te;
    UISlot plateCamo;
    UISlot handleCamo;

    public GKnifeSwitch(MalisisInventoryContainer inventoryContainer, TEKnifeSwitch tileEntity) {
        setInventoryContainer(inventoryContainer);
        this.te = tileEntity;
        guiscreenBackground = false;
    }

    @Override
    public void construct() {
        UIWindow window = new UIWindow(this, 200, 160).setPosition(0, 0, Anchor.CENTER | Anchor.MIDDLE).setZIndex(0);

        UILabel plateCamoLabel = new UILabel(this, "Plate texture").setPosition(16, 5, Anchor.LEFT);
        UILabel buttonCamoLabel = new UILabel(this, "Button texture").setPosition(-1, 5, Anchor.RIGHT);

        window.add(plateCamoLabel);
        window.add(buttonCamoLabel);


        plateCamo = new UISlot(this, te.plateCamo).setPosition(-20, 15, Anchor.CENTER);
        handleCamo = new UISlot(this, te.handleCamo).setPosition(20, 15, Anchor.CENTER);

        UIPlayerInventory playerInv = new UIPlayerInventory(this, inventoryContainer.getPlayerInventory());

        window.add(plateCamo);
        window.add(handleCamo);
        window.add(playerInv);

        addToScreen(window);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
