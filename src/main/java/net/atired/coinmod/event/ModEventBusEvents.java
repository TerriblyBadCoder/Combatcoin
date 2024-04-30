package net.atired.coinmod.event;

import net.atired.coinmod.CombatCoin;
import net.atired.coinmod.particle.ParticleRegistry;
import net.atired.coinmod.particle.custom.ChainedSawSlashParticles;
import net.atired.coinmod.particle.custom.FiredWorkParticles;
import net.atired.coinmod.particle.custom.RicochotParticles;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CombatCoin.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event){
        event.registerSpriteSet(ParticleRegistry.COIN_TRAIL_PARTICLES.get(), ChainedSawSlashParticles.Provider::new);
        event.registerSpriteSet(ParticleRegistry.RICOCHOT_PARTICLES.get(), RicochotParticles.Provider::new);
        event.registerSpriteSet(ParticleRegistry.FIREDWORK_PARTICLES.get(), FiredWorkParticles.Provider::new);
    }
}
