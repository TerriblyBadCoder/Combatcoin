package net.atired.coinmod.datagen;

import net.atired.coinmod.CombatCoin;

import net.atired.coinmod.Items.ItemRegistry;
import net.atired.coinmod.event.loot.AddItemModifier;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider
{
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, CombatCoin.MODID);
    }

    @Override
    protected void start()
        {

            add("nejelezo_template_from_chest", new AddItemModifier(new LootItemCondition[]
                    { new LootTableIdCondition.Builder(new ResourceLocation("chests/nether_bridge")).build(), LootItemRandomChanceCondition.randomChance(1f).build()}, ItemRegistry.NEJELEZO_UPGRADE_SMITHING_TEMPLATE.get()));


        }

}
