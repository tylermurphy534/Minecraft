package net.tylermurphy.Minecraft.Scripts;

import static net.tylermurphy.Minecraft.UI.UIMaster.bindUI;
import static net.tylermurphy.Minecraft.UI.UIMaster.getImage;
import static net.tylermurphy.Minecraft.UI.UIMaster.getText;
import static net.tylermurphy.Minecraft.UI.UIMaster.setEnabled;

import net.tylermurphy.Minecraft.Main;
import net.tylermurphy.Minecraft.Scene.Scene;
import net.tylermurphy.Minecraft.UI.UIImage;
import net.tylermurphy.Minecraft.UI.UIFactory.*;
import net.tylermurphy.Minecraft.Util.Display;

public class UIScript extends Script {

	public void Init() {
		UIStore.InitalizeStoreData();
		CoreUI.initCoreUI();
		F3UI.initF3UI();
		SavingUI.initSavingUI();
	}
	
	public void Update() {
		bindUI(0);
		for(int i=1;i<=10;i++) {
			UIImage heart = getImage("heart" + i);
			if(Scene.currentScene.player.health >= i*2) {
				heart.setTexture(UIStore.TEXTURES.get("heart_full_texture"));
				continue;
			} else if(Scene.currentScene.player.health >= i*2-1) {
				heart.setTexture(UIStore.TEXTURES.get("heart_half_texture"));
				continue;
			} else {
				heart.setTexture(UIStore.TEXTURES.get("heart_empty_texture"));
			}
		}
	}
	
	public void Tick() {
		String position = "Position: "+(int)(Scene.currentScene.player.getGlobalPosition().getX())+","+(int)Scene.currentScene.player.getGlobalPosition().getY()+","+(int)(Scene.currentScene.player.getGlobalPosition().getZ()); 
		String rotation = "Rotation: "+(int)Scene.currentScene.player.getRotation().x+","+(int)Scene.currentScene.player.getRotation().y+","+(int)Scene.currentScene.player.getRotation().z; 
		bindUI(1);
		getText("position").setText(position);
		getText("rotation").setText(rotation);
		getText("freemem").setText("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
		if(Main.getTicks() % 20 == 0) {
			getText("fps").setText("FPS: " + (int)(1/Display.getFrameTimeSeconds()));
			getText("tps").setText("TPS: " + Main.getTPS());
		}
	}
	
	public void End() {
		bindUI(0);
		setEnabled(false);
		bindUI(1);
		setEnabled(false);
		bindUI(2);
		setEnabled(true);
	}
	
}
