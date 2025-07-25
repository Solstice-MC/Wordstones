package org.solstice.wordstones.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.solstice.euclidsElements.EuclidsPlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "onDeath", at = @At("HEAD"))
	public void beforeDeath(CallbackInfo info, @Local(argsOnly = true) DamageSource source) {
		System.out.println("aaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAaaaaaaaaaaaaaaaaaa");
		if (!this.isRemoved() && !this.dead)
			EuclidsPlayerEvents.BEFORE_DEATH.invoker().afterDeath((PlayerEntity)(Object)this, source);
	}

}
