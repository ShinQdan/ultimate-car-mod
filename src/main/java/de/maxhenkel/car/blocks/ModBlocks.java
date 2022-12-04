package de.maxhenkel.car.blocks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.maxhenkel.car.blocks.BlockPaint.EnumPaintType;
import de.maxhenkel.corelib.block.IItemBlock;
import de.maxhenkel.corelib.reflection.ReflectionUtils;
import de.maxhenkel.tools.NoRegister;
import de.maxhenkel.tools.OnlyBlock;
import net.minecraft.world.level.block.Block;

public class ModBlocks {

    public static final BlockAsphalt ASPHALT = new BlockAsphalt();
    public static final BlockAsphaltSlope ASPHALT_SLOPE = new BlockAsphaltSlope();
    public static final BlockAsphaltSlopeFlat ASPHALT_SLOPE_FLAT_UPPER = new BlockAsphaltSlopeFlat(true);
    public static final BlockAsphaltSlopeFlat ASPHALT_SLOPE_FLAT_LOWER = new BlockAsphaltSlopeFlat(false);
    public static final BlockAsphaltSlab ASPHALT_SLAB = new BlockAsphaltSlab();
    public static final BlockGasStation GAS_STATION = new BlockGasStation();
    @OnlyBlock
    public static final BlockGasStationTop GAS_STATION_TOP = new BlockGasStationTop();
    @OnlyBlock
    public static final BlockGuardRail GUARD_RAIL = new BlockGuardRail();
    public static final BlockCarWorkshop CAR_WORKSHOP = new BlockCarWorkshop();
    public static final BlockCarWorkshopOutter CAR_WORKSHOP_OUTTER = new BlockCarWorkshopOutter();
    public static final BlockSign SIGN = new BlockSign();
    public static final BlockSignPost SIGN_POST = new BlockSignPost();
    public static final BlockCarPressurePlate CAR_PRESSURE_PLATE = new BlockCarPressurePlate();

    public static final BlockPaint[] PAINTS;
    public static final BlockPaint[] YELLOW_PAINTS;

    static {
        PAINTS = new BlockPaint[EnumPaintType.values().length];
        for (int i = 0; i < PAINTS.length; i++) {
            PAINTS[i] = new BlockPaint(EnumPaintType.values()[i], false);
        }

        YELLOW_PAINTS = new BlockPaint[EnumPaintType.values().length];
        for (int i = 0; i < YELLOW_PAINTS.length; i++) {
            YELLOW_PAINTS[i] = new BlockPaint(EnumPaintType.values()[i], true);
        }
    }

    public static List<IItemBlock> getBlocksWithItems() {
        List<IItemBlock> blocks = new ArrayList<>();
        for (Field field : ModBlocks.class.getFields()) {
            if (ReflectionUtils.hasAnnotation(field, NoRegister.class) || ReflectionUtils.hasAnnotation(field, OnlyBlock.class)) {
                continue;
            }
            try {
                Object obj = field.get(null);
                if (obj instanceof IItemBlock) {
                    blocks.add((IItemBlock) obj);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        blocks.addAll(Arrays.asList(PAINTS));
        blocks.addAll(Arrays.asList(YELLOW_PAINTS));

        return blocks;
    }

    public static List<Block> getAll() {
        List<Block> blocks = new ArrayList<>();
        for (Field field : ModBlocks.class.getFields()) {
            if (ReflectionUtils.hasAnnotation(field, NoRegister.class)) {
                continue;
            }
            try {
                Object obj = field.get(null);
                if (obj instanceof Block) {
                    blocks.add((Block) obj);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        blocks.addAll(Arrays.asList(PAINTS));
        blocks.addAll(Arrays.asList(YELLOW_PAINTS));

        return blocks;
    }

}
