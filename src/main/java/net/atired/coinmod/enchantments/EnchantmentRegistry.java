package net.atired.coinmod.enchantments;

import net.atired.coinmod.CombatCoin;
import net.atired.coinmod.Items.custom.BlockBusterItem;
import net.atired.coinmod.Items.custom.ChainedSawItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentRegistry {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CombatCoin.MODID);
    public static final EnchantmentCategory BUSTER = EnchantmentCategory.create("buster", item -> item instanceof BlockBusterItem);
    public static final EnchantmentCategory CHAINEDSAW = EnchantmentCategory.create("saw", item -> item instanceof ChainedSawItem);
    public static RegistryObject<Enchantment> GRAPESHOT = ENCHANTMENTS.register("grapeshot",
            () -> new GrapeshotEnchantment(Enchantment.Rarity.UNCOMMON,
                    BUSTER,EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND));
    public static RegistryObject<Enchantment> RECOIL = ENCHANTMENTS.register("recoil",
            () -> new RecoilEnchantment(Enchantment.Rarity.COMMON,
                    BUSTER,EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND));
    public static RegistryObject<Enchantment> SNOWBALLING = ENCHANTMENTS.register("snowballing",
            () -> new SnowballingEnchantment(Enchantment.Rarity.COMMON,
                    BUSTER,EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND));
    public static RegistryObject<Enchantment> OVERHEAT = ENCHANTMENTS.register("overheat",
            () -> new OverHeatEnchantment(Enchantment.Rarity.COMMON,
                    CHAINEDSAW,EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND));
    public static RegistryObject<Enchantment> FIREDWORKS = ENCHANTMENTS.register("firedworks",
            () -> new FiredWorksEnchantment(Enchantment.Rarity.RARE,
                    CHAINEDSAW,EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND));
    public static void register(IEventBus eventBus)
    {
        ENCHANTMENTS.register(eventBus);
    }
}
