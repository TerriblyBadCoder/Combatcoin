package net.atired.coinmod.enchantments;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class OverHeatEnchantment extends Enchantment {
    protected OverHeatEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public float getDamageBonus(int level, MobType mobType, ItemStack enchantedItem) {
        if(((CompoundTag)enchantedItem.serializeNBT().get("tag")).getInt("coinmod_heatpower")>40)
            return ((CompoundTag)enchantedItem.serializeNBT().get("tag")).getInt("coinmod_heatpower")*level/50F;
        return 0;
    }

    @Override
    protected boolean checkCompatibility(Enchantment pOther) {
        return (super.checkCompatibility(pOther)&&!(pOther instanceof DamageEnchantment));

    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}
