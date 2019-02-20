package net.earthcomputer.enchcrack;

import net.minecraft.client.Minecraft;
import org.dimdev.rift.listener.client.ClientTickable;
import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class EnchCrack implements ClientTickable, InitializationListener {

	@Override
	public void onInitialization() {
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.enchantment_cracking.json");
	}

	@Override
	public void clientTick(Minecraft minecraft) {
		if (Minecraft.getInstance().world != null)
			EnchantmentCracker.onTick();
	}
}
