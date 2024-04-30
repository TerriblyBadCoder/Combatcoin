package net.atired.coinmod.event;

import net.atired.coinmod.client.render.ChainedSawHudOverlay;
import net.atired.coinmod.CombatCoin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = CombatCoin.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("coin_heatmeter", ChainedSawHudOverlay.HUD_CHARGE);
        }
    }
}
