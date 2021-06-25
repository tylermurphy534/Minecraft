package net.tylermurphy.Minecraft.Input;

import static net.tylermurphy.Minecraft.UI.UIMaster.*;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Chunk.Cube;
import net.tylermurphy.Minecraft.Scene.Scene;
import net.tylermurphy.Minecraft.Util.Display;
import net.tylermurphy.Minecraft.Util.Flags;
import net.tylermurphy.Minecraft.Util.MousePicker;

public class GameInput implements IInput {

	byte blockId = 1;
	
	public void keyPressed(int keyCode) {
		switch(keyCode) {
			case GLFW.GLFW_KEY_ESCAPE: 
				Flags.actionForceClose = true; break;
			case GLFW.GLFW_KEY_F3:
				bindUI(1);
				setEnabled(!isEnabled());
				break;
			case GLFW.GLFW_KEY_EQUAL:
				while(true) {
					blockId++;
					if(blockId > (byte) (Cube.cubes.length-1)) blockId = 0;
					if(Cube.cubes[blockId].palceable == true) break;
				}
				bindUI(1);
				getText("block").setText("Block Selected: minecraft:"+Cube.cubes[blockId].name);
				break;
			case GLFW.GLFW_KEY_MINUS:
				while(true) {
					blockId--;
					if(blockId < 0) blockId = (byte) (Cube.cubes.length-1);
					if(Cube.cubes[blockId].palceable == true) break;
				}
				bindUI(1);
				getText("block").setText("Block Selected: minecraft:"+Cube.cubes[blockId].name);
				break;
			case GLFW.GLFW_KEY_F2:
				Scene.currentScene.player.isFlying = !Scene.currentScene.player.isFlying;
				break;
			case GLFW.GLFW_KEY_F11:
				Display.setFullScreen(!Display.fullscreen);
				break;
			case GLFW.GLFW_KEY_K:
				if(Scene.currentScene.player.isDead) {
					Scene.currentScene.player.isDead = false;
					Scene.currentScene.player.health = 20;
					Scene.currentScene.player.setPosition(new Vector3f(0,Chunk.getHighestBlock(0, 0)+1,0));
					bindUI(0);
					getText("dead").setEnabled(false);
					getImage("crosshair").setEnabled(true);
				}
				break;
		}
	}

	public void keyRelesed(int keyCode) {}

	public void mousePressed(int mouseButton) {
		switch(mouseButton) {
		case GLFW.GLFW_MOUSE_BUTTON_1:
			if(MousePicker.breakPos!=null)
				Chunk.setBlock((int)MousePicker.breakPos.x,(int)MousePicker.breakPos.y,(int)MousePicker.breakPos.z, Cube.AIR);
			break;
		case GLFW.GLFW_MOUSE_BUTTON_2:
			if(MousePicker.placePos!=null && !Scene.currentScene.player.collides(MousePicker.placePos.x,MousePicker.placePos.y,MousePicker.placePos.z))
				Chunk.setBlock((int)MousePicker.placePos.x,(int)MousePicker.placePos.y,(int)MousePicker.placePos.z, blockId);
			break;
		}
	}

	public void mouseRelesed(int mouseButton) {}

	public void charAction(char c) {

	}
	
}
