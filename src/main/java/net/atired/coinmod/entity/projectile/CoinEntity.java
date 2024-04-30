package net.atired.coinmod.entity.projectile;

import net.atired.coinmod.Items.ItemRegistry;
import net.atired.coinmod.entity.EntityRegistry;
import net.atired.coinmod.particle.ParticleRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;


public class CoinEntity extends ThrowableItemProjectile {
    private Vec3[] trailPositions = new Vec3[64];
    private int trailPointer = -1;
    private int tilldiscarded = 5;
    private boolean discarded = false;
    private Vec3 lastPos;
    public CoinEntity(EntityType pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.lastPos = new Vec3(0,0,0);

    }

    public CoinEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(EntityRegistry.COIN.get(), level);
    }

    public CoinEntity(Level level, LivingEntity thrower) {
        super(EntityRegistry.COIN.get(), thrower, level);

    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.discardCoin();
    }

    @Override
    public boolean canBeHitByProjectile() {
        return !this.discarded;
    }

    public CoinEntity(Level level, double x, double y, double z) {
        super(EntityRegistry.COIN.get(), x, y, z, level);
    }
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return (Packet<ClientGamePacketListener>)NetworkHooks.getEntitySpawningPacket(this);
    }
    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.isRemoved()) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale(partialTick));
    }
    public boolean hasTrail() {
        return trailPointer != -1;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if(!(pResult.getEntity() instanceof CoinEntity))
            this.discardCoin();
    }

    public void tick() {
        super.tick();
        if(false)
        {
            Vec3 interpos = new Vec3((this.getX()+this.lastPos.x)/2,(this.getY()+this.lastPos.y)/2,(this.getZ()+this.lastPos.z)/2);
            this.level().addParticle(ParticleRegistry.COIN_TRAIL_PARTICLES.get(),interpos.x,interpos.y+0.1F,interpos.z,0,0,0);
            this.level().addParticle(ParticleRegistry.COIN_TRAIL_PARTICLES.get(),this.getX(),this.getY()+0.1F,this.getZ(),0,0,0);
        }
        Vec3 vec31 = this.getDeltaMovement();
        this.setDeltaMovement(vec31.x, vec31.y + (double)this.getGravity()/3, vec31.z);
        this.lastPos = this.position();
        Vec3 trailAt = this.position().add(0, this.getBbHeight() / 2F, 0);
        if (trailPointer == -1) {
            Vec3 backAt = trailAt;
            for (int i = 0; i < trailPositions.length; i++) {
                trailPositions[i] = backAt;
            }
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = trailAt;

        if(this.discarded)
        {

            this.tilldiscarded=this.tilldiscarded-1;
            this.setDeltaMovement(0,0,0);
            this.setNoGravity(true);

        }

        if(this.tilldiscarded==0)
            this.discard();
    }
    public int getTillDiscarded()
    {
        return tilldiscarded;
    }
    public void discardCoin()
    {
        this.discarded = true;
        this.setBoundingBox(this.getBoundingBox().inflate(-0.99F));

    }
    public boolean isGonnaBeDiscarded()
    {
        return discarded;
    }



    protected Item getDefaultItem() {
        return ItemRegistry.COIN.get();
    }
}
