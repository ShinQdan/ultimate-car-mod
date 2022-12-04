package de.maxhenkel.car.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.maxhenkel.car.Main;
import de.maxhenkel.car.ModItemGroups;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemBattery extends Item {

    public ItemBattery() {
        super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_CAR).durability(500));
        setRegistryName(new ResourceLocation(Main.MODID, "battery"));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("tooltip.battery_energy", new TextComponent(String.valueOf(getEnergy(stack))).withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("tooltip.battery").withStyle(ChatFormatting.GRAY));

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return super.useOn(context);
    }

    public int getEnergy(ItemStack stack) {
        return getMaxDamage(stack) - getDamage(stack);
    }

    public void setEnergy(ItemStack stack, int energy) {
        setDamage(stack, Math.max(getMaxDamage(stack) - energy, 0));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                if (cap == CapabilityEnergy.ENERGY) {
                    return LazyOptional.of(() -> new BatteryEnergyStorage(stack)).cast();
                }
                return LazyOptional.empty();
            }
        };
    }

    public class BatteryEnergyStorage implements IEnergyStorage {

        private ItemStack stack;

        public BatteryEnergyStorage(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int amount = Math.min(maxReceive, getMaxDamage(stack) - getEnergy(stack));
            if (!simulate) {
                setEnergy(stack, getEnergy(stack) + amount);
            }
            return amount;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public int getEnergyStored() {
            return getEnergy(stack);
        }

        @Override
        public int getMaxEnergyStored() {
            return getMaxDamage(stack);
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return true;
        }
    }

}
