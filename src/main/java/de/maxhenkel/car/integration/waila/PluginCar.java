package de.maxhenkel.car.integration.waila;

import de.maxhenkel.car.entity.car.base.EntityGenericCar;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.resources.ResourceLocation;

@WailaPlugin
public class PluginCar implements IWailaPlugin {

    public static final ResourceLocation OBJECT_NAME_TAG = new ResourceLocation("waila", "object_name");
    public static final ResourceLocation CONFIG_SHOW_REGISTRY = new ResourceLocation("waila", "show_registry");
    public static final ResourceLocation REGISTRY_NAME_TAG = new ResourceLocation("waila", "registry_name");

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(HUDHandlerCars.INSTANCE, TooltipPosition.HEAD, EntityGenericCar.class);
        registrar.registerComponentProvider(HUDHandlerCars.INSTANCE, TooltipPosition.BODY, EntityGenericCar.class);
        registrar.registerComponentProvider(HUDHandlerCars.INSTANCE, TooltipPosition.TAIL, EntityGenericCar.class);
    }

}