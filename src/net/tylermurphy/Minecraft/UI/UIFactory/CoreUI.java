package net.tylermurphy.Minecraft.UI.UIFactory;

import static net.tylermurphy.Minecraft.UI.UIMaster.add;
import static net.tylermurphy.Minecraft.UI.UIMaster.createUI;

import net.tylermurphy.Minecraft.UI.UIImage;
import net.tylermurphy.Minecraft.UI.UIText;

public class CoreUI {
	
	public static void initCoreUI() {
		
		createUI(0);
		UIImage image = new UIImage();
		image.setTexture(UIStore.TEXTURES.get("crosshair"));
		image.setPosition(.5f, -15f, .5f, -15f);
		image.setSize(0, 30, 0, 30);
		image.setKey("crosshair");
		UIImage heart1 = new UIImage();
		heart1.setTexture(UIStore.TEXTURES.get("heart_full_texture"));
		heart1.setPosition(0, 20, 1f, -20);
		heart1.setSize(0, 20, 0, 20);
		heart1.setKey("heart1");
		UIImage heart2 = new UIImage();
		heart2.setTexture(UIStore.TEXTURES.get("heart_full_texture"));
		heart2.setPosition(0, 50, 1f, -20);
		heart2.setSize(0, 20, 0, 20);
		heart2.setKey("heart2");
		UIImage heart3 = new UIImage();
		heart3.setTexture(UIStore.TEXTURES.get("heart_full_texture"));
		heart3.setPosition(0, 80, 1f, -20);
		heart3.setSize(0, 20, 0, 20);
		heart3.setKey("heart3");
		UIImage heart4 = new UIImage();
		heart4.setTexture(UIStore.TEXTURES.get("heart_full_texture"));
		heart4.setPosition(0, 110, 1f, -20);
		heart4.setSize(0, 20, 0, 20);
		heart4.setKey("heart4");
		UIImage heart5 = new UIImage();
		heart5.setTexture(UIStore.TEXTURES.get("heart_full_texture"));
		heart5.setPosition(0, 140, 1f, -20);
		heart5.setSize(0, 20, 0, 20);
		heart5.setKey("heart5");
		UIImage heart6 = new UIImage();
		heart6.setTexture(UIStore.TEXTURES.get("heart_full_texture"));
		heart6.setPosition(0, 170, 1f, -20);
		heart6.setSize(0, 20, 0, 20);
		heart6.setKey("heart6");
		UIImage heart7 = new UIImage();
		heart7.setTexture(UIStore.TEXTURES.get("heart_full_texture"));
		heart7.setPosition(0, 200, 1f, -20);
		heart7.setSize(0, 20, 0, 20);
		heart7.setKey("heart7");
		UIImage heart8 = new UIImage();
		heart8.setTexture(UIStore.TEXTURES.get("heart_full_texture"));
		heart8.setPosition(0, 230, 1f, -20);
		heart8.setSize(0, 20, 0, 20);
		heart8.setKey("heart8");
		UIImage heart9 = new UIImage();
		heart9.setTexture(UIStore.TEXTURES.get("heart_full_texture"));
		heart9.setPosition(0, 260, 1f, -20);
		heart9.setSize(0, 20, 0, 20);
		heart9.setKey("heart9");
		UIImage heart10 = new UIImage();
		heart10.setTexture(UIStore.TEXTURES.get("heart_full_texture"));
		heart10.setPosition(0, 290, 1f, -20);
		heart10.setSize(0, 20, 0, 20);
		heart10.setKey("heart10");
		UIText dead = new UIText("You have died, press k to respawn.",1,UIStore.FONTS.get("yugothic"),1000,false);
		dead.setPosition(0.4f, 0, .5f, -20);
		dead.setKey("dead");
		dead.setEnabled(false);
		add(image,heart1,heart2,heart3,heart4,heart5,heart6,heart7,heart8,heart9,heart10,dead);
	}
	
}
