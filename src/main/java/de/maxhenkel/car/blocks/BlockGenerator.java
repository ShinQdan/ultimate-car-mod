package de.maxhenkel.car.blocks;

import de.maxhenkel.car.blocks.tileentity.TileEntityGenerator;
import de.maxhenkel.car.gui.ContainerGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockGenerator extends BlockGui<TileEntityGenerator> {

    protected BlockGenerator() {
        super("generator", Material.IRON, SoundType.STONE, 3F, 3F);
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

    @Override
    public void openGui(BlockState state, World worldIn, BlockPos pos, ServerPlayerEntity player, Hand handIn, TileEntityGenerator tileEntity) {
        NetworkHooks.openGui(player, new INamedContainerProvider() {

            @Nullable
            @Override
            public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new ContainerGenerator(id, tileEntity, playerInventory, tileEntity.FIELDS);
            }

            @Override
            public ITextComponent getDisplayName() {
                return tileEntity.getDisplayName();
            }
        });
    }


    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(0D, 0D, 0D, 16D, 14D, 16D);
    }

    @Override
    public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face) {
        return false;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityGenerator();
    }
}
