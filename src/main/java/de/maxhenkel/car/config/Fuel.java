package de.maxhenkel.car.config;

import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.conversion.SpecDoubleInRange;
import com.electronwill.nightconfig.core.conversion.SpecIntInRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

public class Fuel {

    private transient Fluid fluid;

    @Path("usage")
    @SpecIntInRange(min = 0, max = Short.MAX_VALUE)
    private int usage;

    @Path("density")
    @SpecIntInRange(min = 0, max = Short.MAX_VALUE)
    private int density;

    @Path("maxTorqueSpeed")
    @SpecDoubleInRange(min = 0.1, max = 0.9)
    private double maxTorqueSpeed;

    @Path("reduction")
    @SpecDoubleInRange(min = 0, max = 0.58)
    private double reduction;

    public Fuel(String fluid, int usage, int density, float maxTorqueSpeed, float reduction) {
        this(fluid);
        this.usage = usage;
        this.density = density;
        this.maxTorqueSpeed = maxTorqueSpeed;
        this.reduction = reduction;
    }

    public Fuel(Fluid fluid, int efficiency, int density, float maxTorqueSpeed, float reduction) {
        this.fluid = fluid;
        this.usage = efficiency;
        this.density = density;
        this.maxTorqueSpeed = maxTorqueSpeed;
        this.reduction = reduction;
    }

    public Fuel(String fluid) {
        this.fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluid));
    }

    public Fluid getFluid() {
        return fluid;
    }

    public int getUsage() {
        return usage;
    }

    public int getDensity(){
        return density;
    }

    public double getMaxTorqueSpeed(){
        return maxTorqueSpeed;
    }

    public double getReduction(){
        return reduction;
    }
}
