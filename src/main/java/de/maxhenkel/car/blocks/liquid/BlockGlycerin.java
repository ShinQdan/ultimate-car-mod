package de.maxhenkel.car.blocks.liquid;

import de.maxhenkel.car.fluids.ModFluids;
import net.minecraftforge.fluids.Fluid;

public class BlockGlycerin extends FluidBlockBase {

	public BlockGlycerin() {
		super("canola_oil");
	}

	@Override
	public Fluid getFluid() {
		return ModFluids.BIO_DIESEL;
	}

}
