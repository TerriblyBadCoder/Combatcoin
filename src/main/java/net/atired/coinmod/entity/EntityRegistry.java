package net.atired.coinmod.entity;

import net.atired.coinmod.CombatCoin;
import net.atired.coinmod.entity.projectile.BustedEntity;
import net.atired.coinmod.entity.projectile.CoinEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CombatCoin.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> DEF_REG
            = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CombatCoin.MODID);
    public static final RegistryObject<EntityType<CoinEntity>> COIN = DEF_REG.register("coin", () -> (EntityType)
            EntityType.Builder.of(CoinEntity::new, MobCategory.MISC).sized(0.7F, 0.7F).setCustomClientFactory(CoinEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).build("coin"));
    public static final RegistryObject<EntityType<BustedEntity>> BUSTED = DEF_REG.register("busted", () -> (EntityType)
            EntityType.Builder.of(BustedEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(true).build("busted"));
}
