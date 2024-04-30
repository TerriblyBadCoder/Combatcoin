package net.atired.coinmod.util;

import net.atired.coinmod.CombatCoin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagRegistry {
    public static class Blocks {
        public static final TagKey<Block> HEATING = tag("heating");
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(CombatCoin.MODID, name));
        }
    }
    public static class Items {

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(CombatCoin.MODID, name));
        }
    }
}
