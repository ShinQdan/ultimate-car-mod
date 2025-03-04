package de.maxhenkel.car.events;

import de.maxhenkel.car.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockEvents {

    private static ResourceLocation GRASS_LOOT_TABLE = new ResourceLocation(Main.MODID, "blocks/grass");

    @SubscribeEvent
    public void breakEvent(BlockEvent.BreakEvent event) {
        if (!event.getState().getBlock().equals(Blocks.GRASS)) {
            return;
        }

        if (event.getPlayer().level instanceof ServerLevel level) {
            LootContext.Builder builder = new LootContext.Builder(level)
                    .withRandom(level.random)
                    .withParameter(LootContextParams.ORIGIN, new Vec3(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()))
                    .withParameter(LootContextParams.TOOL, event.getPlayer().getMainHandItem())
                    .withParameter(LootContextParams.THIS_ENTITY, event.getPlayer())
                    .withParameter(LootContextParams.BLOCK_STATE, event.getState());

            LootContext lootContext = builder.create(LootContextParamSets.BLOCK);

            LootTable lootTable = level.getServer().getLootTables().get(GRASS_LOOT_TABLE);

            lootTable.getRandomItems(lootContext).forEach((stack) -> Block.popResource(level, event.getPos(), stack));
        }
    }

}
