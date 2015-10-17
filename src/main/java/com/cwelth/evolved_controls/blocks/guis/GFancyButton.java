package com.cwelth.evolved_controls.blocks.guis;


import com.cwelth.evolved_controls.blocks.tileentities.TEFancyButton;
import com.cwelth.evolved_controls.network.NWFancyButton;
import com.google.common.base.Predicate;
import com.google.common.eventbus.Subscribe;
import net.malisis.core.client.gui.Anchor;
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.client.gui.component.UISlot;
import net.malisis.core.client.gui.component.container.UIPlayerInventory;
import net.malisis.core.client.gui.component.container.UIWindow;
import net.malisis.core.client.gui.component.decoration.UILabel;
import net.malisis.core.client.gui.component.interaction.UITextField;
import net.malisis.core.client.gui.event.ComponentEvent;
import net.malisis.core.inventory.MalisisInventoryContainer;

/**
 * Created by ZtH on 16.10.2015.
 */
public class GFancyButton extends MalisisGui {

    private TEFancyButton te;
    UITextField pushDuration;
    UISlot plateCamo;
    UISlot buttonCamo;

    public GFancyButton(MalisisInventoryContainer inventoryContainer, TEFancyButton tileEntity) {
        setInventoryContainer(inventoryContainer);
        this.te = tileEntity;
        guiscreenBackground = false;
    }

    @Override
    public void construct() {
        UIWindow window = new UIWindow(this, 200, 160).setPosition(0, 0, Anchor.CENTER | Anchor.MIDDLE).setZIndex(0);

        UILabel plateCamoLabel = new UILabel(this, "Plate texture").setPosition(16, 5, Anchor.LEFT);
        UILabel buttonCamoLabel = new UILabel(this, "Button texture").setPosition(-10, 5, Anchor.RIGHT);
        UILabel pushDurationLabel = new UILabel(this, "Push duration (3-128 ticks)").setPosition(0, 37, Anchor.CENTER);

        window.add(plateCamoLabel);
        window.add(buttonCamoLabel);
        window.add(pushDurationLabel);


        plateCamo = new UISlot(this, te.plateCamo).setPosition(-20, 15, Anchor.CENTER);
        buttonCamo = new UISlot(this, te.buttonCamo).setPosition(20, 15, Anchor.CENTER);
        pushDuration = new UITextField(this, String.valueOf(te.getPressDelay()), false).setSize(30, 20).setPosition(0, 47, Anchor.CENTER).setAutoSelectOnFocus(true).register(this);
        pushDuration.setValidator(new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                boolean isNumber = input.matches("\\d+");
                if (!isNumber) return false;
                Integer value = Integer.parseInt(input);
                if (value < 0 || value > 128) return false;
                return true;
            }
        });


        UIPlayerInventory playerInv = new UIPlayerInventory(this, inventoryContainer.getPlayerInventory());

        window.add(plateCamo);
        window.add(buttonCamo);
        window.add(playerInv);
        window.add(pushDuration);

        addToScreen(window);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if(pushDuration.getText().equals(""))
            pushDuration.setText("3");
        else {
            Integer value = Integer.parseInt(pushDuration.getText());
            if (value < 3 || value > 128) pushDuration.setText("3");
        }
        te.setPressDelay(Integer.parseInt(pushDuration.getText()));
        NWFancyButton.send(te);
    }

    @Subscribe
    public void onValueChanged(ComponentEvent.ValueChange<UITextField, String> event)
    {
    }
}
