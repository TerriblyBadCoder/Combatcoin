package net.atired.coinmod.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.atired.coinmod.CombatCoin;
import net.atired.coinmod.entity.projectile.CoinEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;


@OnlyIn(Dist.CLIENT)
public class CoinRenderer<T extends Entity & ItemSupplier> extends EntityRenderer<CoinEntity> {
    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation(CombatCoin.MODID, "textures/particle/trail.png");

    private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
    private final ItemRenderer itemRenderer;
    private final float scale;
    private int time;
    private final boolean fullBright;

    public CoinRenderer(EntityRendererProvider.Context pContext, float pScale, boolean pFullBright) {
        super(pContext);
        this.itemRenderer = pContext.getItemRenderer();
        this.scale = pScale;
        this.fullBright = pFullBright;
    }

    public CoinRenderer(EntityRendererProvider.Context pContext) {
        this(pContext, 1.0F, false);
    }

    protected int getBlockLightLevel(CoinEntity pEntity, BlockPos pPos) {
        return 15;
    }

    public void render(CoinEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(pEntity) < 12.25)) {
            pMatrixStack.pushPose();

            if(pEntity.isGonnaBeDiscarded())
            {
                pMatrixStack.scale(0F,0F,0F);
            }
            else
            {
                pMatrixStack.scale(this.scale, this.scale, this.scale);
                pMatrixStack.mulPose(Axis.YP.rotationDegrees(pEntity.tickCount*45F));
            }

            this.itemRenderer.renderStatic(((ItemSupplier)pEntity).getItem(), ItemDisplayContext.GROUND, pPackedLight, NO_OVERLAY, pMatrixStack, pBuffer, pEntity.level(), pEntity.getId());
            pMatrixStack.popPose();
            super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
            
        }
        if (pEntity.hasTrail()) {
            double x = Mth.lerp(pPartialTicks, pEntity.xOld, pEntity.getX());
            double y = Mth.lerp(pPartialTicks, pEntity.yOld, pEntity.getY());
            double z = Mth.lerp(pPartialTicks, pEntity.zOld, pEntity.getZ());
            pMatrixStack.pushPose();
            pMatrixStack.translate(-x, -y, -z);
            renderTrail(pEntity, pPartialTicks, pMatrixStack, pBuffer, 1F,0.6F,0.15F, 0.4F, 250);
            pMatrixStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(CoinEntity coinEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
    private void renderTrail(CoinEntity entityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, float trailR, float trailG, float trailB, float trailA, int packedLightIn) {

        int sampleSize = entityIn.getTillDiscarded();
        float trailHeight = 0.3F;
        float trailZRot = entityIn.tickCount;

        Vec3 topAngleVec = new Vec3(0, trailHeight-0.12, 0).zRot(trailZRot/100F);
        Vec3 bottomAngleVec = new Vec3(0, -trailHeight-0.12, 0).zRot(trailZRot/100F);

        Vec3 drawFrom = entityIn.getTrailPosition(0, partialTicks);
        VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityTranslucent(TRAIL_TEXTURE));
        for(int samples = 0; samples < sampleSize; samples++) {
            Vec3 sample = entityIn.getTrailPosition(samples + 2, partialTicks);
            float u1 = samples / (float) sampleSize;
            float u2 = u1 + 1 / (float) sampleSize;
            float offset = (samples+ 5 - sampleSize)*0.03F;
            Vec3 draw1 = drawFrom;
            Vec3 draw2 = sample;

            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();

            vertexconsumer.vertex(matrix4f, (float) draw1.x + (float) bottomAngleVec.x, (float) draw1.y + (float) bottomAngleVec.y, (float) draw1.z + (float) bottomAngleVec.z).color(trailR, trailG-offset, trailB-offset, trailA).uv(u1, 1F).overlayCoords(NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) draw2.x + (float) bottomAngleVec.x, (float) draw2.y + (float) bottomAngleVec.y, (float) draw2.z + (float) bottomAngleVec.z).color(trailR, trailG-offset, trailB-offset, trailA).uv(u2, 1F).overlayCoords(NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) draw2.x + (float) topAngleVec.x, (float) draw2.y + (float) topAngleVec.y, (float) draw2.z + (float) topAngleVec.z).color(trailR, trailG-offset, trailB-offset, trailA).uv(u2, 0).overlayCoords(NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) draw1.x + (float) topAngleVec.x, (float) draw1.y + (float) topAngleVec.y, (float) draw1.z + (float) topAngleVec.z).color(trailR, trailG-offset, trailB-offset, trailA).uv(u1, 0).overlayCoords(NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();

            drawFrom = sample;
        }
    }


}