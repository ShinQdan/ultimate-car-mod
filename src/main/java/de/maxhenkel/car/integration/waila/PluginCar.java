package de.maxhenkel.car.integration.waila;

import de.maxhenkel.car.entity.car.base.EntityGenericCar;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class PluginCar implements IWailaPlugin {

    public static final ResourceLocation OBJECT_NAME_TAG = new ResourceLocation("jade", "object_name");

    @Override
    public void register(IWailaCommonRegistration registration) {
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {

        registration.registerEntityComponent(HUDHandlerCars.INSTANCE, EntityGenericCar.class);
    }

}