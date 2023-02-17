package de.maxhenkel.car.blocks;

import de.maxhenkel.car.Main;
import de.maxhenkel.car.blocks.BlockPaint.EnumPaintType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);

    public static final RegistryObject<BlockAsphalt> ASPHALT = BLOCK_REGISTER.register("asphalt", () -> new BlockAsphalt());
    public static final RegistryObject<BlockAsphaltSlope> ASPHALT_SLOPE = BLOCK_REGISTER.register("asphalt_slope", () -> new BlockAsphaltSlope());
    public static final RegistryObject<BlockAsphaltSlopeFlat> ASPHALT_SLOPE_FLAT_UPPER = BLOCK_REGISTER.register("asphalt_slope_flat_upper", () -> new BlockAsphaltSlopeFlat(true));
    public static final RegistryObject<BlockAsphaltSlopeFlat> ASPHALT_SLOPE_FLAT_LOWER = BLOCK_REGISTER.register("asphalt_slope_flat_lower", () -> new BlockAsphaltSlopeFlat(false));
    public static final RegistryObject<BlockAsphaltSlab> ASPHALT_SLAB = BLOCK_REGISTER.register("asphalt_slab", () -> new BlockAsphaltSlab());
    public static final RegistryObject<BlockGasStation> GAS_STATION = BLOCK_REGISTER.register("gas_station", () -> new BlockGasStation());
    public static final RegistryObject<BlockGasStationTop> GAS_STATION_TOP = BLOCK_REGISTER.register("gas_station_top", () -> new BlockGasStationTop());
    public static final RegistryObject<BlockGuardRail> GUARD_RAIL = BLOCK_REGISTER.register("guard_rail", () -> new BlockGuardRail());
    public static final RegistryObject<BlockCarWorkshop> CAR_WORKSHOP = BLOCK_REGISTER.register("car_workshop", () -> new BlockCarWorkshop());
    public static final RegistryObject<BlockCarWorkshopOutter> CAR_WORKSHOP_OUTTER = BLOCK_REGISTER.register("car_workshop_outter", () -> new BlockCarWorkshopOutter());
    public static final RegistryObject<BlockSign> SIGN = BLOCK_REGISTER.register("sign", () -> new BlockSign());
    public static final RegistryObject<BlockSignPost> SIGN_POST = BLOCK_REGISTER.register("sign_post", () -> new BlockSignPost());
    public static final RegistryObject<BlockCarPressurePlate> CAR_PRESSURE_PLATE = BLOCK_REGISTER.register("car_pressure_plate", () -> new BlockCarPressurePlate());

    public static final RegistryObject<BlockPaint>[] PAINTS;
    public static final RegistryObject<BlockPaint>[] YELLOW_PAINTS;

    static {
        PAINTS = new RegistryObject[EnumPaintType.values().length];
        for (int i = 0; i < PAINTS.length; i++) {
            int paintIndex = i;
            PAINTS[i] = BLOCK_REGISTER.register(EnumPaintType.values()[i].getPaintName(), () -> new BlockPaint(EnumPaintType.values()[paintIndex], false));
        }

        YELLOW_PAINTS = new RegistryObject[EnumPaintType.values().length];
        for (int i = 0; i < YELLOW_PAINTS.length; i++) {
            int paintIndex = i;
            YELLOW_PAINTS[i] = BLOCK_REGISTER.register(EnumPaintType.values()[i].getPaintName() + "_yellow", () -> new BlockPaint(EnumPaintType.values()[paintIndex], true));
        }
    }

    public static void init() {
        BLOCK_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
