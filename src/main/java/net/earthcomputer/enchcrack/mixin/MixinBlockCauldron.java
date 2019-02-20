package net.earthcomputer.enchcrack.mixin;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockCauldron.class)
public abstract class MixinBlockCauldron extends Block {

	public MixinBlockCauldron(Properties properties) {
		super(properties);
	}

	@Inject(method = "onBlockActivated", at = @At("HEAD"))
	public void onOnBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> ci) {
		if (!world.isRemote)
			return;
		ItemStack heldItem = player.getHeldItem(hand);
		if (heldItem.isEmpty())
			return;
		int level = state.get(BlockCauldron.LEVEL);
		Item item = heldItem.getItem();
		if (!player.abilities.isCreativeMode && heldItem.getCount() > 1) {
			if (player.inventory.getFirstEmptyStack() == -1) {
				if (item == Items.BUCKET && level == 3) {
					EnchantmentCracker.dropItemCheck();
				} else if (item == Items.GLASS_BOTTLE && level > 0) {
					EnchantmentCracker.dropItemCheck();
				}
			} else if (item.isIn(ItemTags.BANNERS) && TileEntityBanner.getPatterns(heldItem) > 0 && level > 0) {
				ItemStack cleanedBanner = heldItem.copy();
				cleanedBanner.setCount(1);
				TileEntityBanner.removeBannerData(cleanedBanner);
				if (player.inventory.storeItemStack(cleanedBanner) == -1) {
					EnchantmentCracker.dropItemCheck();
				}
			}
		}
	}

}
