package net.atired.coinmod.mixin;

import net.atired.coinmod.Items.custom.ChainedSawItem;
import net.atired.coinmod.enchantments.EnchantmentRegistry;
import net.atired.coinmod.particle.ParticleRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity{


    @Shadow public abstract HumanoidArm getMainArm();

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method= "sweepAttack",at = @At("HEAD"), cancellable = true)
    public void chainedsaw(CallbackInfo ci){


        double d0 = (double)(-Mth.sin(this.getYRot() * 0.017453292F));
        double d1 = (double)Mth.cos(this.getYRot() * 0.017453292F);
        if (level() instanceof ServerLevel && this.getMainHandItem().getItem() instanceof ChainedSawItem && ((CompoundTag)this.getMainHandItem().serializeNBT().get("tag")).getInt("coinmod_heatpower") > 40) {
            if(this.getMainHandItem().getEnchantmentLevel(EnchantmentRegistry.OVERHEAT.get())>0)
            {
                ci.cancel();
                ((ServerLevel)this.level()).sendParticles(ParticleRegistry.COIN_TRAIL_PARTICLES.get(), this.getX() + d0, this.getY(0.5), this.getZ() + d1, 1,0.0, 0.0, 0.0, 0.0);
            }
        }
    }


}
