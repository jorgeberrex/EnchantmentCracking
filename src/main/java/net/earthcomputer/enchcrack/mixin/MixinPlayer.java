package net.earthcomputer.enchcrack.mixin;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class MixinPlayer extends EntityLivingBase {

	public MixinPlayer(World world) {
		super(EntityType.PLAYER, world);
	}

	private boolean wasWet;

	@Inject(method = "tick", at = @At("RETURN"))
	public void onTick(CallbackInfo ci) {
		if (world.isRemote) {
			if (isSprinting()) {
				EnchantmentCracker.resetCracker("sprint");
			}
			if (isWet() && !wasWet) {
				EnchantmentCracker.resetCracker("enterWater");
			}
			if (isWet() && (!isSneaking() || !onGround)) {
				EnchantmentCracker.resetCracker("swim");
			}
			wasWet = isWet();
			if (!getActivePotionEffects().isEmpty()) {
				EnchantmentCracker.resetCracker("potion");
			}
			if (!EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, this).isEmpty()
					&& !world.getEntitiesWithinAABB(EntityXPOrb.class, this.getBoundingBox()).isEmpty()) {
				EnchantmentCracker.resetCracker("mending");
			}
			if (isInWater() && EnchantmentHelper.getRespirationModifier(this) > 0) {
				EnchantmentCracker.resetCracker("respiration");
			}
			if (EnchantmentHelper.hasFrostWalker(this)) {
				EnchantmentCracker.frostWalkerCheck((EntityPlayer) (Object) this,
						EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, this));
			}
			if (isElytraFlying()) {
				ItemStack elytra = ((EntityPlayer) (Object) this).inventory.armorItemInSlot(2);
				if (!elytra.isEmpty() && elytra.getItem() == Items.ELYTRA) {
					EnchantmentCracker.toolDamageCheck(elytra, 1);
				}
			}
		}
	}

}
