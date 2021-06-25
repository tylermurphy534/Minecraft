package net.tylermurphy.Minecraft.Util;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Scene.Camera;
import net.tylermurphy.Minecraft.Scene.Player;
import net.tylermurphy.Minecraft.Scene.Scene;

public class SceneCreator {

	public static void createScene() {
		
		Scene.currentScene = new Scene();
		
		Camera camera = (Camera) ResourceManager.loadObject("playerdata/", "temp.camera");
		if(camera!=null) {
			Scene.currentScene.camera = camera;
		} else{
			Scene.currentScene.addCamera(new Camera());
		}
		
		NBT worldData = (NBT) ResourceManager.loadObject("", "world.dat");
		if(worldData == null) {
			Chunk.SEED = new Random().nextInt(1000000000);
			Scene.currentScene.world_origin = new Vector2f(0,0);
		} else {
			Chunk.SEED = worldData.INTS.get("SEED");
			float wx = worldData.FLOATS.get("OriginX");
			float wz = worldData.FLOATS.get("OriginZ");
			Scene.currentScene.world_origin = new Vector2f(wx,wz);
		}
		Player player = (Player) ResourceManager.loadObject("playerdata/", "temp.player");
		if(player!=null) {
			Scene.currentScene.player = player;
		} else{
			Scene.currentScene.createPlayer(0, Chunk.getHighestBlock(0, 0)+1, 0, 0f, 0f, 0f, 0.6f);
		}
		
	}
	
}
	
