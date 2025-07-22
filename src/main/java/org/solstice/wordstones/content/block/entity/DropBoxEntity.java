package org.solstice.wordstones.content.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.solstice.wordstones.content.entity.WordstonesPlayerInventory;
import org.solstice.wordstones.registry.WordstoneBlockEntityTypes;
import org.solstice.wordstones.registry.WordstonesSoundEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DropBoxEntity extends BlockEntity {

	protected Map<UUID, PlayerInventory> playerInventories = new HashMap<>();

	public DropBoxEntity(BlockPos pos, BlockState state) {
        super(WordstoneBlockEntityTypes.DROP_BOX, pos, state);
    }

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		if (this.playerInventories == null || this.playerInventories.isEmpty()) return;
		NbtCompound compound = new NbtCompound();
		this.playerInventories.forEach((uuid, inventory) -> {
			((WordstonesPlayerInventory)inventory).setRegistryLookup(registryLookup);
			NbtList list = new NbtList();
			list = inventory.writeNbt(list);
			compound.put(uuid.toString(), list);
		});
		nbt.put("player_inventories", compound);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		if (!nbt.contains("player_inventories")) return;

		NbtCompound compound = nbt.getCompound("player_inventories");
		compound.getKeys().forEach(uuid -> {
			NbtList list = compound.getList(uuid, 10);
			PlayerInventory inventory = new PlayerInventory(null);
			((WordstonesPlayerInventory)inventory).setRegistryLookup(registryLookup);
			inventory.readNbt(list);
			this.playerInventories.put(UUID.fromString(uuid), inventory);
		});
	}

	public ActionResult depositItems(PlayerEntity player) {
		UUID uuid = player.getUuid();
		if (this.playerInventories.containsKey(uuid)) {
			player.sendMessage(Text.translatable("block.wordstones.drop_box.inventory_exists"), true);
			return ActionResult.PASS;
		}

		PlayerInventory inventory = player.getInventory();
		if (inventory.isEmpty()) return ActionResult.PASS;

		PlayerInventory newInventory = tryUnequipPlayer(player);
		this.playerInventories.put(uuid, newInventory);

		markDirty();
		return ActionResult.SUCCESS;
	}

	public PlayerInventory tryUnequipPlayer(PlayerEntity player) {
		PlayerInventory inventory = new PlayerInventory(null);
		for (int slot = 0; slot < inventory.size(); slot++) {
			ItemStack stack = player.getInventory().getStack(slot);
			if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE))
				continue;

			if (!EnchantmentHelper.hasAnyEnchantmentsWith(stack, EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP))
				inventory.setStack(slot, stack);
			player.getInventory().setStack(slot, ItemStack.EMPTY);
		}
		return inventory;
	}

	public ActionResult retrieveItems(PlayerEntity player) {
		UUID uuid = player.getUuid();
		if (!this.playerInventories.containsKey(uuid)) {
			player.sendMessage(Text.translatable("block.wordstones.drop_box.inventory_not_exists"), true);
			return ActionResult.PASS;
		}

		PlayerInventory inventory = this.playerInventories.get(uuid);
		tryEquipPlayer(inventory, player);
		this.playerInventories.remove(uuid);

		markDirty();
		return ActionResult.SUCCESS;
	}

	public void tryEquipPlayer(PlayerInventory inventory, PlayerEntity player) {
		for (int slot = 0; slot < inventory.size(); slot++) {
			ItemStack stack = inventory.getStack(slot);
			if (player.getInventory().getStack(slot).isEmpty()) {
				player.getInventory().setStack(slot, stack);
			} else {
				player.getInventory().offerOrDrop(stack);
			}
		}
	}

}
