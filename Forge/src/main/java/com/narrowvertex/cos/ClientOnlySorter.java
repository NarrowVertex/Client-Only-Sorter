package com.narrowvertex.cos;

import com.narrowvertex.cos.input.KeyBindings;
import net.minecraftforge.fml.common.Mod;

@Mod(ClientOnlySorter.MODID)
public class ClientOnlySorter {
	public static final String MODID = "cos";

	private ClientOnlySorterClient client;
	private KeyBindings keyMapping;

	public ClientOnlySorter() {
		client = new ClientOnlySorterClient();
		client.init();

		keyMapping = new KeyBindings();
		keyMapping.register();
	}
}
