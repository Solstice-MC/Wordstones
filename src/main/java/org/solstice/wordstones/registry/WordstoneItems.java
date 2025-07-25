package org.solstice.wordstones.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.solstice.euclidsElements.EuclidsPlayerEvents;
import org.solstice.wordstones.Wordstones;
import org.solstice.euclidsElements.client.EuclidsModelPredicateProviderRegistry;
import org.solstice.wordstones.content.item.LastWillItem;
import org.solstice.euclidsElements.content.item.LocationBindingItem;

import java.util.function.Function;

public class WordstoneItems {

	public static void init() {
		EuclidsPlayerEvents.BEFORE_DEATH.register(LastWillItem::beforeDeath);
	}

	@Deprecated
	@Environment(EnvType.CLIENT)
	public static void clientInit() {
		EuclidsModelPredicateProviderRegistry.register(
			LocationBindingItem.class,
			Wordstones.of("bound_location"),
			LocationBindingItem::getLocationPredicate
		);
	}

	public static final Item LAST_WILL = register("last_will",
		LastWillItem::new,
		new Item.Settings()
			.maxCount(1)
			.rarity(Rarity.RARE)
	);

	public static Item register(String name) {
		return register(name, Item::new);
	}

	public static Item register(String name, Function<Item.Settings, Item> function) {
		return register(name, function, new Item.Settings());
	}

	public static Item register(String name, Item.Settings settings) {
		return register(name, Item::new, settings);
	}

	public static Item register(String name, Function<Item.Settings, Item> function, Item.Settings settings) {
		Identifier id = Wordstones.of(name);
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
		Item item = function.apply(settings);
		return Registry.register(Registries.ITEM, key, item);
	}

}
