package com.Minecraft.World;
import java.util.Random;

import com.Minecraft.Chunk.Chunk;
import com.Minecraft.Scene.Camera;
import com.Minecraft.Scene.Player;
import com.Minecraft.Scene.Scene;

public class SceneCreator {

	public static void createScene() {
		
		Camera camera = ResourceManager.loadCamera("test");
		if(camera!=null) {
			Scene.camera = camera;
		} else{
			Scene.addCamera(new Camera());
		}
		
		if(!ResourceManager.loadWorldData("test")) {
			Chunk.SEED = new Random().nextInt(1000000000);
		}
		
		Player player = ResourceManager.loadPlayer("test");
		if(player!=null) {
			Scene.player = player;
		} else{
			Chunk c = ResourceManager.loadChunk("test", 0, 0);
			if(c == null) {
				c = new Chunk(0,0);
				c.generate();
			}
			Scene.chunks.add(c);
			Scene.createPlayer(0, Chunk.getHighestBlock(0, 0)+1, 0, 0f, 0f, 0f, 0.6f);
		}
		
	}
	
}
	
