package org.solstice.wordstones.content.item;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.solstice.euclidsElements.content.item.LocationBindingItem;
import org.solstice.euclidsElements.util.Location;
import org.solstice.wordstones.content.block.DropBoxBlock;
import org.solstice.wordstones.content.block.entity.DropBoxEntity;
import org.solstice.wordstones.registry.WordstoneComponentTypes;

public class LastWillItem extends LocationBindingItem {

	static {
		ServerLivingEntityEvents.AFTER_DEATH.register(LastWillItem::afterDeath);
	}

	public LastWillItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof DropBoxBlock))
			return ActionResult.PASS;
		return super.useOnBlock(context);
	}

	public static void afterDeath(LivingEntity entity, DamageSource damageSource) {
		if (!(entity instanceof PlayerEntity player)) return;

		ItemStack stack = player.getOffHandStack();
		if (stack.isEmpty() || !(stack.getItem() instanceof LastWillItem)) return;

		Location location = stack.getOrDefault(WordstoneComponentTypes.LOCATION, Location.ZERO);
		if (location.isZero()) return;

		World world = entity.getWorld().getServer().getWorld(location.worldKey());
		if (!(world.getBlockEntity(location.pos()) instanceof DropBoxEntity dropBoxEntity)) return;

		stack.decrement(1);
		dropBoxEntity.depositItems(player);
	}

}
