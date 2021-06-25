package net.tylermurphy.Minecraft.Scripts; 

import java.util.Random;

import net.tylermurphy.Minecraft.Audio.Sound;
import net.tylermurphy.Minecraft.Audio.SoundManager;
import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Scene.Scene;
import net.tylermurphy.Minecraft.Tick.TickManager;
import net.tylermurphy.Minecraft.Util.ChunkLoader;
import net.tylermurphy.Minecraft.Util.NBT;
import net.tylermurphy.Minecraft.Util.ResourceManager;
import net.tylermurphy.Minecraft.Util.SceneCreator;

public class GameScript extends Script {
	
	Sound sound;
	String[] music = {"calm1","calm2","calm3","hal1","hal2","hal3","hal4","nuance1","nuance2","piano1","piano2","piano3"};
	
	public void Init() {
		SceneCreator.createScene();
		loadNextSound();
	}
	
	public void Update() {
		ChunkLoader.run(true);
	}

	public void Tick() {
		if(!sound.isPlaying()) loadNextSound();
		for(Chunk c: Scene.currentScene.chunks.values()) {
			if(c==null) continue;
			if(!c.wasModifiedLast)
				c.wasModifiedLast = c.wasModified;
			if(c.wasModified)
				c.updateMesh();
			c.wasModified = false;
		}
		TickManager.doTick();
	}
	
	public void loadNextSound() {
		Random random = new Random();
		int i = random.nextInt(music.length);
		sound = SoundManager.loadSound(music[i]);
		sound.setLooping(false);
		sound.play();
	}
	
	public void Stop() {
		sound.stop();
		
		ChunkLoader.save("test");
		
		ResourceManager.saveObject("playerdata/", "temp.player", Scene.currentScene.player);
		ResourceManager.saveObject("playerdata/", "temp.camera", Scene.currentScene.camera);
		NBT worldData = new NBT();
		worldData.INTS.put("SEED", Chunk.SEED);
		worldData.FLOATS.put("OriginX", Scene.currentScene.world_origin.x);
		worldData.FLOATS.put("OriginZ", Scene.currentScene.world_origin.y);
		ResourceManager.saveObject("", "world.dat", worldData);
	}
	
}  