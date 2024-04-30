package net.atired.coinmod.block;

import net.atired.coinmod.block.custom.BlockOfNejelezo;
import net.atired.coinmod.CombatCoin;
import net.atired.coinmod.Items.ItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry {

        public static final DeferredRegister<Block>
                BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CombatCoin.MODID);
        private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
            RegistryObject<T> toReturn = BLOCKS.register(name, block);
            registerBlockItem(name, toReturn);
            return toReturn;
        }
        public static final RegistryObject<Block> BLOCK_OF_NEJELEZO = registerBlock("block_of_nejelezo",
            () -> new BlockOfNejelezo(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK)));
        private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block)
        {
            return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(),new Item.Properties()));
        }
        public static void register(IEventBus eventBus)
        {
            BLOCKS.register(eventBus);
        }

}
