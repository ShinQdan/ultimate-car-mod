package de.maxhenkel.car;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import com.google.common.collect.ImmutableSet;

import de.maxhenkel.car.blocks.ModBlocks;
import de.maxhenkel.car.blocks.tileentity.TileEntityCarWorkshop;
import de.maxhenkel.car.blocks.tileentity.TileEntityGasStation;
import de.maxhenkel.car.blocks.tileentity.TileEntitySign;
import de.maxhenkel.car.blocks.tileentity.render.TileEntitySpecialRendererSign;
import de.maxhenkel.car.blocks.tileentity.render.TileentitySpecialRendererGasStation;
import de.maxhenkel.car.commands.CommandCarDemo;
import de.maxhenkel.car.config.ClientConfig;
import de.maxhenkel.car.config.FuelConfig;
import de.maxhenkel.car.config.ServerConfig;
import de.maxhenkel.car.entity.car.base.EntityGenericCar;
import de.maxhenkel.car.entity.model.GenericCarModel;
import de.maxhenkel.car.events.BlockEvents;
import de.maxhenkel.car.events.CapabilityEvents;
import de.maxhenkel.car.events.KeyEvents;
import de.maxhenkel.car.events.PlayerEvents;
import de.maxhenkel.car.events.RenderEvents;
import de.maxhenkel.car.events.SoundEvents;
import de.maxhenkel.car.gui.ContainerCar;
import de.maxhenkel.car.gui.ContainerCarInventory;
import de.maxhenkel.car.gui.ContainerCarWorkshopCrafting;
import de.maxhenkel.car.gui.ContainerCarWorkshopRepair;
import de.maxhenkel.car.gui.ContainerFactoryTileEntity;
import de.maxhenkel.car.gui.ContainerGasStation;
import de.maxhenkel.car.gui.ContainerGasStationAdmin;
import de.maxhenkel.car.gui.ContainerLicensePlate;
import de.maxhenkel.car.gui.ContainerPainter;
import de.maxhenkel.car.gui.ContainerSign;
import de.maxhenkel.car.gui.GuiCar;
import de.maxhenkel.car.gui.GuiCarInventory;
import de.maxhenkel.car.gui.GuiCarWorkshopCrafting;
import de.maxhenkel.car.gui.GuiCarWorkshopRepair;
import de.maxhenkel.car.gui.GuiGasStation;
import de.maxhenkel.car.gui.GuiGasStationAdmin;
import de.maxhenkel.car.gui.GuiLicensePlate;
import de.maxhenkel.car.gui.GuiPainter;
import de.maxhenkel.car.gui.GuiSign;
import de.maxhenkel.car.items.ItemLicensePlate;
import de.maxhenkel.car.items.ModItems;
import de.maxhenkel.car.net.MessageCarGui;
import de.maxhenkel.car.net.MessageCarHorn;
import de.maxhenkel.car.net.MessageCenterCar;
import de.maxhenkel.car.net.MessageCenterCarClient;
import de.maxhenkel.car.net.MessageControlCar;
import de.maxhenkel.car.net.MessageCrash;
import de.maxhenkel.car.net.MessageEditLicensePlate;
import de.maxhenkel.car.net.MessageEditSign;
import de.maxhenkel.car.net.MessageGasStationAdminAmount;
import de.maxhenkel.car.net.MessageOpenCarWorkshopGui;
import de.maxhenkel.car.net.MessageRepairCar;
import de.maxhenkel.car.net.MessageSpawnCar;
import de.maxhenkel.car.net.MessageStartFuel;
import de.maxhenkel.car.net.MessageStarting;
import de.maxhenkel.car.net.MessageSyncTileEntity;
import de.maxhenkel.car.recipes.KeyRecipe;
import de.maxhenkel.car.sounds.ModSounds;
import de.maxhenkel.car.villagers.VillagerEvents;
import de.maxhenkel.corelib.ClientRegistry;
import de.maxhenkel.corelib.CommonRegistry;
import de.maxhenkel.corelib.block.IItemBlock;
import de.maxhenkel.corelib.config.DynamicConfig;
import de.maxhenkel.corelib.dataserializers.DataSerializerItemList;
import de.maxhenkel.tools.EntityTools;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DataSerializerEntry;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "car";

    public static final Logger LOGGER = LogManager.getLogger(Main.MODID);

    public static SimpleChannel SIMPLE_CHANNEL;

    public static EntityType<EntityGenericCar> CAR_ENTITY_TYPE;

    public static LootItemFunctionType COPY_FLUID;

    public static PoiType POINT_OF_INTEREST_TYPE_GAS_STATION_ATTENDANT;
    public static VillagerProfession VILLAGER_PROFESSION_GAS_STATION_ATTENDANT;

    public static ServerConfig SERVER_CONFIG;
    public static FuelConfig FUEL_CONFIG;
    public static ClientConfig CLIENT_CONFIG;

    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(SoundEvent.class, this::registerSounds);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(EntityType.class, this::registerEntities);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(MenuType.class, this::registerContainers);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(BlockEntityType.class, this::registerTileEntities);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(RecipeSerializer.class, this::registerRecipes);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(DataSerializerEntry.class, this::registerSerializers);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(PoiType.class, this::registerPointsOfInterest);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(VillagerProfession.class, this::registerVillagerProfessions);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        SERVER_CONFIG = CommonRegistry.registerConfig(ModConfig.Type.SERVER, ServerConfig.class, true);
        FUEL_CONFIG = CommonRegistry.registerDynamicConfig(DynamicConfig.DynamicConfigType.SERVER, Main.MODID, "fuel", FuelConfig.class);
        CLIENT_CONFIG = CommonRegistry.registerConfig(ModConfig.Type.CLIENT, ClientConfig.class);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(Main.this::clientSetup));
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandCarDemo.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CapabilityEvents());
        MinecraftForge.EVENT_BUS.register(new BlockEvents());

        MinecraftForge.EVENT_BUS.register(new VillagerEvents());

        SIMPLE_CHANNEL = CommonRegistry.registerChannel(Main.MODID, "default");
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 0, MessageControlCar.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 1, MessageCarGui.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 2, MessageStarting.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 3, MessageCrash.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 4, MessageStartFuel.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 6, MessageSyncTileEntity.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 7, MessageSpawnCar.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 8, MessageOpenCarWorkshopGui.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 9, MessageRepairCar.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 10, MessageCarHorn.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 11, MessageEditSign.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 12, MessageGasStationAdminAmount.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 13, MessageCenterCar.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 14, MessageCenterCarClient.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 15, MessageEditLicensePlate.class);
    }

    public static KeyMapping FORWARD_KEY;
    public static KeyMapping BACK_KEY;
    public static KeyMapping LEFT_KEY;
    public static KeyMapping RIGHT_KEY;

    public static KeyMapping CAR_GUI_KEY;
    public static KeyMapping START_KEY;
    public static KeyMapping HORN_KEY;
    public static KeyMapping CENTER_KEY;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void clientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(GAS_STATION_TILE_ENTITY_TYPE, TileentitySpecialRendererGasStation::new);
        BlockEntityRenderers.register(SIGN_TILE_ENTITY_TYPE, TileEntitySpecialRendererSign::new);

        ClientRegistry.<ContainerCar, GuiCar>registerScreen(Main.CAR_CONTAINER_TYPE, GuiCar::new);
        ClientRegistry.<ContainerCarInventory, GuiCarInventory>registerScreen(Main.CAR_INVENTORY_CONTAINER_TYPE, GuiCarInventory::new);
        ClientRegistry.<ContainerCarWorkshopCrafting, GuiCarWorkshopCrafting>registerScreen(Main.CAR_WORKSHOP_CRAFTING_CONTAINER_TYPE, GuiCarWorkshopCrafting::new);
        ClientRegistry.<ContainerCarWorkshopRepair, GuiCarWorkshopRepair>registerScreen(Main.CAR_WORKSHOP_REPAIR_CONTAINER_TYPE, GuiCarWorkshopRepair::new);
        ClientRegistry.<ContainerGasStation, GuiGasStation>registerScreen(Main.GAS_STATION_CONTAINER_TYPE, GuiGasStation::new);
        ClientRegistry.<ContainerGasStationAdmin, GuiGasStationAdmin>registerScreen(Main.GAS_STATION_ADMIN_CONTAINER_TYPE, GuiGasStationAdmin::new);
        ClientRegistry.<ContainerLicensePlate, GuiLicensePlate>registerScreen(Main.LICENSE_PLATE_CONTAINER_TYPE, GuiLicensePlate::new);
        ClientRegistry.<ContainerPainter, GuiPainter>registerScreen(Main.PAINTER_CONTAINER_TYPE, GuiPainter::new);
        ClientRegistry.<ContainerSign, GuiSign>registerScreen(Main.SIGN_CONTAINER_TYPE, GuiSign::new);

        FORWARD_KEY = ClientRegistry.registerKeyBinding("key.car_forward", "category.car", GLFW.GLFW_KEY_W);
        BACK_KEY = ClientRegistry.registerKeyBinding("key.car_back", "category.car", GLFW.GLFW_KEY_S);
        LEFT_KEY = ClientRegistry.registerKeyBinding("key.car_left", "category.car", GLFW.GLFW_KEY_A);
        RIGHT_KEY = ClientRegistry.registerKeyBinding("key.car_right", "category.car", GLFW.GLFW_KEY_D);
        CAR_GUI_KEY = ClientRegistry.registerKeyBinding("key.car_gui", "category.car", GLFW.GLFW_KEY_I);
        START_KEY = ClientRegistry.registerKeyBinding("key.car_start", "category.car", GLFW.GLFW_KEY_R);
        HORN_KEY = ClientRegistry.registerKeyBinding("key.car_horn", "category.car", GLFW.GLFW_KEY_H);
        CENTER_KEY = ClientRegistry.registerKeyBinding("key.center_car", "category.car", GLFW.GLFW_KEY_SPACE);

        MinecraftForge.EVENT_BUS.register(new RenderEvents());
        MinecraftForge.EVENT_BUS.register(new SoundEvents());
        MinecraftForge.EVENT_BUS.register(new KeyEvents());
        MinecraftForge.EVENT_BUS.register(new PlayerEvents());

        EntityRenderers.register(CAR_ENTITY_TYPE, GenericCarModel::new);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                ModBlocks.getBlocksWithItems().stream().map(IItemBlock::toItem).toArray(Item[]::new)
        );

        event.getRegistry().registerAll(
                ModItems.getAll().toArray(new Item[0])
        );
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                ModBlocks.getAll().toArray(new Block[0])
        );

        if (FMLEnvironment.dist == Dist.CLIENT) {
            for (Block block : ModBlocks.PAINTS) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
            }
            for (Block block : ModBlocks.YELLOW_PAINTS) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
            }
        }
    }

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                ModSounds.getAll().toArray(new SoundEvent[0])
        );
    }

    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        CAR_ENTITY_TYPE = CommonRegistry.registerEntity(Main.MODID, "car", MobCategory.MISC, EntityGenericCar.class, builder -> {
            builder.setTrackingRange(128)
                    .setUpdateInterval(1)
                    .setShouldReceiveVelocityUpdates(true)
                    .sized(1F, 1F)
                    .setCustomClientFactory((spawnEntity, world) -> new EntityGenericCar(world));
        });
        event.getRegistry().register(CAR_ENTITY_TYPE);
    }

    public static MenuType<ContainerCar> CAR_CONTAINER_TYPE;
    public static MenuType<ContainerCarInventory> CAR_INVENTORY_CONTAINER_TYPE;
    public static MenuType<ContainerCarWorkshopCrafting> CAR_WORKSHOP_CRAFTING_CONTAINER_TYPE;
    public static MenuType<ContainerCarWorkshopRepair> CAR_WORKSHOP_REPAIR_CONTAINER_TYPE;
    public static MenuType<ContainerGasStation> GAS_STATION_CONTAINER_TYPE;
    public static MenuType<ContainerGasStationAdmin> GAS_STATION_ADMIN_CONTAINER_TYPE;
    public static MenuType<ContainerLicensePlate> LICENSE_PLATE_CONTAINER_TYPE;
    public static MenuType<ContainerPainter> PAINTER_CONTAINER_TYPE;
    public static MenuType<ContainerSign> SIGN_CONTAINER_TYPE;

    @SubscribeEvent
    public void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
        CAR_CONTAINER_TYPE = new MenuType<>((IContainerFactory<ContainerCar>) (windowId, inv, data) -> {
            EntityGenericCar car = EntityTools.getCarByUUID(inv.player, data.readUUID());
            if (car == null) {
                return null;
            }
            return new ContainerCar(windowId, car, inv);
        });
        CAR_CONTAINER_TYPE.setRegistryName(new ResourceLocation(Main.MODID, "car"));
        event.getRegistry().register(CAR_CONTAINER_TYPE);

        CAR_INVENTORY_CONTAINER_TYPE = new MenuType<>((IContainerFactory<ContainerCarInventory>) (windowId, inv, data) -> {
            EntityGenericCar car = EntityTools.getCarByUUID(inv.player, data.readUUID());
            if (car == null) {
                return null;
            }
            return new ContainerCarInventory(windowId, car, inv);
        });
        CAR_INVENTORY_CONTAINER_TYPE.setRegistryName(new ResourceLocation(Main.MODID, "car_inventory"));
        event.getRegistry().register(CAR_INVENTORY_CONTAINER_TYPE);

        CAR_WORKSHOP_CRAFTING_CONTAINER_TYPE = new MenuType<>(new ContainerFactoryTileEntity((ContainerFactoryTileEntity.ContainerCreator<ContainerCarWorkshopCrafting, TileEntityCarWorkshop>) ContainerCarWorkshopCrafting::new));
        CAR_WORKSHOP_CRAFTING_CONTAINER_TYPE.setRegistryName(new ResourceLocation(Main.MODID, "car_workshop_crafting"));
        event.getRegistry().register(CAR_WORKSHOP_CRAFTING_CONTAINER_TYPE);

        CAR_WORKSHOP_REPAIR_CONTAINER_TYPE = new MenuType<>(new ContainerFactoryTileEntity((ContainerFactoryTileEntity.ContainerCreator<ContainerCarWorkshopRepair, TileEntityCarWorkshop>) ContainerCarWorkshopRepair::new));
        CAR_WORKSHOP_REPAIR_CONTAINER_TYPE.setRegistryName(new ResourceLocation(Main.MODID, "car_workshop_repair"));
        event.getRegistry().register(CAR_WORKSHOP_REPAIR_CONTAINER_TYPE);

        GAS_STATION_CONTAINER_TYPE = new MenuType<>(new ContainerFactoryTileEntity((ContainerFactoryTileEntity.ContainerCreator<ContainerGasStation, TileEntityGasStation>) ContainerGasStation::new));
        GAS_STATION_CONTAINER_TYPE.setRegistryName(new ResourceLocation(Main.MODID, "fuel_station"));
        event.getRegistry().register(GAS_STATION_CONTAINER_TYPE);

        GAS_STATION_ADMIN_CONTAINER_TYPE = new MenuType<>(new ContainerFactoryTileEntity((ContainerFactoryTileEntity.ContainerCreator<ContainerGasStationAdmin, TileEntityGasStation>) ContainerGasStationAdmin::new));
        GAS_STATION_ADMIN_CONTAINER_TYPE.setRegistryName(new ResourceLocation(Main.MODID, "fuel_station_admin"));
        event.getRegistry().register(GAS_STATION_ADMIN_CONTAINER_TYPE);

        LICENSE_PLATE_CONTAINER_TYPE = new MenuType<>((id, inv) -> {
            ItemStack licensePlate = null;
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = inv.player.getItemInHand(hand);
                if (stack.getItem() instanceof ItemLicensePlate) {
                    licensePlate = stack;
                    break;
                }
            }
            if (licensePlate != null) {
                return new ContainerLicensePlate(id, licensePlate);
            }
            return null;
        });
        LICENSE_PLATE_CONTAINER_TYPE.setRegistryName(new ResourceLocation(Main.MODID, "license_plate"));
        event.getRegistry().register(LICENSE_PLATE_CONTAINER_TYPE);

        PAINTER_CONTAINER_TYPE = new MenuType<>((IContainerFactory<ContainerPainter>) (windowId, inv, data) -> new ContainerPainter(windowId, inv, data.readBoolean()));
        PAINTER_CONTAINER_TYPE.setRegistryName(new ResourceLocation(Main.MODID, "painter"));
        event.getRegistry().register(PAINTER_CONTAINER_TYPE);

        SIGN_CONTAINER_TYPE = new MenuType<>(new ContainerFactoryTileEntity((ContainerFactoryTileEntity.ContainerCreator<ContainerSign, TileEntitySign>) ContainerSign::new));
        SIGN_CONTAINER_TYPE.setRegistryName(new ResourceLocation(Main.MODID, "sign"));
        event.getRegistry().register(SIGN_CONTAINER_TYPE);
    }

    public static BlockEntityType<TileEntityCarWorkshop> CAR_WORKSHOP_TILE_ENTITY_TYPE;
    public static BlockEntityType<TileEntitySign> SIGN_TILE_ENTITY_TYPE;
    public static BlockEntityType<TileEntityGasStation> GAS_STATION_TILE_ENTITY_TYPE;

    @SubscribeEvent
    public void registerTileEntities(RegistryEvent.Register<BlockEntityType<?>> event) {
        CAR_WORKSHOP_TILE_ENTITY_TYPE = BlockEntityType.Builder.of(TileEntityCarWorkshop::new, ModBlocks.CAR_WORKSHOP).build(null);
        CAR_WORKSHOP_TILE_ENTITY_TYPE.setRegistryName(new ResourceLocation(MODID, "car_workshop"));
        event.getRegistry().register(CAR_WORKSHOP_TILE_ENTITY_TYPE);

        SIGN_TILE_ENTITY_TYPE = BlockEntityType.Builder.of(TileEntitySign::new, ModBlocks.SIGN).build(null);
        SIGN_TILE_ENTITY_TYPE.setRegistryName(new ResourceLocation(MODID, "sign"));
        event.getRegistry().register(SIGN_TILE_ENTITY_TYPE);

        GAS_STATION_TILE_ENTITY_TYPE = BlockEntityType.Builder.of(TileEntityGasStation::new, ModBlocks.GAS_STATION).build(null);
        GAS_STATION_TILE_ENTITY_TYPE.setRegistryName(new ResourceLocation(MODID, "gas_station"));
        event.getRegistry().register(GAS_STATION_TILE_ENTITY_TYPE);
    }

    public static RecipeSerializer<KeyRecipe> CRAFTING_SPECIAL_KEY;

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<RecipeSerializer<?>> event) {
        CRAFTING_SPECIAL_KEY = new SimpleRecipeSerializer<>(KeyRecipe::new);
        CRAFTING_SPECIAL_KEY.setRegistryName(new ResourceLocation(MODID, "crafting_special_key"));
        event.getRegistry().register(CRAFTING_SPECIAL_KEY);
    }

    @SubscribeEvent
    public void registerSerializers(RegistryEvent.Register<DataSerializerEntry> event) {
        DataSerializerItemList.register(event, new ResourceLocation(MODID, "serializer_item_list"));
    }

    @SubscribeEvent
    public void registerPointsOfInterest(RegistryEvent.Register<PoiType> event) {
        POINT_OF_INTEREST_TYPE_GAS_STATION_ATTENDANT = new PoiType("gas_station_attendant", ImmutableSet.copyOf(ModBlocks.GAS_STATION.getStateDefinition().getPossibleStates()), 1, 1);
        POINT_OF_INTEREST_TYPE_GAS_STATION_ATTENDANT.setRegistryName(Main.MODID, "gas_station_attendant");
        event.getRegistry().register(POINT_OF_INTEREST_TYPE_GAS_STATION_ATTENDANT);
    }

    @SubscribeEvent
    public void registerVillagerProfessions(RegistryEvent.Register<VillagerProfession> event) {
        VILLAGER_PROFESSION_GAS_STATION_ATTENDANT = new VillagerProfession("gas_station_attendant", POINT_OF_INTEREST_TYPE_GAS_STATION_ATTENDANT, ImmutableSet.of(), ImmutableSet.of(), ModSounds.GAS_STATION_ATTENDANT);
        VILLAGER_PROFESSION_GAS_STATION_ATTENDANT.setRegistryName(Main.MODID, "gas_station_attendant");
        event.getRegistry().register(VILLAGER_PROFESSION_GAS_STATION_ATTENDANT);
    }

}
