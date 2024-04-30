package net.atired.coinmod.event;

import net.atired.coinmod.Items.ItemRegistry;
import net.atired.coinmod.entity.projectile.CoinEntity;
import net.atired.coinmod.particle.ParticleRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CarrotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class ProjectileEvents {
    @SubscribeEvent
    public void FiredWorksTick(LivingEvent.LivingTickEvent event)
    {
        if(event.getEntity().getDeltaMovement().y()<-0.1 & event.getEntity().getPersistentData().getInt("coinmod_firedworks")>0)
        {
            int Power = event.getEntity().getPersistentData().getInt("coinmod_firedworks");
            System.out.println("Eh? ha! heh heh!" + event.getEntity().level());
            LivingEntity livingEntity= event.getEntity();
            if (event.getEntity().level() instanceof ServerLevel serverLevel) {
                livingEntity.getPersistentData().putInt("coinmod_firedworks",0);
                livingEntity.invulnerableTime = 0;
                livingEntity.setRemainingFireTicks(livingEntity.getRemainingFireTicks()+5*Power);
                livingEntity.addDeltaMovement(new Vec3(0,0.2*Power,0));
                livingEntity.hurt(livingEntity.level().damageSources().inFire(),2*Power);
                serverLevel.sendParticles(ParticleRegistry.FIREDWORK_PARTICLES.get(),livingEntity.getX(),livingEntity.getY(),livingEntity.getZ(),1,0,0,0,0);
                serverLevel.sendParticles(ParticleRegistry.FIREDWORK_PARTICLES.get(),livingEntity.getX(),livingEntity.getY(),livingEntity.getZ(),1,0,0,0,4);
            }

        }
    }
    @SubscribeEvent
    public void Eventthing(ProjectileImpactEvent event)
    {
        event.getImpactResult();
        Level level = event.getProjectile().level();

        if(event.getRayTraceResult().getType() == HitResult.Type.ENTITY & !(event.getProjectile() instanceof CoinEntity))
        {
            double movement = event.getProjectile().getDeltaMovement().length()/4;
            Vec3 hitRes = event.getRayTraceResult().getLocation();



                AABB box = new AABB(hitRes.x-0.1,hitRes.y-0.1,hitRes.z+0.1,hitRes.x+0.1,hitRes.y+0.1,hitRes.z+0.1);
                box = box.inflate(1);

                double dist = 9999;
                List<CoinEntity> coinies = new java.util.ArrayList<>(event.getEntity().level().getEntitiesOfClass(CoinEntity.class, box.inflate(20)).stream().toList());
                Projectile proj = getNearestCoin(coinies,TargetingConditions.DEFAULT,null,hitRes.x,hitRes.y,hitRes.z);;


                if(proj!=null)
                {

                    if (!event.getEntity().level().isClientSide) {
                        if(level instanceof ServerLevel serverLevel)
                        {
                            serverLevel.sendParticles(ParticleRegistry.RICOCHOT_PARTICLES.get(),hitRes.x,hitRes.y,hitRes.z,1,0,0,0,0);
                        }


                        List<LivingEntity> entities = new java.util.ArrayList<>(event.getEntity().level().getEntitiesOfClass(LivingEntity.class, box.inflate(20)).stream().toList());
                        List<CoinEntity> coins = new java.util.ArrayList<>(event.getEntity().level().getEntitiesOfClass(CoinEntity.class, box.inflate(20)).stream().toList());
                        if (event.getProjectile().getOwner() != null)
                            entities.remove(event.getProjectile().getOwner());
                        CoinEntity coin = getNearestCoin(coins,TargetingConditions.DEFAULT,(CoinEntity)proj,hitRes.x,hitRes.y,hitRes.z);
                        LivingEntity eh = event.getEntity().level().getNearestEntity(entities, TargetingConditions.DEFAULT, null, hitRes.x, hitRes.y, hitRes.z);
                        if(coin == null)
                        {
                            if (eh != null) {

                                Projectile projectile = replaceProjectile(event.getProjectile(), level);
                                Vec3 offset = eh.getPosition(0).add(event.getEntity().getPosition(0).multiply(-1, -1, -1)).add(0, eh.getBbHeight() / 2 + 0.05F, 0).add(eh.getDeltaMovement());
                                offset = offset.normalize().multiply(2, 2, 2);
                                projectile.shoot(offset.x, offset.y, offset.z, 2F+(float)movement , 0.01F);
                                level.addFreshEntity(projectile);
                            }
                        }
                        else
                        {
                            Projectile projectile = replaceProjectile(event.getProjectile(), level);
                            Vec3 offset = coin.getPosition(0).add(event.getEntity().getPosition(0).multiply(-1, -1, -1)).add(0, 0.2F, 0);
                            offset = offset.normalize().multiply(2, 2, 2);
                            projectile.shoot(offset.x, offset.y, offset.z, 3F+(float)movement, 0F);
                            level.addFreshEntity(projectile);
                        }

                    }
                    CoinEntity coin2 = (CoinEntity)proj;
                    coin2.discardCoin();

                }
        }
    }
    @SubscribeEvent
    public void FOVchanges(ComputeFovModifierEvent event)
    {
        float f = event.getFovModifier();
        Player p = event.getPlayer();
        if (p.isUsingItem()) {
            if (p.getUseItem().is(ItemRegistry.BLOCKBUSTER.get())) {
                int i = p.getUseItem().getUseDuration() - p.getUseItemRemainingTicks();
                float f1 = (float)i / 20.0F;
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                } else {
                    f1 *= f1;
                }

                f *= 1.0F - f1 * 0.3F;
            }
        }
        event.setNewFovModifier(f);
    }
    @Nullable
    public static Projectile replaceProjectile(Projectile projectile, Level level) {
        //yeah, I stole this from uraneptus,,,
        Projectile oldProjectile = projectile;
        projectile = (Projectile) projectile.getType().create(level);
        if (projectile == null) {
            return null;
        }
        oldProjectile.setRemoved(Entity.RemovalReason.DISCARDED);
        CompoundTag compoundtag = oldProjectile.saveWithoutId(new CompoundTag());
        compoundtag.remove("Motion");
        projectile.load(compoundtag);
        return projectile;
    }

    @Nullable
    public CoinEntity getNearestCoin(List<CoinEntity> pEntities, TargetingConditions pPredicate, @Nullable CoinEntity coinEntity, double pX, double pY, double pZ) {
        double $$6 = -1.0;
        CoinEntity $$7 = null;
        Iterator var13 = pEntities.iterator();

        while(true) {
            CoinEntity $$8;
            double $$9;
            do {

                    if (!var13.hasNext()) {
                        return $$7;
                    }

                    $$8 = (CoinEntity)var13.next();



                $$9 = $$8.distanceToSqr(pX, pY, pZ);
            } while($$6 != -1.0 && !($$9 < $$6));


            if(!$$8.isGonnaBeDiscarded() & $$8 != coinEntity)
            {
                $$6 = $$9;
                $$7 = $$8;
            }

        }
    }
}
