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
        setObject("immersiveengineering:biodiesel", new Fuel("immersiveengineering:biodiesel", 8, 815, 0.6F, 0F));
        setObject("immersiveengineering:ethanol", new Fuel("immersiveengineering:ethanol", 10, 789, 0.75F, 0F));
        setObject("immersiveengineering:plantoil", new Fuel("immersiveengineering:plantoil", 6, 840, 0.2F, 0F));
        setObject("immersiveengineering:creosote", new Fuel("immersiveengineering:creosote", 2, 1100, 0.25F, 0.4F));
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        fuels = getFuelsInternal();
    }

    private Map<Fluid, Fuel> getFuelsInternal() {
        return getSubValues().stream().map(s -> getObject(s, () -> new Fuel(s))).filter(fuel -> fuel.getFluid() != Fluids.EMPTY).collect(Collectors.toMap(Fuel::getFluid, fuel -> fuel));
    }

    public Map<Fluid, Fuel> getFuels() {
        return fuels;
    }

}
