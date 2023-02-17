package de.maxhenkel.car.config;

import de.maxhenkel.corelib.config.ConfigBase;
import de.maxhenkel.corelib.tag.Tag;
import de.maxhenkel.corelib.tag.TagUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServerConfig extends ConfigBase {

    public final ForgeConfigSpec.IntValue gasStationTransferRate;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> gasStationValidFuels;

    public final ForgeConfigSpec.DoubleValue repairKitRepairAmount;

    public final ForgeConfigSpec.IntValue canisterMaxFuel;

    public final ForgeConfigSpec.DoubleValue carOffroadSpeed;
    public final ForgeConfigSpec.DoubleValue carOnroadSpeed;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> carDriveBlocks;

    public final ForgeConfigSpec.BooleanValue collideWithEntities;
    public final ForgeConfigSpec.BooleanValue damageEntities;
    public final ForgeConfigSpec.BooleanValue hornFlee;
    public final ForgeConfigSpec.BooleanValue useBattery;

    public final ForgeConfigSpec.IntValue tankSmallMaxFuel;
    public final ForgeConfigSpec.IntValue tankMediumMaxFuel;
    public final ForgeConfigSpec.IntValue tankLargeMaxFuel;

    public final ForgeConfigSpec.DoubleValue engine6CylinderFuelEfficiency;
    public final ForgeConfigSpec.DoubleValue engine3CylinderFuelEfficiency;
    public final ForgeConfigSpec.DoubleValue engineTruckFuelEfficiency;

    public final ForgeConfigSpec.DoubleValue engine6CylinderAcceleration;
    public final ForgeConfigSpec.DoubleValue engine3CylinderAcceleration;
    public final ForgeConfigSpec.DoubleValue engineTruckAcceleration;

    public final ForgeConfigSpec.DoubleValue engine6CylinderMaxSpeed;
    public final ForgeConfigSpec.DoubleValue engine3CylinderMaxSpeed;
    public final ForgeConfigSpec.DoubleValue engineTruckMaxSpeed;

    public final ForgeConfigSpec.DoubleValue engine6CylinderMaxReverseSpeed;
    public final ForgeConfigSpec.DoubleValue engine3CylinderMaxReverseSpeed;
    public final ForgeConfigSpec.DoubleValue engineTruckMaxReverseSpeed;

    public final ForgeConfigSpec.DoubleValue bodyBigWoodFuelEfficiency;
    public final ForgeConfigSpec.DoubleValue bodyBigWoodAcceleration;
    public final ForgeConfigSpec.DoubleValue bodyBigWoodMaxSpeed;

    public final ForgeConfigSpec.DoubleValue bodyWoodFuelEfficiency;
    public final ForgeConfigSpec.DoubleValue bodyWoodAcceleration;
    public final ForgeConfigSpec.DoubleValue bodyWoodMaxSpeed;

    public final ForgeConfigSpec.DoubleValue bodySportFuelEfficiency;
    public final ForgeConfigSpec.DoubleValue bodySportAcceleration;
    public final ForgeConfigSpec.DoubleValue bodySportMaxSpeed;

    public final ForgeConfigSpec.DoubleValue bodySUVFuelEfficiency;
    public final ForgeConfigSpec.DoubleValue bodySUVAcceleration;
    public final ForgeConfigSpec.DoubleValue bodySUVMaxSpeed;

    public final ForgeConfigSpec.DoubleValue bodyTransporterFuelEfficiency;
    public final ForgeConfigSpec.DoubleValue bodyTransporterAcceleration;
    public final ForgeConfigSpec.DoubleValue bodyTransporterMaxSpeed;

    public List<Tag<Fluid>> gasStationValidFuelList = new ArrayList<>();
    public List<Tag<Fluid>> generatorValidFuelList = new ArrayList<>();
    public List<Tag<Block>> carDriveBlockList = new ArrayList<>();

    public ServerConfig(ForgeConfigSpec.Builder builder) {
        super(builder);
        gasStationTransferRate = builder.defineInRange("machines.gas_station.transfer_rate", 50, 1, Short.MAX_VALUE);
        gasStationValidFuels = builder.comment("If it starts with '#' it is a tag").defineList("machines.gas_station.valid_fuels", List.of("#car:gas_station", "immersiveengineering:biodiesel", "immersiveengineering:ethanol", "immersiveengineering:plantoil", "immersiveengineering:creosote"), Objects::nonNull);

        repairKitRepairAmount = builder.defineInRange("items.repair_kit.repair_amount", 5F, 0.1F, 100F);

        canisterMaxFuel = builder.defineInRange("items.canister.max_fuel", 1000, 1, 1000);

        collideWithEntities = builder.comment("Whether the cars should collide with other entities (except cars)").define("car.collide_with_entities", false);
        damageEntities = builder.comment("Whether the cars should damage other entities on collision").define("car.damage_entities", true);
        hornFlee = builder.comment("Whether animals flee from the car when the horn is activted").define("car.horn_flee", true);
        useBattery = builder.comment("True if starting the car should use battery").define("car.use_battery", true);

        carOffroadSpeed = builder.comment("The speed modifier for cars on non road blocks").defineInRange("car.offroad_speed_modifier", 0.75D, 0.001D, 10D);
        carOnroadSpeed = builder.comment("The speed modifier for cars on road blocks", "On road blocks are defined in the config section 'road_blocks'").defineInRange("car.onroad_speed_modifier", 1D, 0.001D, 10D);
        carDriveBlocks = builder.comment("If it starts with '#' it is a tag").defineList("car.road_blocks.blocks", Collections.singletonList("#car:drivable_blocks"), Objects::nonNull);

        tankSmallMaxFuel = builder.defineInRange("car.parts.small_tank.max_fuel", 5000, 100, 100_000);
        tankMediumMaxFuel = builder.defineInRange("car.parts.medium_tank.max_fuel", 10000, 100, 100_000);
        tankLargeMaxFuel = builder.defineInRange("car.parts.large_tank.max_fuel", 15000, 100, 100_000);

        engine6CylinderFuelEfficiency = builder.defineInRange("car.parts.engine_6_cylinder.fuel_efficiency", 0.49D, 0.001D, 10D);
        engine3CylinderFuelEfficiency = builder.defineInRange("car.parts.engine_3_cylinder.fuel_efficiency", 0.7D, 0.001D, 10D);
        engineTruckFuelEfficiency = builder.defineInRange("car.parts.engine_truck.fuel_efficiency", 1D, 0.001D, 10D);
        engine6CylinderAcceleration = builder.defineInRange("car.parts.engine_6_cylinder.acceleration", 0.05D, 0.001D, 10D);
        engine3CylinderAcceleration = builder.defineInRange("car.parts.engine_3_cylinder.acceleration", 0.045D, 0.001D, 10D);
        engineTruckAcceleration = builder.defineInRange("car.parts.engine_truck.acceleration", 0.04D, 0.001D, 10D);
        engine6CylinderMaxSpeed = builder.defineInRange("car.parts.engine_6_cylinder.max_speed", 1D, 0.001D, 10D);
        engine3CylinderMaxSpeed = builder.defineInRange("car.parts.engine_3_cylinder.max_speed", 0.8D, 0.001D, 10D);
        engineTruckMaxSpeed = builder.defineInRange("car.parts.engine_truck.max_speed", 0.6D, 0.001D, 10D);
        engine6CylinderMaxReverseSpeed = builder.defineInRange("car.parts.engine_6_cylinder.max_reverse_speed", 0.2D, 0.001D, 10D);
        engine3CylinderMaxReverseSpeed = builder.defineInRange("car.parts.engine_3_cylinder.max_reverse_speed", 0.175D, 0.001D, 10D);
        engineTruckMaxReverseSpeed = builder.defineInRange("car.parts.engine_truck.max_reverse_speed", 0.15D, 0.001D, 10D);

        bodyBigWoodFuelEfficiency = builder.defineInRange("car.parts.body_big_wood.fuel_efficiency", 0.85D, 0.001D, 10D);
        bodyBigWoodAcceleration = builder.defineInRange("car.parts.body_big_wood.acceleration", 1.15D, 0.001D, 10D);
        bodyBigWoodMaxSpeed = builder.defineInRange("car.parts.body_big_wood.max_speed", 1.1D, 0.001D, 10D);
        bodyWoodFuelEfficiency = builder.defineInRange("car.parts.body_wood.fuel_efficiency", 0.9D, 0.001D, 10D);
        bodyWoodAcceleration = builder.defineInRange("car.parts.body_wood.acceleration", 1.2D, 0.001D, 10D);
        bodyWoodMaxSpeed = builder.defineInRange("car.parts.body_wood.max_speed", 1.2D, 0.001D, 10D);
        bodySportFuelEfficiency = builder.defineInRange("car.parts.body_sport.fuel_efficiency", 1.0D, 0.001D, 10D);
        bodySportAcceleration = builder.defineInRange("car.parts.body_sport.acceleration", 1.3D, 0.001D, 10D);
        bodySportMaxSpeed = builder.defineInRange("car.parts.body_sport.max_speed", 1.4D, 0.001D, 10D);
        bodySUVFuelEfficiency = builder.defineInRange("car.parts.body_suv.fuel_efficiency", 0.8D, 0.001D, 10D);
        bodySUVAcceleration = builder.defineInRange("car.parts.body_suv.acceleration", 1.1D, 0.001D, 10D);
        bodySUVMaxSpeed = builder.defineInRange("car.parts.body_suv.max_speed", 0.9D, 0.001D, 10D);
        bodyTransporterFuelEfficiency = builder.defineInRange("car.parts.body_transporter.fuel_efficiency", 0.7D, 0.001D, 10D);
        bodyTransporterAcceleration = builder.defineInRange("car.parts.body_transporter.acceleration", 1D, 0.001D, 10D);
        bodyTransporterMaxSpeed = builder.defineInRange("car.parts.body_transporter.max_speed", 0.8D, 0.001D, 10D);
    }

    @Override
    public void onReload(ModConfigEvent event) {
        super.onReload(event);
        gasStationValidFuelList = gasStationValidFuels.get().stream().map(TagUtils::getFluid).filter(Objects::nonNull).collect(Collectors.toList());
        carDriveBlockList = carDriveBlocks.get().stream().map(TagUtils::getBlock).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
