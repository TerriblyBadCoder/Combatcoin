package net.atired.coinmod.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Unique
    public boolean coyoteTime = false;
    @Unique
    public int coyoteTimeYet = 0;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow public abstract ItemStack getMainHandItem();



    @Inject(method = "jumpFromGround",at = @At("HEAD"), cancellable = true)
    protected void coin_jumpFromGround(CallbackInfo ci) {
        coyoteTime = false;
    }
    @Inject(method = "tick",at = @At("HEAD"), cancellable = true)
    public void ticking(CallbackInfo ci)
    {

        if(this.onGround())
            coyoteTime =true;
        if(coyoteTimeYet > 0)
        {
            coyoteTimeYet+=1;
        }
    }



    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;getValue()D"))
    public double coinmod_whatev(AttributeInstance instance)
    {

        if(getType()==EntityType.PLAYER)
        {


            if(coyoteTimeYet < 20 & coyoteTime & getDeltaMovement().length() > 0.05 & level().getBlockState(BlockPos.containing(position().x,position().y-0.1,position().z)).getBlock() == Blocks.AIR)
            {
                if(coyoteTimeYet == 0)
                {
                    coyoteTimeYet+=1;
                    setDeltaMovement(getDeltaMovement().x,0,getDeltaMovement().z);
                }
                return Math.max(0F,(coyoteTimeYet-10)*0.002F);
            }
            else
            {
                if(coyoteTime)
                {
                    coyoteTime = false;
                    coyoteTimeYet = 0;
                    setDeltaMovement(getDeltaMovement().x,0,getDeltaMovement().z);
                }
                System.out.println(coyoteTime + " " + this.onGround() + " " + instance.getValue());
                return  instance.getValue();
            }

        }
        return instance.getValue();
    }
}
