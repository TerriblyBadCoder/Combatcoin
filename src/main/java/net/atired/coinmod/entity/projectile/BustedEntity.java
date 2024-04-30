package net.atired.coinmod.entity.projectile;

import net.atired.coinmod.enchantments.EnchantmentRegistry;
import net.atired.coinmod.entity.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

import java.util.Objects;

public class BustedEntity extends Projectile {

    protected static final EntityDataAccessor<BlockPos> DATA_START_POS;
    protected static final EntityDataAccessor<Integer> SNOWBALLINGLEVEL;
    protected static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE;
    protected static final EntityDataAccessor<Float> DATA_SCALE;
    protected static float HARDNESS;
    private float rotation;


    public BustedEntity(EntityType pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }

    public float getRotation() {
        return rotation;
    }

    public void addRotation(float rotation) {
        this.rotation += rotation;
    }

    public BustedEntity(Level level, LivingEntity player, BlockState pState, ItemStack pStack) {
        super(EntityRegistry.BUSTED.get(),level);
        this.setOwner(player);
        this.setStartPos(this.blockPosition());
        this.entityData.set(DATA_BLOCK_STATE,pState);
        if(pStack.getEnchantmentLevel(EnchantmentRegistry.SNOWBALLING.get()) >0)
        {
            this.entityData.set(SNOWBALLINGLEVEL,pStack.getEnchantmentLevel(EnchantmentRegistry.SNOWBALLING.get()));
        }
        if(pStack.getEnchantmentLevel(EnchantmentRegistry.GRAPESHOT.get()) == 1)
            this.entityData.set(DATA_SCALE,0.55F);
        else
            this.entityData.set(DATA_SCALE,1F);
        HARDNESS =  pState.getDestroySpeed(level,new BlockPos(0,0,0));

    }

    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        this.setStartPos(this.blockPosition());
    }
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if(level() instanceof  ServerLevel serverLevel)
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,this.entityData.get(DATA_BLOCK_STATE)),this.getX()+(Math.random()-0.5)*getScale(),this.getY()+(Math.random()-0.5)*getScale(),this.getZ()+(Math.random()-0.5)*getScale(),5,0,0,0,0.2);
        if(!this.level().isClientSide & this.getState().getBlock() == Blocks.TNT)
        {

            level().explode(this,this.getX(),this.getY(),this.getZ(),2*getScale(), Level.ExplosionInteraction.TNT);
        }


        this.discard();
    }
    public float getScale()
    {
        return this.entityData.get(DATA_SCALE);
    }
    public BlockPos getStartPos() {
        return (BlockPos)this.entityData.get(DATA_START_POS);
    }
    public BlockState getState() {
        return (BlockState)this.entityData.get(DATA_BLOCK_STATE);
    }
    public void setStartPos(BlockPos pStartPos) {
        this.entityData.set(DATA_START_POS, pStartPos);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if(pResult.getEntity() instanceof LivingEntity)
        {
            if(level() instanceof ServerLevel serverLevel)
                serverLevel.sendParticles(ParticleTypes.FLASH,pResult.getLocation().x,pResult.getLocation().y+ (pResult.getEntity().getBoundingBox().maxY-pResult.getEntity().getBoundingBox().minY)/2 ,pResult.getLocation().z,1,0,0,0,0);
            if(getOwner()!=null)
            {

                pResult.getEntity().hurt(level().damageSources().mobProjectile(this,(LivingEntity)getOwner()), (float)((this.getDeltaMovement().length()*2+3F)*getScale()*Math.pow(HARDNESS,0.4)));
                pResult.getEntity().addDeltaMovement(this.getDeltaMovement().multiply(0.5,0.3,0.5).scale(Math.pow(HARDNESS,0.4)+0.25).scale(getScale()));
            }
            else
            {
                pResult.getEntity().hurt(level().damageSources().mobProjectile(this,null), (float)((this.getDeltaMovement().length()*2+3F)*getScale()*Math.pow(HARDNESS,0.4)));
                pResult.getEntity().addDeltaMovement(this.getDeltaMovement().multiply(0.5,0.3,0.5).scale(Math.pow(HARDNESS,0.4)+0.25).scale(getScale()));
            }

        }

        if(this.getDeltaMovement().length()>0.8)
        {
            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.1,Math.abs(this.getDeltaMovement().y)/this.getDeltaMovement().y/3,-0.1).add(0,0.6,0));
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_START_POS, BlockPos.ZERO);
        this.entityData.define(DATA_BLOCK_STATE, Blocks.MAGMA_BLOCK.defaultBlockState());
        this.entityData.define(DATA_SCALE,1F);
        this.entityData.define(SNOWBALLINGLEVEL,0);
    }
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.put("BlockState", NbtUtils.writeBlockState((BlockState)this.entityData.get(DATA_BLOCK_STATE)));
        pCompound.putFloat("BlockScale",this.entityData.get(DATA_SCALE));
        pCompound.putInt("Snowballing",this.entityData.get(SNOWBALLINGLEVEL));

    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.entityData.set(DATA_BLOCK_STATE,NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), pCompound.getCompound("BlockState")));
        this.entityData.set(DATA_SCALE,pCompound.getFloat("BlockScale"));
        this.entityData.set(SNOWBALLINGLEVEL,pCompound.getInt("Snowballing"));
    }
    @Override
    public void tick() {
        super.tick();

        Vec3 vec32 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec32.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec32.x, vec32.z) * 57.2957763671875));
            this.setXRot((float)(Mth.atan2(vec32.y, d0) * 57.2957763671875));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        boolean flag = false;
        if (hitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)hitresult).getBlockPos();
            BlockState blockstate = this.level().getBlockState(blockpos);
            if (blockstate.is(Blocks.NETHER_PORTAL)) {
                this.handleInsidePortal(blockpos);
                flag = true;
            } else if (blockstate.is(Blocks.END_GATEWAY)) {
                BlockEntity blockentity = this.level().getBlockEntity(blockpos);
                if (blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    TheEndGatewayBlockEntity.teleportEntity(this.level(), blockpos, blockstate, this, (TheEndGatewayBlockEntity)blockentity);
                }

                flag = true;
            }
        }

        if (hitresult.getType() != HitResult.Type.MISS && !flag && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d2 = this.getX() + vec3.x;
        double d0 = this.getY() + vec3.y;
        double d1 = this.getZ() + vec3.z;
        this.updateRotation();
        float f;
        if (this.isInWater()) {
            for(int i = 0; i < 4; ++i) {
                float f1 = 0.25F;
                this.level().addParticle(ParticleTypes.BUBBLE, d2 - vec3.x * 0.25, d0 - vec3.y * 0.25, d1 - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
            }

            f = 0.8F;
        } else {
            f = 0.93F;
        }
        boolean flag1 = this.entityData.get(DATA_BLOCK_STATE).getBlock() == Blocks.CHORUS_FLOWER;
        this.setDeltaMovement(vec3.scale((double)f));
        if (!this.isNoGravity()&&!flag1) {
            Vec3 vec31 = this.getDeltaMovement();
            this.setDeltaMovement(vec31.x, vec31.y - 0.12F, vec31.z);
        }
        else if(flag1 & this.tickCount>400)
        {

            for(int i = 0; i < 5; i++)
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK,this.entityData.get(DATA_BLOCK_STATE)),this.getX()+(Math.random()-0.5)*getScale(),this.getY()+(Math.random()-0.5)*getScale(),this.getZ()+(Math.random()-0.5)*getScale(),0,0.3,0);
            this.discard();
        }
        else if(flag1)
        {
            Vec3 vec31 = this.getDeltaMovement();
            this.setDeltaMovement(vec31.x, vec31.y + 0.04F, vec31.z);
        }
        this.entityData.set(DATA_SCALE,getScale()+this.entityData.get(SNOWBALLINGLEVEL)*0.03F/(float)Math.pow(getScale(),3));
        this.setPos(d2, d0, d1);

    }

    @Override
    protected AABB makeBoundingBox() {
        return this.getDimensions(Pose.STANDING).scale(getScale()).makeBoundingBox(this.position());
    }

    @Override
    public void setPos(double p_20210_, double p_20211_, double p_20212_) {
        this.setPosRaw(p_20210_, p_20211_, p_20212_);
        this.setBoundingBox(this.makeBoundingBox());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
    }
    static {
        DATA_START_POS = SynchedEntityData.defineId(BustedEntity.class, EntityDataSerializers.BLOCK_POS);
        DATA_BLOCK_STATE = SynchedEntityData.defineId(BustedEntity.class, EntityDataSerializers.BLOCK_STATE);
        DATA_SCALE = SynchedEntityData.defineId(BustedEntity.class, EntityDataSerializers.FLOAT);
        SNOWBALLINGLEVEL = SynchedEntityData.defineId(BustedEntity.class, EntityDataSerializers.INT);
        HARDNESS = 0;
    }
}
