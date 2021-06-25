package net.tylermurphy.Minecraft.UI.UIFactory;

import static net.tylermurphy.Minecraft.UI.UIMaster.*;

import net.tylermurphy.Minecraft.UI.UIText;

public class SavingUI {

	public static void initSavingUI() {
		createUI(2);
		UIText saving = new UIText("Saving...",1,UIStore.FONTS.get("yugothic"),1000,false);
		saving.setPosition(0.5f, -50, 0.5f, -20);
		add(saving);
		setEnabled(false);
	}
	
}
