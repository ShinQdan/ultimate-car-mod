package de.maxhenkel.car.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.car.entity.car.base.EntityCarInventoryBase;
import de.maxhenkel.corelib.inventory.ScreenBase;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiCarInventory extends ScreenBase<ContainerCarInventory> {

    private static final ResourceLocation GUI_TEXTURE_3 = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static final ResourceLocation GUI_TEXTURE_6 = new ResourceLocation("textures/gui/container/generic_54.png");

    private EntityCarInventoryBase car;
    private Inventory playerInventory;

    public GuiCarInventory(ContainerCarInventory carInventory, Inventory playerInventory, Component title) {
        super(carInventory.getRows() == 3 ? GUI_TEXTURE_3 : GUI_TEXTURE_6, carInventory, playerInventory, title);
        this.car = carInventory.getCar();
        this.playerInventory = playerInventory;
        imageWidth = 176;
        imageHeight = carInventory.getRows() == 3 ? 166 : 222;
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        super.renderLabels(matrixStack, mouseX, mouseY);
        font.draw(matrixStack, car.getDisplayName().getVisualOrderText(), 8, 6, FONT_COLOR);
        font.draw(matrixStack, playerInventory.getDisplayName().getVisualOrderText(), 8, imageHeight - 96 + 3, FONT_COLOR);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
