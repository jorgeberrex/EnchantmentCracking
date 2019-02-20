package net.earthcomputer.enchcrack.mixin;

import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

	@Inject(method = "Lnet/minecraft/client/Minecraft;loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Lnet/minecraft/client/gui/GuiScreen;)V", at = @At("HEAD"))
	public void onUnloadWorld(WorldClient world, GuiScreen a, CallbackInfo ci) {
		EnchantmentCracker.resetCracker("recreatePlayer");
	}

}
