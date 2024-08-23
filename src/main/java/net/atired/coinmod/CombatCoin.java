package net.atired.coinmod;

import com.mojang.logging.LogUtils;
import net.atired.coinmod.block.BlockRegistry;
import net.atired.coinmod.Items.ItemRegistry;
import net.atired.coinmod.enchantments.EnchantmentRegistry;
import net.atired.coinmod.entity.EntityRegistry;
import net.atired.coinmod.event.ProjectileEvents;

import net.atired.coinmod.event.loot.ModLootModifier;
import net.atired.coinmod.particle.ParticleRegistry;
import net.atired.coinmod.renderer.entity.BustedRenderer;
import net.atired.coinmod.renderer.entity.CoinRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CombatCoin.MODID)
public class CombatCoin
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "coinmod";


    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    public CombatCoin()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemRegistry.register(modEventBus);
        BlockRegistry.register(modEventBus);
        ModLootModifier.register(modEventBus);
        ParticleRegistry.register(modEventBus);
        EnchantmentRegistry.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new ProjectileEvents());
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        EntityRegistry.DEF_REG.register(modEventBus);
        modEventBus.addListener(this::addCreative);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    public static ResourceLocation modPrefix(String path) {
        return new ResourceLocation(CombatCoin.MODID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {


    }


    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.COMBAT)
        {
            event.accept(ItemRegistry.COIN);
            event.accept(ItemRegistry.CHAINEDSAW);
            event.accept(ItemRegistry.BLOCKBUSTER);
        }
        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
        {
            event.accept(BlockRegistry.BLOCK_OF_NEJELEZO);
        }
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS)
        {
            event.accept(ItemRegistry.NEJELEZO_INGOT);
            event.accept(ItemRegistry.NEJELEZO_UPGRADE_SMITHING_TEMPLATE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(EntityRegistry.BUSTED.get(), (context) -> {
                        return new BustedRenderer(context);
                    }
            );
            EntityRenderers.register(EntityRegistry.COIN.get(), (context) -> {
                return new CoinRenderer<>(context, 1.1F, true);
            }
            );
        }
    }
}
