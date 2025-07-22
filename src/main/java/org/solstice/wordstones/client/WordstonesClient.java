package org.solstice.wordstones.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.solstice.euclidsElements.client.EuclidsModelPredicateProviderRegistry;
import org.solstice.wordstones.registry.WordstoneItems;

public class WordstonesClient implements ClientModInitializer {

	@Deprecated
	@Override
	public void onInitializeClient() {
		WordstoneItems.clientInit();
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> EuclidsModelPredicateProviderRegistry.init());
	}

}
