package net.atired.coinmod.block.custom;

import net.atired.coinmod.block.BlockRegistry;
import net.atired.coinmod.util.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;

public class BlockOfNejelezo extends Block {
    public static final IntegerProperty HEAT;
    public BlockOfNejelezo(Properties pProperties) {
        super(pProperties);
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(new Property[]{HEAT});

    }
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
            pLevel.scheduleTick(pCurrentPos, this, 10 + pLevel.getRandom().nextInt(5));
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(heatCheck(pLevel,pPos)!=getDatHeat(pState))
        {
            pLevel.setBlock(pPos, this.getStateForHeat(heatCheck(pLevel,pPos)), 2);
        }

    }
    public BlockState getStateForHeat(int pHeat) {
        return (BlockState)this.defaultBlockState().setValue(this.getHeat(), pHeat);
    }
    protected int heatCheck(BlockGetter pLevel, BlockPos pPos) {
        BlockState state = pLevel.getBlockState(pPos);
        Direction[] var4 = Direction.values();
        int var5 = var4.length;
        int totalcount;
        List<Integer> list = new ArrayList<>();
        list.add(0);
        for(int var6 = 0; var6 < var5; ++var6) {
            Direction direction = var4[var6];
            BlockState blockState = pLevel.getBlockState(pPos.relative(direction));
            Block block = blockState.getBlock();
            if(block == Blocks.LAVA || block.defaultBlockState().getTags().toList().contains(TagRegistry.Blocks.HEATING))
            {
                return 4;
            }
            else if(block == BlockRegistry.BLOCK_OF_NEJELEZO.get())
            {
                BlockOfNejelezo blockOfNejelezo = (BlockOfNejelezo)block;
                BlockOfNejelezo originBlockofNejl = (BlockOfNejelezo)(state.getBlock());
                int heat = blockOfNejelezo.getDatHeat(blockState);
                if(heat!=0 &&heat != originBlockofNejl.getDatHeat(state)&&!list.contains(heat))
                    list.add(heat);
            }
        }
        int max = 0;
        if((long) list.size() >0)
            for(int a:list)
            {
                max = Math.max(max,a);
            }
        if(max>0)
        {
            max-=1;
            return max;
        }
        return 0;
    }
    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        pLevel.scheduleTick(pPos, this, 2 + pLevel.getRandom().nextInt(2));
    }
    protected IntegerProperty getHeat() {
        return HEAT;
    }
    public int getDatHeat(BlockState pState) {
        return (Integer)pState.getValue(this.getHeat());
    }
    static {
        HEAT = IntegerProperty.create("coinmod_heat",0,4);
    }
}
