package com.Minecraft.Core;

import org.lwjgl.openal.AL11;

import com.Minecraft.Audio.SoundManager;
import com.Minecraft.Data.Texture;
import com.Minecraft.Render.MainRenderer;
import com.Minecraft.Util.Display;
import com.Minecraft.Util.Flags;
import com.Minecraft.Util.Input;

public class Engine {
	
	private static Game game;
	
	private static long currentTime = System.nanoTime();
	private static long nanoSeconendsPassed;
	private static int tps,ticks;
	private static MainRenderer renderer;
	
	public static void main(String args[]) {
		
		Display.create(1280, 720, "Minecraft", Texture.loadRawTextureData("icon"));
		Input.addInput(new GameInput());
		SoundManager.init();
		AL11.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);

		game = new Game();
		game.init();
		renderer = new MainRenderer();
		
		while(!Display.closed() && Flags.actionForceClose == false) {
			
			long lastTime = currentTime;
			currentTime = System.nanoTime();
			nanoSeconendsPassed += currentTime - lastTime;
			
			Display.update();
			
			game.onFrame();
			if(nanoSeconendsPassed >= 50000000) {
				tps = Math.round((50000000f/nanoSeconendsPassed)*20);
				game.onTick();
				nanoSeconendsPassed -= 50000000;
				if(nanoSeconendsPassed >= 1000000000)
					nanoSeconendsPassed = 0;
				ticks++;
			}
			renderer.update();
			Display.swapBuffers();
			
		}
		
		game.lastFrame();
		renderer.update();
		Display.swapBuffers();
		
		renderer.close();
		game.stop();
		
		SoundManager.cleanUp();
		Display.close();
		
		System.exit(0);
		
	}
	
	public static int getTPS() {
		return tps;
	}
	
	public static int getTicks() {
		return ticks;
	}
	
}
