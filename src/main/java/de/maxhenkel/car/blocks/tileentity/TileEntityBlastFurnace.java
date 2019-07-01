package de.maxhenkel.car.blocks.tileentity;

import de.maxhenkel.car.Config;
import de.maxhenkel.car.Main;
import de.maxhenkel.car.blocks.BlockGui;
import de.maxhenkel.car.blocks.ModBlocks;
import de.maxhenkel.car.fluids.ModFluids;
import de.maxhenkel.tools.ItemTools;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.Fluid;

public class TileEntityBlastFurnace extends TileEntityEnergyFluidProducer {

    public TileEntityBlastFurnace() {
        super(Main.BLAST_FURNACE_TILE_ENTITY_TYPE);
        this.maxStorage = Config.blastFurnaceEnergyStorage;
        this.storedEnergy = 0;
        this.timeToGenerate = 0;
        this.generatingTime = Config.blastFurnaceGeneratingTime;
        this.maxMillibuckets = Config.blastFurnaceFluidStorage;
        this.currentMillibuckets = 0;
        this.energyUsage = Config.blastFurnaceEnergyUsage;
        this.millibucketsPerUse = Config.blastFurnaceFluidGeneration;
    }

    @Override
    public BlockGui getOwnBlock() {
        return ModBlocks.BLAST_FURNACE;
    }

    @Override
    public ItemStack getOutputItem() {
        return new ItemStack(Items.CHARCOAL, 1);
    }

    @Override
    public boolean isValidItem(ItemStack stack) {
        return ItemTools.matchesOredict(stack, "logWood");
    }
/*
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation("tile.blastfurnace.name");
	}*/

    @Override
    public Fluid getProducingFluid() {
        return ModFluids.METHANOL;
    }

}
