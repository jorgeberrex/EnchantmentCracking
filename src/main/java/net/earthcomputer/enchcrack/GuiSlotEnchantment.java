package net.earthcomputer.enchcrack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

public class GuiSlotEnchantment extends GuiSlot {

	private List<EnchantmentInstance> enchantments;
	public Map<EnchantmentInstance, GuiButton> wantedButtons;
	public Map<EnchantmentInstance, GuiButton> unwantedButtons;
	public Map<EnchantmentInstance, GuiButton> dontCareButtons;

	public GuiSlotEnchantment(Minecraft mc, int width, int height, int top, int bottom, int slotHeight, int left,
			List<EnchantmentInstance> enchantments,
            Map<EnchantmentInstance, GuiButton> wantedButtons,
			Map<EnchantmentInstance, GuiButton> unwantedButtons,
			Map<EnchantmentInstance, GuiButton> dontCareButtons) {
		super(mc, width, height, top, bottom, slotHeight);
		setSlotXBoundsFromLeft(left);
		this.enchantments = enchantments;
		this.wantedButtons = wantedButtons;
		this.unwantedButtons = unwantedButtons;
		this.dontCareButtons = dontCareButtons;
	}

	@Override
	protected int getSize() {
		return enchantments.size();
	}

	@Override
	public boolean mouseClicked(int slotIndex, int button, double mouseX, double mouseY) {
		EnchantmentInstance ench = enchantments.get(slotIndex);
		GuiButton wantedButton = wantedButtons.get(ench);
		GuiButton unwantedButton = unwantedButtons.get(ench);
		GuiButton dontCareButton = dontCareButtons.get(ench);
		SoundHandler sh = Minecraft.getInstance().getSoundHandler();
		if (wantedButton.isMouseOver()) {
			wantedButton.enabled = false;
			unwantedButton.enabled = true;
			dontCareButton.enabled = true;
			wantedButton.playPressSound(sh);
		}
		if (unwantedButton.isMouseOver()) {
			wantedButton.enabled = true;
			unwantedButton.enabled = false;
			dontCareButton.enabled = true;
			unwantedButton.playPressSound(sh);
		}
		if (dontCareButton.isMouseOver()) {
			wantedButton.enabled = true;
			unwantedButton.enabled = true;
			dontCareButton.enabled = false;
			dontCareButton.playPressSound(sh);
		}
		return super.mouseClicked(slotIndex, button, mouseX, mouseY);
	}

    @Override
	protected boolean isSelected(int slotIndex) {
		return false;
	}

	@Override
	protected void drawBackground() {
	}

	@Override
	public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		Minecraft mc = Minecraft.getInstance();
		GuiScreen gui = mc.currentScreen;
		GL11.glScissor(left * mc.mainWindow.getWidth() / gui.width, top * mc.mainWindow.getHeight() / gui.height,
				width * mc.mainWindow.getWidth() / gui.width, height * mc.mainWindow.getHeight() / gui.height);
		super.drawScreen(mouseXIn, mouseYIn, partialTicks);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	@Override
	protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn,
			float partialTicks) {
		EnchantmentInstance ench = enchantments.get(slotIndex);
		Minecraft.getInstance().fontRenderer.drawString(ench.enchantment.func_200305_d(ench.enchantmentLevel).getString(),
				xPos + 5, yPos + 5, 0xffffff);

		GuiButton wantedButton = wantedButtons.get(ench);
		wantedButton.x = xPos + width - 66;
		wantedButton.y = yPos + 16;
		GuiButton unwantedButton = unwantedButtons.get(ench);
		unwantedButton.x = xPos + width - 46;
		unwantedButton.y = yPos + 16;
		GuiButton dontCareButton = dontCareButtons.get(ench);
		dontCareButton.x = xPos + width - 26;
		dontCareButton.y = yPos + 16;
		wantedButton.render(mouseXIn, mouseYIn, partialTicks);
		unwantedButton.render(mouseXIn, mouseYIn, partialTicks);
		dontCareButton.render(mouseXIn, mouseYIn, partialTicks);
	}

	@Override
	public int getScrollBarX() {
		return left + width - 10;
	}

	@Override
	public int getListWidth() {
		return width;
	}

	@Override
	public void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
	}

}