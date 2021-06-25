package net.tylermurphy.Minecraft;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL11;

import net.tylermurphy.Minecraft.Audio.SoundManager;
import net.tylermurphy.Minecraft.Chunk.Cube;
import net.tylermurphy.Minecraft.Input.GameInput;
import net.tylermurphy.Minecraft.Input.Input;
import net.tylermurphy.Minecraft.Render.MainRenderer;
import net.tylermurphy.Minecraft.Render.Data.Texture;
import net.tylermurphy.Minecraft.Scripts.*;
import net.tylermurphy.Minecraft.Util.ChunkLoader;
import net.tylermurphy.Minecraft.Util.Display;
import net.tylermurphy.Minecraft.Util.Flags;

class Test {
	int value = 0;
	Test test;
}

public class Main {
	
	private static long currentTime = System.nanoTime();
	private static long nanoSeconendsPassed;
	private static int tps,ticks; 
	private static MainRenderer renderer;
	
	public static String currentWorld = "test";
	
	private static List<Script> scripts = new ArrayList<Script>();
	
	public static void main(String args[]) {
		
		Display.create(1280, 720, "Minecraft", Texture.loadRawTextureData("icon"));
		Input.addInput(new GameInput());
		SoundManager.init();
		AL11.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);

		scripts.add(new GameScript());
		scripts.add(new PlayerScript());
		scripts.add(new UIScript());
		
		for(Script script : scripts) script.Init();
		
		renderer = new MainRenderer();
		
		Cube.init();
		
		ChunkLoader.run(false);
		
		while(!Display.closed() && Flags.actionForceClose == false) {
			
			long lastTime = currentTime;
			currentTime = System.nanoTime();
			nanoSeconendsPassed += currentTime - lastTime;
			
			Display.update();
			
			for(Script script : scripts) script.Update();

			if(nanoSeconendsPassed >= 50000000) {
				tps = Math.round((50000000f/nanoSeconendsPassed)*20);
				for(Script script : scripts) script.Tick();
				nanoSeconendsPassed -= 50000000;
				if(nanoSeconendsPassed >= 5000000000L)
					nanoSeconendsPassed = 0;
				ticks++;
			}
			renderer.update();
			Display.swapBuffers();
			
		}
		
		for(Script script : scripts) script.End();
		
		renderer.update();
		Display.swapBuffers();
		renderer.close();
		
		for(Script script : scripts) script.Stop();
		
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
