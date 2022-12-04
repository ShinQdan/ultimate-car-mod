package de.maxhenkel.car.config;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import de.maxhenkel.corelib.config.DynamicConfig;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FuelConfig extends DynamicConfig {

    private Map<Fluid, Fuel> fuels;

    public FuelConfig() {
        fuels = new HashMap<>();
    }

    @Override
    protected void setDefaults() {
        super.setDefaults();
        // addFuel(ModFluids.BIO_DIESEL, 100);
        setObject("immersiveengineering:biodiesel", new Fuel("immersiveengineering:biodiesel", 100));
        setObject("immersiveengineering:ethanol", new Fuel("immersiveengineering:ethanol", 50));
        setObject("immersiveengineering:plantoil", new Fuel("immersiveengineering:plantoil", 70));
        setObject("immersiveengineering:creosote", new Fuel("immersiveengineering:creosote", 25));
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        fuels = getFuelsInternal();
    }

    // private void addFuel(Fluid fluid, int efficiency) {
    //     String name = fluid.getRegistryName().toString();
    //     setObject(name, new Fuel(name, efficiency));
    // }

    private Map<Fluid, Fuel> getFuelsInternal() {
        return getSubValues().stream().map(s -> getObject(s, () -> new Fuel(s))).filter(fuel -> fuel.getFluid() != Fluids.EMPTY).collect(Collectors.toMap(Fuel::getFluid, fuel -> fuel));
    }

    public Map<Fluid, Fuel> getFuels() {
        return fuels;
    }

}
