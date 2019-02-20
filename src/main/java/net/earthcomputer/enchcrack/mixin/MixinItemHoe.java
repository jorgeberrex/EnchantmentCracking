package net.earthcomputer.enchcrack.mixin;

import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemHoe.class)
public abstract class MixinItemHoe extends Item {

	public MixinItemHoe(Properties properties) {
		super(properties);
	}

	@Inject(method = "onItemUse", at = @At("HEAD"))
	public void onSetBlock(ItemUseContext context,
                           CallbackInfoReturnable<EnumActionResult> cir) {
		EnchantmentCracker.toolDamageCheck(context.getItem(), 1);
	}

}
