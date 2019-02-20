package net.earthcomputer.enchcrack.mixin;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.block.Block;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ItemSpade.class)
public abstract class MixinItemSpade extends ItemTool {

	public MixinItemSpade(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Properties builder) {
		super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builder);
	}

	@Inject(method = "onItemUse", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z"))
	public void onOnItemUse(ItemUseContext context, CallbackInfoReturnable<EnumActionResult> ci) {
		if (context.getWorld().isRemote) {
			EnchantmentCracker.toolDamageCheck(context.getPlayer().getHeldItem(context.getPlayer().getActiveHand()), 1);
		}
	}

}
