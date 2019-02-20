package net.earthcomputer.enchcrack;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.util.registry.IRegistry;

public class EnchantmentInstance extends EnchantmentData {

	private int hash;

	public EnchantmentInstance(Enchantment enchantmentObj, int enchLevel) {
		super(enchantmentObj, enchLevel);
		hash = IRegistry.ENCHANTMENT.getId(enchantmentObj) + enchLevel;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof EnchantmentData && equals((EnchantmentData) other);
	}

	public boolean equals(EnchantmentData other) {
		return enchantment == other.enchantment && enchantmentLevel == other.enchantmentLevel;
	}

}