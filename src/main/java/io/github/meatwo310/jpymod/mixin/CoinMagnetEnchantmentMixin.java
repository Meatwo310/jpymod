package io.github.meatwo310.jpymod.mixin;

import io.github.lightman314.lightmanscurrency.common.capability.wallet.IWalletHandler;
import io.github.lightman314.lightmanscurrency.common.enchantments.CoinMagnetEnchantment;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CoinMagnetEnchantment.class,
        remap = false // SUPER IMPORTANT
)
public class CoinMagnetEnchantmentMixin {
    @Inject(method = "runEntityTick(" +
            "Lio/github/lightman314/lightmanscurrency/common/capability/wallet/IWalletHandler;" +
            "Lnet/minecraft/world/entity/LivingEntity;" +
            ")V",
            at = @At("HEAD"),
            cancellable = true)
    private static void runEntityTickMixin(IWalletHandler walletHandler, LivingEntity entity, CallbackInfo ci) {
        ci.cancel();
    }
}
