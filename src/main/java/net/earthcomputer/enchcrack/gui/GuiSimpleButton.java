package net.earthcomputer.enchcrack.gui;

import net.minecraft.client.gui.GuiButton;

public class GuiSimpleButton extends GuiButton {

    private Runnable callback;

    public GuiSimpleButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this(buttonId, x, y, widthIn, heightIn, buttonText, null);
    }

    public GuiSimpleButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, Runnable callback) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.callback = callback;
    }

    public void setOnClick(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (callback != null) callback.run();
    }
}
