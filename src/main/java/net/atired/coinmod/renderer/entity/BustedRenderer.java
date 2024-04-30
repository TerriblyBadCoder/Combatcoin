package net.atired.coinmod.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Iterator;

import com.mojang.math.Axis;
import net.atired.coinmod.entity.projectile.BustedEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

@OnlyIn(Dist.CLIENT)
public class BustedRenderer extends EntityRenderer<BustedEntity> {
    private final BlockRenderDispatcher dispatcher;

    public BustedRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.shadowRadius = 0F;
        this.dispatcher = pContext.getBlockRenderDispatcher();
    }

    @Override
    public ResourceLocation getTextureLocation(BustedEntity bustedEntity) {
        return null;
    }

    public void render(BustedEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        BlockState blockstate = Blocks.SAND.defaultBlockState();
        if(pEntity.getState()!=null)
        {
             blockstate = pEntity.getState();
        }
        else
        {
            blockstate = Blocks.SAND.defaultBlockState();
        }

        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            Level level = pEntity.level();
            if (blockstate != level.getBlockState(pEntity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                pMatrixStack.pushPose();
                pEntity.addRotation(10F*(float)pEntity.getDeltaMovement().length());
                pMatrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
                pMatrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot())));
                pMatrixStack.mulPose(Axis.XP.rotationDegrees(pEntity.getRotation()));
                float scale = pEntity.getScale();
                BlockPos blockpos = BlockPos.containing(pEntity.getX(), pEntity.getBoundingBox().maxY, pEntity.getZ());
                pMatrixStack.translate(scale*-0.5, scale*-0.5, scale*-0.5);
                pMatrixStack.scale(scale,scale,scale);
                BakedModel model = this.dispatcher.getBlockModel(blockstate);
                Iterator var11 = model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(pEntity.getStartPos())), ModelData.EMPTY).iterator();

                while(var11.hasNext()) {
                    RenderType renderType = (RenderType)var11.next();
                    this.dispatcher.getModelRenderer().tesselateBlock(level, model, blockstate, blockpos, pMatrixStack, pBuffer.getBuffer(renderType), false, RandomSource.create(), blockstate.getSeed(pEntity.getStartPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
                }

                pMatrixStack.popPose();

                super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
            }
        }

    }


}