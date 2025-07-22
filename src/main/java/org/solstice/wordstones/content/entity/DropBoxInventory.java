package org.solstice.wordstones.content.entity;

import com.mojang.serialization.Codec;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

public class DropBoxInventory implements Inventory {

	public static final Codec<DropBoxInventory> CODEC = Codec.list(ItemStack.VALIDATED_CODEC).xmap(
		DropBoxInventory::new,
		DropBoxInventory::getItems
	);

	protected final List<ItemStack> items;

	public DropBoxInventory(PlayerInventory inventory) {
		this(getCombinedInventory(inventory.combinedInventory));
	}

	public static List<ItemStack> getCombinedInventory(List<DefaultedList<ItemStack>> inventories) {
		List<ItemStack> result = new ArrayList<>();
		inventories.reversed().forEach(stacks -> stacks.stream()
			.filter(stack -> !stack.isEmpty())
			.forEach(stack -> result.add(stack.copy()))
		);
		return result;
	}

	public DropBoxInventory(List<ItemStack> items) {
		this.items = items;
	}

	@Override
	public int size() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		return this.items.stream().anyMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getStack(int slot) {
		return items.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack result = Inventories.splitStack(items, slot, amount);
		if (!result.isEmpty()) {
			markDirty();
		}
		return result;
	}

	@Override
	public ItemStack removeStack(int slot) {
		ItemStack result = Inventories.removeStack(items, slot);
		markDirty();
		return result;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		items.set(slot, stack);
		markDirty();
	}

	@Override
	public void markDirty() {}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		items.clear();
		markDirty();
	}

	public List<ItemStack> getItems() {
		return this.items;
	}

}
