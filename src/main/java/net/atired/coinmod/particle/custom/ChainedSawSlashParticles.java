package net.atired.coinmod.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChainedSawSlashParticles extends TextureSheetParticle {

    private final SpriteSet sprites;
    protected ChainedSawSlashParticles(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        this.gravity = 0F;
        this.friction = 0F;

        this.sprites = spriteSet;
        this.quadSize *= 8F + (float) Math.random() - 0.5F;
        this.lifetime = 8;
        this.roll =( ((float) Math.random() * ((float) Math.PI * 2F))*0.5F-((float) Math.random() * ((float) Math.PI * 2F))*(float)Math.random())/12;
        this.oRoll = this.roll;
        this.rCol = 0.9f;

        this.gCol = 0.4f+ ((float) Math.random() - 0.5F)/12;
        this.bCol = 0.35f+ ((float) Math.random() - 0.5F)/12;
        this.setSpriteFromAge(spriteSet);

    }

    @Override
    public void tick() {
        super.tick();
            this.setSpriteFromAge(this.sprites);
            this.gCol+=0.01f;
    }


    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new ChainedSawSlashParticles(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}