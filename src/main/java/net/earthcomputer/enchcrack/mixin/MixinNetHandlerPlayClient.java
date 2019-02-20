package net.earthcomputer.enchcrack.mixin;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @Inject(method = "sendPacket", at = @At("HEAD"))
    public void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof CPacketChatMessage) {
            String message = ((CPacketChatMessage) packet).getMessage();
            if (message.startsWith("/") && message.substring(1).trim().startsWith("give")) {
                EnchantmentCracker.resetCracker("give");
            }
        }
    }

}
