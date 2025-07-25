package org.solstice.wordstones.content.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.content.item.LocationBindingItem;
import org.solstice.euclidsElements.util.Location;
import org.solstice.wordstones.content.block.DropBoxBlock;
import org.solstice.wordstones.content.block.entity.DropBoxEntity;
import org.solstice.wordstones.registry.WordstoneComponentTypes;

public class LastWillItem extends LocationBindingItem {

	public LastWillItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof DropBoxBlock))
			return ActionResult.PASS;
		return super.useOnBlock(context);
	}

	@Nullable
	public static ItemStack getLastWillStack(PlayerEntity player) {
		for (Hand hand : Hand.values()) {
			ItemStack stack = player.getStackInHand(hand);
			if (!stack.isEmpty() && stack.getItem() instanceof LastWillItem)
				return stack;
		}
		return null;
	}

	public static void beforeDeath(PlayerEntity player, DamageSource source) {
		MinecraftServer server = player.getServer();
		if (server == null) return;

		ItemStack stack = getLastWillStack(player);
		if (stack == null) return;

		Location location = stack.getOrDefault(WordstoneComponentTypes.LOCATION, Location.ZERO);
		if (location.isZero()) return;

		World world = server.getWorld(location.worldKey());
		if (world == null) return;

		if (world.getBlockEntity(location.pos()) instanceof DropBoxEntity dropBox) {
			stack.decrement(1);
			dropBox.depositItems(player);
		}
	}

}
