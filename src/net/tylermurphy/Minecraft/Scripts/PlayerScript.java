package net.tylermurphy.Minecraft.Scripts;

import static net.tylermurphy.Minecraft.UI.UIMaster.bindUI;
import static net.tylermurphy.Minecraft.UI.UIMaster.getImage;
import static net.tylermurphy.Minecraft.UI.UIMaster.getText;
import static net.tylermurphy.Minecraft.UI.UIMaster.setEnabled;

import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Main;
import net.tylermurphy.Minecraft.Scene.Scene;

public class PlayerScript extends Script {

	int last_height = 0;

	public void Update() {
		if(Scene.currentScene.player.isInAir && !Scene.currentScene.player.isFlying && !Scene.currentScene.player.isSwiming) Scene.currentScene.player.isFalling = true;
		else Scene.currentScene.player.isFalling = false;
		if(Scene.currentScene.player.isSwiming || Scene.currentScene.player.isFlying) last_height = (int) Scene.currentScene.player.getGlobalPosition().y;
		
		if(Scene.currentScene.player.isFalling) {
			if(!Scene.currentScene.player.wasFalling)
				last_height = (int) Scene.currentScene.player.getGlobalPosition().y;
			Scene.currentScene.player.wasFalling = true;
		} else if(Scene.currentScene.player.wasFalling == true) {
			int current_height = (int) Scene.currentScene.player.getGlobalPosition().y;
			float height_fell = last_height - current_height;
			if(height_fell > 3)
				Scene.currentScene.player.health -= height_fell - 3;
			Scene.currentScene.player.health = Math.max(Scene.currentScene.player.health,0);
			Scene.currentScene.player.wasFalling = false;
		}
		
		if(Scene.currentScene.player.health <= 0) {
			Scene.currentScene.player.isDead = true;
			bindUI(1);
			setEnabled(false);
			bindUI(0);
			getText("dead").setEnabled(true);
			getImage("crosshair").setEnabled(false);
		}
		
		Vector3f position = Scene.currentScene.player.getPosition();
		if((int)position.x>=256) {
			Scene.currentScene.world_origin.x += 256;
			Scene.currentScene.player.setPosition(new Vector3f(position.x - 256 ,position.y,position.z));
		} else if((int)position.x<=-256) {
			Scene.currentScene.world_origin.x -= 256;
			Scene.currentScene.player.setPosition(new Vector3f(position.x + 256 ,position.y,position.z));
		}
		
		if((int)position.z>=256) {
			Scene.currentScene.world_origin.y += 256;
			Scene.currentScene.player.setPosition(new Vector3f(position.x,position.y,position.z - 256));
		} else if((int)position.z<=-256) {
			Scene.currentScene.world_origin.y -= 256;
			Scene.currentScene.player.setPosition(new Vector3f(position.x,position.y,position.z + 256));
		}
		if(!Scene.currentScene.player.isDead && Main.getTicks() % 80 == 0 && Scene.currentScene.player.health < 20) Scene.currentScene.player.health++;
	}

}
