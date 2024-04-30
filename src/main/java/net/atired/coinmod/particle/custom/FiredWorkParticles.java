package net.atired.coinmod.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class FiredWorkParticles extends TextureSheetParticle {
    private static final Vector3f ROTATION_VECTOR = (new Vector3f(0.5F, 0.5F, 0.5F)).normalize();
    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);
    private static final float MAGICAL_X_ROT = 1.0472F;
    private int delay;
    private double doesrotate;
    private final SpriteSet sprite;

    FiredWorkParticles(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, int pDelay, double lengths) {
        super(pLevel, pX, pY, pZ, 0.0, 0.0, 0.0);
        this.quadSize = 0.55F;
        this.delay = pDelay;
        this.lifetime = 10;
        this.gravity = 0.0F;
        this.sprite = spriteSet;
        this.doesrotate = lengths;
        this.xd = 0.0;
        this.yd = 0.01;
        this.zd = 0.0;
        this.alpha = 1F;
        this.rCol = 1F;
        this.gCol = 1F;
        this.bCol = 1F;
    }
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        if(this.doesrotate==0)
        {
            if (this.delay <= 0) {
                if((double) this.age /this.lifetime < 0.5)
                    this.quadSize += this.lifetime/(this.age+0.5F)/60.0F;
                this.alpha = 1.0F - Mth.clamp(((float)this.age + pPartialTicks) / (float)this.lifetime, 0.0F, 1.0F);
                this.renderRotatedParticle(pBuffer, pRenderInfo, pPartialTicks, (p_253347_) -> {
                    p_253347_.mul((new Quaternionf()).rotationYXZ(0F, -1.57F, (float)Math.pow(this.age,0.5)));
                });
                this.renderRotatedParticle(pBuffer, pRenderInfo, pPartialTicks, (p_253346_) -> {
                    p_253346_.mul((new Quaternionf()).rotationYXZ(-3.14F, 1.57F, (float)Math.pow(this.age,0.5)));
                });

            }
        }
        else if (this.delay <= 0)
        {
            super.render(pBuffer,pRenderInfo,pPartialTicks);
            this.alpha = 1.0F - Mth.clamp(((float)this.age + pPartialTicks) / (float)this.lifetime, 0.0F, 1.0F);
        }
    }

    private void renderRotatedParticle(VertexConsumer pConsumer, Camera pCamera, float p_233991_, Consumer<Quaternionf> pQuaternion) {
        Vec3 $$4 = pCamera.getPosition();
        float $$5 = (float)(Mth.lerp((double)p_233991_, this.xo, this.x) - $$4.x());
        float $$6 = (float)(Mth.lerp((double)p_233991_, this.yo, this.y) - $$4.y());
        float $$7 = (float)(Mth.lerp((double)p_233991_, this.zo, this.z) - $$4.z());
        Quaternionf $$8 = (new Quaternionf()).setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
        pQuaternion.accept($$8);
        $$8.transform(TRANSFORM_VECTOR);
        Vector3f[] $$9 = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float $$10 = this.getQuadSize(p_233991_);

        int $$13;
        for($$13 = 0; $$13 < 4; ++$$13) {
            Vector3f $$12 = $$9[$$13];
            $$12.rotate($$8);
            $$12.mul($$10);
            $$12.add($$5, $$6, $$7);
        }

        $$13 = this.getLightColor(p_233991_);
        this.makeCornerVertex(pConsumer, $$9[0], this.getU1(), this.getV1(), $$13);
        this.makeCornerVertex(pConsumer, $$9[1], this.getU1(), this.getV0(), $$13);
        this.makeCornerVertex(pConsumer, $$9[2], this.getU0(), this.getV0(), $$13);
        this.makeCornerVertex(pConsumer, $$9[3], this.getU0(), this.getV1(), $$13);
    }

    private void makeCornerVertex(VertexConsumer pConsumer, Vector3f pVertex, float pU, float pV, int pPackedLight) {
        pConsumer.vertex((double)pVertex.x(), (double)pVertex.y(), (double)pVertex.z()).uv(pU, pV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(pPackedLight).endVertex();
    }

    public int getLightColor(float pPartialTick) {
        return 240;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {

        System.out.println("this particle sure does exist " + this.age + " "+ this.doesrotate);
        this.setSpriteFromAge(this.sprite);
        if((double) this.age /this.lifetime < 0.5)
            this.quadSize += this.lifetime/(this.age+0.5F)/60.0F;
        if (this.delay > 0) {
            --this.delay;
        } else {
            super.tick();
        }
        this.gCol -= this.age/150.0F;
        this.bCol -= this.age/150.0F;

    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            net.atired.coinmod.particle.custom.FiredWorkParticles $$8 = new net.atired.coinmod.particle.custom.FiredWorkParticles(pLevel, pX, pY, pZ, this.sprite,1,pXSpeed+pYSpeed+pZSpeed);
            $$8.setSpriteFromAge(this.sprite);
            $$8.setAlpha(1.0F);
            return $$8;
        }
    }
}