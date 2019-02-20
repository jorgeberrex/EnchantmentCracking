package net.earthcomputer.enchcrack.mixin;

import net.earthcomputer.enchcrack.EnchantmentCracker;
import net.earthcomputer.enchcrack.EnchantmentInstance;
import net.earthcomputer.enchcrack.EnchantmentManipulationPlan;
import net.earthcomputer.enchcrack.GuiSlotEnchantment;
import net.earthcomputer.enchcrack.gui.GuiSimpleButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Mixin(GuiEnchantment.class)
public abstract class MixinGuiEnchantment extends GuiContainer {

	public MixinGuiEnchantment(Container inventorySlots) {
		super(inventorySlots);
	}

	private GuiButton addInfoButton;
	private GuiSlotEnchantment list;
	private List<EnchantmentInstance> enchantments = new ArrayList<>();
	private Map<EnchantmentInstance, GuiButton> wantedButtons = new HashMap<>();
	private Map<EnchantmentInstance, GuiButton> unwantedButtons = new HashMap<>();
	private Map<EnchantmentInstance, GuiButton> dontCareButtons = new HashMap<>();
	private GuiButton manipulateButton;

	@Inject(method = "drawGuiContainerBackgroundLayer", at = @At("RETURN"))
	public void drawOverlay(float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
		List<EnchantmentData> wanted = new ArrayList<>();
		List<EnchantmentData> unwanted = new ArrayList<>();
		for (EnchantmentData ench : enchantments) {
			if (!wantedButtons.get(ench).enabled) {
				wanted.add(ench);
			} else if (!unwantedButtons.get(ench).enabled) {
				unwanted.add(ench);
			}
		}
		EnchantmentCracker.drawEnchantmentGUIOverlay(this, wanted, unwanted);
	}

	@Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;sendEnchantPacket(II)V"))
	public void onEnchantedItem(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> ci) {
		EnchantmentCracker.onEnchantedItem();
	}

	@Inject(method = "mouseClicked", at = @At(value = "RETURN"))
	public void onMouseClicked(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> ci) {
		list.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiEnchantment;renderHoveredToolTip(II)V"))
	public void onRender(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (EnchantmentCracker.isCracked())
			list.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean mouseScrolled(double p_mouseScrolled_1_) {
		if (EnchantmentCracker.isCracked())
			list.mouseScrolled(p_mouseScrolled_1_);
		return super.mouseScrolled(p_mouseScrolled_1_);
	}

	@Override
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		if (EnchantmentCracker.isCracked())
			list.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
		return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
	}

	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		if (EnchantmentCracker.isCracked())
			list.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
		return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
	}

	@Override
	public void initGui() {
		super.initGui();
		addInfoButton = addButton(new GuiSimpleButton(0, width / 2 + xSize / 2 + 10, height / 2 - ySize / 2 - 25, width / 2 - xSize / 2 - 20,
				20, I18n.format("enchCrack.addInfo"), () -> {
			RayTraceResult objMouseOver = Minecraft.getInstance().objectMouseOver;
			if (objMouseOver.type == RayTraceResult.Type.BLOCK) {
				EnchantmentCracker.addEnchantmentSeedInfo(Minecraft.getInstance().world, objMouseOver.getBlockPos(),
						(ContainerEnchantment) inventorySlots);
				manipulateButton.enabled = inventorySlots.getSlot(0).getHasStack() && EnchantmentCracker.isCracked();
			}
		}));
		manipulateButton = addButton(new GuiSimpleButton(1, width / 2 + xSize / 2 + 10, height / 2 + ySize / 2 + 5, width / 2 - xSize / 2 - 20, 20, I18n.format("enchCrack.manipulate"),
				() -> {
					Predicate<List<EnchantmentData>> predicate = l -> true;
					for (EnchantmentInstance ench : enchantments) {
				if (!wantedButtons.get(ench).enabled) {
					predicate = predicate.and(l -> l.contains(ench));
				} else if (!unwantedButtons.get(ench).enabled) {
					predicate = predicate.and(l -> !l.contains(ench));
				}
			}
					EnchantmentManipulationPlan plan = EnchantmentCracker.makeEnchantmentManipulationPlan(inventorySlots.getSlot(0).getStack().getItem(), predicate);
					if (plan != null && plan.getType() == EnchantmentManipulationPlan.Type.THROW_ITEMS) {
				Minecraft.getInstance().displayGuiScreen(new GuiYesNo((result, id) -> {
					Minecraft.getInstance().displayGuiScreen(this);
					if (result) {
						EnchantmentCracker.EnchantManipulationStatus status = EnchantmentCracker.manipulateEnchantments(plan);
						if (status != EnchantmentCracker.EnchantManipulationStatus.OK) {
							ITextComponent message = new TextComponentTranslation(status.getTranslation());
							message.getStyle().setColor(TextFormatting.RED);
							Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(message);
						}
					}
				}, I18n.format("enchCrack.insn.confirm.line1", plan.getItemsToThrow()), I18n.format("enchCrack.insn.confirm.line2"), 0));
			} else {
				EnchantmentCracker.EnchantManipulationStatus status = EnchantmentCracker.manipulateEnchantments(plan);
				if (status != EnchantmentCracker.EnchantManipulationStatus.OK) {
					ITextComponent message = new TextComponentTranslation(status.getTranslation());
					message.getStyle().setColor(TextFormatting.RED);
					Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(message);
				}
			}}));
		addInfoButton.enabled = false;
		manipulateButton.enabled = false;
		list = new GuiSlotEnchantment(Minecraft.getInstance(), width / 2 - xSize / 2, ySize,
				height / 2 - ySize / 2, height / 2 + ySize / 2, 30, width / 2 + xSize / 2 + 5, enchantments,
				wantedButtons, unwantedButtons, dontCareButtons);
	}

	@Override
	public void handleMouseClick(Slot slot, int slotId, int mouseButton, ClickType clickType) {
		super.handleMouseClick(slot, slotId, mouseButton, clickType);
		ItemStack stack = inventorySlots.getSlot(0).getStack();
		addInfoButton.enabled = !stack.isEmpty() && !stack.isEnchanted()
				&& stack.getItem().getItemEnchantability() > 0;
		manipulateButton.enabled = addInfoButton.enabled && EnchantmentCracker.isCracked();
		recalculateEnchantments();
	}

	private void recalculateEnchantments() {
		enchantments.clear();

		ItemStack stack = inventorySlots.getSlot(0).getStack();
		int enchantability = stack.getItem().getItemEnchantability();
		if (stack.isEmpty()) {
			return;
		}
		if (enchantability == 0) {
			return;
		}
		if (!EnchantmentHelper.getEnchantments(stack).isEmpty()) {
			return;
		}

		int minLevel = 2;
		int maxLevel = 31 + enchantability / 4 + enchantability / 4;
		maxLevel = Math.round(maxLevel + maxLevel * 0.15f);

		for (Enchantment enchantment : IRegistry.ENCHANTMENT) {
			if (!enchantment.isTreasureEnchantment()
					&& (enchantment.type.canEnchantItem(stack.getItem()) || stack.getItem() == Items.BOOK)) {
				for (int enchLvl = enchantment.getMinLevel(); enchLvl <= enchantment.getMaxLevel(); enchLvl++) {
					if (maxLevel >= enchantment.getMinEnchantability(enchLvl)
							&& minLevel <= enchantment.getMaxEnchantability(enchLvl)) {
						EnchantmentInstance ench = new EnchantmentInstance(enchantment, enchLvl);
						enchantments.add(ench);
						if (!wantedButtons.containsKey(ench)) {
							wantedButtons.put(ench, new GuiSimpleButton(0, 0, 0, 15, 15, "Y"));
							unwantedButtons.put(ench, new GuiSimpleButton(0, 0, 0, 15, 15, "N"));
							GuiButton dontCareButton = new GuiSimpleButton(0, 0, 0, 15, 15, "DC");
							dontCareButton.enabled = false;
							dontCareButtons.put(ench, dontCareButton);
						}
					}
				}
			}
		}
	}

}
