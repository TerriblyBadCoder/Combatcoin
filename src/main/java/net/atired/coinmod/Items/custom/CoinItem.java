package net.atired.coinmod.Items.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SmithingTableBlock;

import java.util.function.Function;

public class CoinItem extends Item {
    private final float throwAngle;
    private final float throwSpeed;
    private final float throwRandomness;
    private final Function<Player, ThrowableItemProjectile> projectileSupplier;
    public CoinItem(Properties pProperties, Function<Player, ThrowableItemProjectile> projectileSupplier, float throwAngle, float throwSpeed, float throwRandomness) {
        super(pProperties);
        this.throwAngle = throwAngle;
        this.throwSpeed = throwSpeed;
        this.throwRandomness = throwRandomness;
        this.projectileSupplier = projectileSupplier;

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.swing(pUsedHand);
        pLevel.playSound((Player) null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 2F);
        if (!pLevel.isClientSide) {
            ThrowableItemProjectile coin = projectileSupplier.apply(pPlayer);
            coin.setItem(itemstack);
            coin.setPos(coin.getX(),coin.getY()-0.2F,coin.getZ());
            coin.shootFromRotation(pPlayer, pPlayer.getXRot()-5, pPlayer.getYRot(), throwAngle, throwSpeed, throwRandomness);
            pLevel.addFreshEntity(coin);
        }
        if (!pPlayer.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
