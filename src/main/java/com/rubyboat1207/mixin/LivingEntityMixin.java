package com.rubyboat1207.mixin;

import com.rubyboat1207.ZachAdds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow private @Nullable LivingEntity attacker;

    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"))
    public void overrideTakeKnockback(LivingEntity instance, double strength, double x, double z) {
        System.out.println("Official Gaming (:");

        if(attacker == null) {
            return;
        }
        if(!attacker.getStackInHand(Hand.MAIN_HAND).isOf(ZachAdds.TREE_ITEM)) {
            instance.takeKnockback(strength, x, z);
        }
    }
}
