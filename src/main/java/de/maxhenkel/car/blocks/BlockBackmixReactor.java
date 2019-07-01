package de.maxhenkel.car.blocks;

import de.maxhenkel.car.blocks.tileentity.TileEntityBackmixReactor;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class BlockBackmixReactor extends BlockGui {

    protected BlockBackmixReactor() {
        super("backmix_reactor", Material.IRON, SoundType.METAL, 3F, 3F);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack stack = player.getHeldItem(handIn);

        if (stack != null) {
            FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(null);

            if (fluidStack != null) {
                boolean success = BlockTank.handleEmpty(stack, worldIn, pos, player, handIn);
                if (success) {
                    return true;
                }
            }
            IFluidHandler handler = FluidUtil.getFluidHandler(stack).orElse(null);

            if (handler != null) {
                boolean success1 = BlockTank.handleFill(stack, worldIn, pos, player, handIn);
                if (success1) {
                    return true;
                }
            }

        }

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityBackmixReactor();
    }
}
