package net.atired.coinmod.Items.custom;

import com.google.common.collect.ImmutableSet;
import net.atired.coinmod.Items.ItemRegistry;
import net.atired.coinmod.enchantments.EnchantmentRegistry;
import net.atired.coinmod.entity.projectile.BustedEntity;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Set;
import java.util.function.Consumer;

public class BlockBusterItem extends Item implements Vanishable {

    public BlockBusterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.is(ItemRegistry.NEJELEZO_INGOT.get());
    }
    public int getEnchantmentValue() {
        return 1;
    }
    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {

        pStack.hurtAndBreak(1, pLivingEntity, (p_43296_) -> {
            p_43296_.broadcastBreakEvent(p_43296_.getUsedItemHand());
        });
        System.out.println(pStack.getDamageValue() + " " + pStack.getMaxDamage());
        if (pStack.getUseDuration()-pTimeCharged > 5)
           {
            Player player = (Player)pLivingEntity;

            InteractionHand hand = InteractionHand.MAIN_HAND;
            if(player.getUsedItemHand() == InteractionHand.MAIN_HAND)
                hand = InteractionHand.OFF_HAND;
               if(pLivingEntity.getItemInHand(hand).getItem() instanceof  BlockItem){
                   ItemStack blockItem = pLivingEntity.getItemInHand(hand);
                   Vec3 vec3recoil = pLivingEntity.getViewVector(0).scale(pLivingEntity.getItemInHand(pLivingEntity.getUsedItemHand()).getEnchantmentLevel(EnchantmentRegistry.RECOIL.get())*pLivingEntity.getDeltaMovement().length());
                   pLivingEntity.addDeltaMovement(vec3recoil.scale(-0.8));
                   pLivingEntity.addDeltaMovement(pLivingEntity.getViewVector(0).scale(-0.5).scale(pLivingEntity.getItemInHand(pLivingEntity.getUsedItemHand()).getEnchantmentLevel(EnchantmentRegistry.RECOIL.get())));

                pLevel.playSound(player,BlockPos.containing(player.getEyePosition().x,player.getEyePosition().y,player.getEyePosition().z), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS,1,0.55F);
                if(pLevel instanceof ServerLevel serverLevel) {
                    float inaccuracy = 0.005F;
                    int u = 1;
                    float off = 0;
                    Block item = ((BlockItem) (pLivingEntity.getItemInHand(hand).getItem())).getBlock();
                    if(pStack.getEnchantmentLevel(EnchantmentRegistry.GRAPESHOT.get())==1)
                    {
                        u = 3;
                        off = 5.625F;
                        inaccuracy = 2F;
                    }
                    for(int i = 0; i < u; i++)
                    {

                        Vec3 vec31 = player.getUpVector(1.0F);
                        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(-(u-i-2)*off * 0.017453292F), vec31.x, vec31.y, vec31.z);
                        Vec3 vec3 = player.getViewVector(1.0F);
                        Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
                        BustedEntity busted = new BustedEntity(serverLevel, pLivingEntity, item.defaultBlockState(),pStack);
                        busted.setPos(pLivingEntity.getX(), pLivingEntity.getY() + 0.9, pLivingEntity.getZ());
                        busted.shoot(vector3f.x,vector3f.y, vector3f.z, ((float) Mth.clamp((pStack.getUseDuration() - pTimeCharged)/Math.pow(u,0.2), 0, 45)/6)/(0.9F+(float)Math.pow(item.defaultBlockState().getDestroySpeed(pLevel,new BlockPos(0,0,0)),0.5)/9F) + 0.1F, inaccuracy);
                        busted.addDeltaMovement(vec3recoil);
                        pLivingEntity.level().addFreshEntity(busted);
                    }
                }
                else
                {
                    Vec3 vec3 = player.getEyePosition().add(0,-0.3,0);
                    Vec3 eyeVec3 = player.getLookAngle().scale(0.8);
                    for(int i = 0; i < 6; i++)
                        pLevel.addParticle(ParticleTypes.SMOKE,vec3.x+(Math.random()-0.5)/3,vec3.y+(Math.random()-0.5)/3,vec3.z+(Math.random()-0.5)/3,eyeVec3.x+(Math.random()-0.5)/2,eyeVec3.y+(Math.random()-0.5)/2,eyeVec3.z+(Math.random()-0.5)/2);
                }
                   if(!player.getAbilities().instabuild)
                       blockItem.setCount(blockItem.getCount()-1);
            }

        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        InteractionHand hand = InteractionHand.MAIN_HAND;
        if(pUsedHand == InteractionHand.MAIN_HAND)
            hand = InteractionHand.OFF_HAND;
        if(pPlayer.getItemInHand(hand).getItem() instanceof BlockItem)
        {
            pPlayer.startUsingItem(pUsedHand);
            return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
        }
        return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
    }


}
