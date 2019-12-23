package de.maxhenkel.car.gui;

import de.maxhenkel.car.Config;
import de.maxhenkel.car.entity.car.base.EntityCarDamageBase;
import de.maxhenkel.car.items.ModItems;
import de.maxhenkel.car.sounds.ModSounds;
import de.maxhenkel.tools.ItemTools;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class SlotRepairKit extends Slot {

    private EntityCarDamageBase car;
    private PlayerEntity player;

    public SlotRepairKit(EntityCarDamageBase car, int index, int xPosition, int yPosition, PlayerEntity player) {
        super(new Inventory(1), index, xPosition, yPosition);
        this.car = car;
        this.player = player;
    }

    @Override
    public void putStack(ItemStack stack) {
        if (stack == null) {
            return;
        }

        if (!stack.getItem().equals(ModItems.REPAIR_KIT)) {
            return;
        }

        if (car.getDamage() >= 90) {

            ItemTools.decrItemStack(stack, null);

            float damage = car.getDamage() - Config.repairKitRepairAmount.get().floatValue();
            if (damage >= 0) {
                car.setDamage(damage);
            }
            ModSounds.playSound(ModSounds.RATCHET, car.world, car.getPosition(), null, SoundCategory.BLOCKS);
        }

        if (!player.inventory.addItemStackToInventory(stack)) {
            InventoryHelper.spawnItemStack(car.world, car.func_226277_ct_(), car.func_226278_cu_(), car.func_226281_cx_(), stack);
        }
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem().equals(ModItems.REPAIR_KIT);
    }

}
