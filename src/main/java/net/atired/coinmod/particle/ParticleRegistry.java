package net.atired.coinmod.particle;

import net.atired.coinmod.CombatCoin;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CombatCoin.MODID);
    public static final RegistryObject<SimpleParticleType> COIN_TRAIL_PARTICLES = PARTICLE_TYPES.register("coin_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RICOCHOT_PARTICLES = PARTICLE_TYPES.register("ricochot_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> FIREDWORK_PARTICLES = PARTICLE_TYPES.register("firedwork_particles", () -> new SimpleParticleType(true));
    public static void register(IEventBus eventBus){
        PARTICLE_TYPES.register(eventBus);
    }
}
