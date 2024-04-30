package net.atired.coinmod.Items;

import net.atired.coinmod.CombatCoin;
import net.atired.coinmod.Items.custom.BlockBusterItem;
import net.atired.coinmod.Items.custom.ChainedSawItem;
import net.atired.coinmod.Items.custom.CoinItem;
import net.atired.coinmod.Items.custom.NejelezoSmithingTemplateItem;
import net.atired.coinmod.entity.projectile.CoinEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item>
            ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CombatCoin.MODID);
    public static final RegistryObject<Item> CHAINEDSAW = ITEMS.register("chainedsaw",()->new ChainedSawItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> NEJELEZO_INGOT = ITEMS.register("nejelezo_ingot",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> NEJELEZO_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("nejelezo_upgrade_smithing_template",()->  new NejelezoSmithingTemplateItem());
    public  static final RegistryObject<Item>
            BLOCKBUSTER = ITEMS.register("blockbuster",()->new BlockBusterItem(new Item.Properties().defaultDurability(384)
    ));
    public  static final RegistryObject<Item>
            COIN = ITEMS.register("coin",()->new CoinItem(new Item.Properties(), player -> new CoinEntity(player.level(), player), -20.0F, 0.7F, 0.2F));
    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }
}
