package net.atired.coinmod.Items.custom;

import ca.weblite.objc.Client;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.atired.coinmod.enchantments.EnchantmentRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.NoteBlockEvent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Filter;

public class ChainedSawItem extends SwordItem {
    IClientItemExtensions extension = new IClientItemExtensions() {
        @Override
        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            return true;

        }

    };

    public ChainedSawItem(Properties pProperties) {
        super(Tiers.IRON, 2, -0.2F, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.invulnerableTime = 1;
        int charge = ((CompoundTag)pStack.serializeNBT().get("tag")).getInt("coinmod_heatpower");
        CompoundTag compound = new CompoundTag();


        compound.putInt("coinmod_heatpower",Mth.clamp(charge,0,100)+(int)(20*Math.pow(((Player)pAttacker).getAttackStrengthScale(0.1F),2)));
        pStack.addTagElement("coinmod_heatpower",compound.get("coinmod_heatpower"));
        return super.hurtEnemy(pStack, pTarget, pAttacker);

    }
    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);

        CompoundTag compound = new CompoundTag();
        compound.putInt("coinmod_heatcharge", Mth.clamp(pStack.getUseDuration()-pTimeCharged,0,25));
        pStack.addTagElement("coinmod_heatcharge",compound.get("coinmod_heatcharge"));

        pLivingEntity.swing(pLivingEntity.getUsedItemHand());
    }
    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;

    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                int i = arm == HumanoidArm.RIGHT ? 1 : -1;

                if (!player.isUsingItem() & ((CompoundTag)itemInHand.serializeNBT().get("tag")).getInt("coinmod_heatcharge") > 0) {
                    poseStack.translate(0, 0.6, -1.05);

                    poseStack.mulPose(Axis.YP.rotationDegrees(-5));
                    poseStack.mulPose(Axis.XP.rotationDegrees(-87));
                }
                return false;

            }
        });
    }



    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        int charge = ((CompoundTag)pStack.serializeNBT().get("tag")).getInt("coinmod_heatcharge");
        int heat = ((CompoundTag)pStack.serializeNBT().get("tag")).getInt("coinmod_heatpower");
        AABB hitBox = pEntity.getBoundingBox().inflate(0.2);
        List<Entity> list = pLevel.getEntities(pEntity, hitBox);
        CompoundTag compound = new CompoundTag();
        boolean heated = false;

        if((pIsSelected||pSlotId==0) && charge>0)
        {
            if(heat > 40)
            {
                heat-=3;
                heated = true;
                pEntity.invulnerableTime += 3;
            }
            boolean bool = false;
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);
                    if (entity instanceof LivingEntity) {
                        if(((LivingEntity) entity).invulnerableTime>0)
                            bool = true;
                        pEntity.invulnerableTime += 1;
                        entity.hurt(pLevel.damageSources().playerAttack((Player)pEntity),5);
                        if(heated)
                        {
                            entity.setDeltaMovement(0,0.7,0);
                            entity.setRemainingFireTicks(entity.getRemainingFireTicks()+20);
                            if(pStack.getEnchantmentLevel(EnchantmentRegistry.FIREDWORKS.get())>0)
                            {
                                entity.getPersistentData().putInt("coinmod_firedworks",pStack.getEnchantmentLevel(EnchantmentRegistry.FIREDWORKS.get()));
                            }
                        }
                        charge -= 1;

                    }
                }
                if(!heated)
                {
                    if(bool)
                        pEntity.setDeltaMovement(pEntity.getViewVector(1).multiply(1,0.2,1).scale(-0.8).add(0,0.3,0));
                    charge=2;
                }


            }
            pEntity.addDeltaMovement(pEntity.getViewVector(1).multiply(1,0.2,1).scale(charge/30F));

        }
        if(charge>0)
        {
            compound.putInt("coinmod_heatcharge", charge-1);
            pStack.addTagElement("coinmod_heatcharge",compound.get("coinmod_heatcharge"));
        }


        if(heat>0 & pEntity.tickCount%2==0)
        {
            compound = new CompoundTag();
            if(pStack.getEnchantmentLevel(EnchantmentRegistry.OVERHEAT.get())>0 && heat > 40)
                compound.putInt("coinmod_heatpower", (int)((heat-1)*0.98));
            else
                compound.putInt("coinmod_heatpower", heat-1);
            pStack.addTagElement("coinmod_heatpower",compound.get("coinmod_heatpower"));
        }


    }



    @Override
    public int getMaxDamage(ItemStack stack) {
        return 384;
    }
}
