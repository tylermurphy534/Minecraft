package com.Minecraft.Core; 

import static com.Minecraft.UI.UIMaster.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.Minecraft.Audio.Sound;
import com.Minecraft.Audio.SoundManager;
import com.Minecraft.Chunk.Chunk;
import com.Minecraft.Data.Texture;
import com.Minecraft.Scene.Scene;
import com.Minecraft.UI.UIFont;
import com.Minecraft.UI.UIImage;
import com.Minecraft.UI.UIText;
import com.Minecraft.Util.Display;
import com.Minecraft.World.ChunkLoader;
import com.Minecraft.World.ResourceManager;
import com.Minecraft.World.SceneCreator;

public class Game{
	
	Sound sound;
	String[] music = {"calm1","calm2","calm3","hal1","hal2","hal3","hal4","nuance1","nuance2","piano1","piano2","piano3"};
	int crosshair = Texture.loadTexture("crosshair");
	static UIFont font;
	static int heart_full_texture,heart_half_texture,heart_empty_texture;
	int last_height = 0;
	
	public void init() {
		
		SceneCreator.createScene();
		loadNextSound();
		
		font = new UIFont(Texture.loadFontAtlas("yugothic"),"yugothic");
		
		heart_full_texture = Texture.loadTexture("heart_full");
		heart_half_texture = Texture.loadTexture("heart_half");
		heart_empty_texture = Texture.loadTexture("heart_empty");
		
		initCoreUI();
		initF3UI();
		
	}
	
	public void onFrame() {
		ChunkLoader.run();
		bindUI(0);
		for(int i=1;i<=10;i++) {
			UIImage heart = getImage("heart" + i);
			if(Scene.player.health >= i*2) {
				heart.setTexture(heart_full_texture);
				continue;
			} else if(Scene.player.health >= i*2-1) {
				heart.setTexture(heart_half_texture);
				continue;
			} else {
				heart.setTexture(heart_empty_texture);
			}
		}
		if(Scene.player.isInAir && !Scene.player.isFlying && !Scene.player.isSwiming) Scene.player.isFalling = true;
		else Scene.player.isFalling = false;
		if(Scene.player.isSwiming || Scene.player.isFlying) last_height = (int) Scene.player.getPosition().y;
		
		if(Scene.player.isFalling) {
			if(!Scene.player.wasFalling)
				last_height = (int) Scene.player.getPosition().y;
			Scene.player.wasFalling = true;
		} else if(Scene.player.wasFalling == true) {
			int current_height = (int) Scene.player.getPosition().y;
			float height_fell = last_height - current_height;
			if(height_fell > 3)
				Scene.player.health -= height_fell - 3;
			Scene.player.health = Math.max(Scene.player.health,0);
			Scene.player.wasFalling = false;
		}
		
		if(Scene.player.health <= 0) {
			Scene.player.isDead = true;
			bindUI(1);
			setEnabled(false);
			bindUI(0);
			getText("dead").setEnabled(true);
			getImage("crosshair").setEnabled(false);
		}
		
		if((int)Scene.player.getPosition().x>=256) {
			Scene.world_origin.x += 256;
			Scene.player.getPosition().x -= 256;
		} else if((int)Scene.player.getPosition().x<=-256) {
			Scene.world_origin.x -= 256;
			Scene.player.getPosition().x += 256;
		}
		
		if((int)Scene.player.getPosition().z>=256) {
			Scene.world_origin.y += 256;
			Scene.player.getPosition().z -= 256;
		} else if((int)Scene.player.getPosition().z<=-256) {
			Scene.world_origin.y -= 256;
			Scene.player.getPosition().z += 256;
		}
	}
	
	private boolean foundGrass(List<Vector3f> changed, int x, int y, int z)
	{
		for(int a = x-1; a<x+2; a++)
			for(int b = y-1; b<y+2; b++)
				for(int c = z-1; c<z+2; c++) {
					boolean cont = false;
					if(x == a && c == z)
						cont = true;
					for(Vector3f v : changed)
						if(v.x == a && v.y == b && v.z == c)
							cont = true;
					if(cont) continue;
					if(Chunk.getBlock(a, b, c) == 0)
						return true;
				}
		return false;
	}
	
	public void onTick() {
		if(!sound.isPlaying()) loadNextSound();
		String position = "Position: "+(int)(Scene.player.getPosition().getX()+Scene.world_origin.x)+","+(int)Scene.player.getPosition().getY()+","+(int)(Scene.player.getPosition().getZ()+Scene.world_origin.y); 
		String rotation = "Rotation: "+(int)Scene.player.getRotation().x+","+(int)Scene.player.getRotation().y+","+(int)Scene.player.getRotation().z; 
		bindUI(1);
		getText("position").setText(position);
		getText("rotation").setText(rotation);
		getText("freemem").setText("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
		for(Chunk c:Scene.chunks) {
			if(!c.wasModifiedLast)
				c.wasModifiedLast = c.wasModified;
			if(c.wasModified)
				c.updateMesh();
			c.wasModified = false;
		}
		if(Engine.getTicks() % 20 == 0) {
			getText("fps").setText("FPS: " + (int)(1/Display.getFrameTimeSeconds()));
			getText("tps").setText("TPS: " + Engine.getTPS());
		}
		if(Engine.getTicks() % 10 == 0) {
			for(Chunk c : Scene.chunks) {
				for(int x=0;x<16;x++) {
					for(int y=0;y<256;y++) {
						for(int z=0;z<16;z++) {
							if(c.cubes[x][y][z] == 17) {
								if(Chunk.getBlock(x-1+c.gridX*16, y, z+c.gridZ*16) == -1) Chunk.setBlock(x-1+c.gridX*16, y, z+c.gridZ*16, (byte) 17);
								if(Chunk.getBlock(x+1+c.gridX*16, y, z+c.gridZ*16) == -1) Chunk.setBlock(x+1+c.gridX*16, y, z+c.gridZ*16, (byte) 17);
								if(Chunk.getBlock(x+c.gridX*16, y-1, z+c.gridZ*16) == -1) Chunk.setBlock(x+c.gridX*16, y-1, z+c.gridZ*16, (byte) 17);
								if(Chunk.getBlock(x+c.gridX*16, y, z-1+c.gridZ*16) == -1) Chunk.setBlock(x+c.gridX*16, y, z-1+c.gridZ*16, (byte) 17);
								if(Chunk.getBlock(x+c.gridX*16, y, z+1+c.gridZ*16) == -1) Chunk.setBlock(x+c.gridX*16, y, z+1+c.gridZ*16, (byte) 17);
							}
						}
					}
				}
			}
		}
		if(Engine.getTicks() % 200 == 0) {
			List<Vector3f> changed = new ArrayList<Vector3f>();
			for(Chunk c:Scene.chunks) { 
				if(c.wasModifiedLast == false)
					continue;
				c.wasModifiedLast = false;
				for(int x = 0; x<16; x++) {
					for(int y = 0; y < 256; y++) {
						for(int z = 0; z<16; z++) {
							if(c.cubes[x][y][z] == 1 && foundGrass(changed, x+16*c.gridX,y,z+16*c.gridZ) && (y != 255 && c.cubes[x][y+1][z] == -1)) {
								c.cubes[x][y][z] = 0;
								c.hasBeenModified = c.wasModified = true;
								changed.add(new Vector3f(x+16*c.gridX,y,z+16*c.gridZ));
							}
						}
					}
				}
			}
		}
		if(!Scene.player.isDead && Engine.getTicks() % 80 == 0 && Scene.player.health < 20) Scene.player.health++;
	}
	
	public void lastFrame() {
		createUI(2);
		UIText saving = new UIText("Saving...",1,font,1000,false);
		saving.setPosition(0.5f, -50, 0.5f, -20);
		add(saving);
		bindUI(0);
		setEnabled(false);
		bindUI(1);
		setEnabled(false);
	}
	
	public void loadNextSound() {
		Random random = new Random();
		int i = random.nextInt(music.length);
		sound = SoundManager.loadSound(music[i]);
		sound.setLooping(false);
		sound.play();
	}
	
	public void stop() {
		sound.stop();
		for(Chunk chunk : Scene.chunks) {
			ResourceManager.saveChunk("test", chunk);
		}
		ResourceManager.savePlayer("test", Scene.player);
		ResourceManager.saveCamera("test", Scene.camera);
		ResourceManager.saveWorldData("test");
	}
	
	private static void initF3UI() {
		createUI(1);
		setEnabled(false);
		
		String maxMem = "Max memory (bytes): " + Runtime.getRuntime().maxMemory();
		String freeMem = "Free memory (bytes): " + Runtime.getRuntime().freeMemory();
		String allocMem = "Allocated memory (bytes): " + Runtime.getRuntime().totalMemory();
		String cores = "Available processors (cores): "+ Runtime.getRuntime().availableProcessors();
		
		UIText maxMemText = new UIText(maxMem,1,font,1000,false);
		maxMemText.setPosition(0, 0, .125f, 0);
		UIText freeMemText = new UIText(freeMem,1,font,1000,false);
		freeMemText.setPosition(0, 0, .15f, 0);
		freeMemText.setKey("freemem");
		UIText allocMemText = new UIText(allocMem,1,font,1000,false);
		allocMemText.setPosition(0, 0, .175f, 0);
		UIText coresText = new UIText(cores,1,font,1000,false);
		coresText.setPosition(0, 0, .2f, .2f);
		
		UIText position = new UIText("",1,font,1000,false);
		position.setPosition(0, 0, 0, 0);
		position.setKey("position");
		UIText rotation = new UIText("",1,font,1000,false);
		rotation.setPosition(0, 0, .025f, 0);
		rotation.setKey("rotation");
		UIText fps = new UIText("",1,font,1000,false);
		fps.setPosition(0, 0, .05f, 0);
		fps.setKey("fps");
		UIText tps = new UIText("",1,font,1000,false);
		tps.setPosition(0, 0, 0.075f, 0);
		tps.setKey("tps");
		
		UIText block = new UIText("Block Selected: minecraft:dirt",1,font,1000,false);
		block.setPosition(0, 0, .25f, 0);
		block.setKey("block");
		
		add(maxMemText,freeMemText,allocMemText,coresText,position,rotation,fps,block,tps);
	}
	
	private static void initCoreUI() {
		createUI(0);
		UIImage image = new UIImage();
		image.setTexture(Texture.loadTexture("crosshair"));
		image.setPosition(.5f, -15f, .5f, -15f);
		image.setSize(0, 30, 0, 30);
		image.setKey("crosshair");
		UIImage heart1 = new UIImage();
		heart1.setTexture(heart_full_texture);
		heart1.setPosition(0, 20, 1f, -20);
		heart1.setSize(0, 20, 0, 20);
		heart1.setKey("heart1");
		UIImage heart2 = new UIImage();
		heart2.setTexture(heart_full_texture);
		heart2.setPosition(0, 50, 1f, -20);
		heart2.setSize(0, 20, 0, 20);
		heart2.setKey("heart2");
		UIImage heart3 = new UIImage();
		heart3.setTexture(heart_full_texture);
		heart3.setPosition(0, 80, 1f, -20);
		heart3.setSize(0, 20, 0, 20);
		heart3.setKey("heart3");
		UIImage heart4 = new UIImage();
		heart4.setTexture(heart_full_texture);
		heart4.setPosition(0, 110, 1f, -20);
		heart4.setSize(0, 20, 0, 20);
		heart4.setKey("heart4");
		UIImage heart5 = new UIImage();
		heart5.setTexture(heart_full_texture);
		heart5.setPosition(0, 140, 1f, -20);
		heart5.setSize(0, 20, 0, 20);
		heart5.setKey("heart5");
		UIImage heart6 = new UIImage();
		heart6.setTexture(heart_full_texture);
		heart6.setPosition(0, 170, 1f, -20);
		heart6.setSize(0, 20, 0, 20);
		heart6.setKey("heart6");
		UIImage heart7 = new UIImage();
		heart7.setTexture(heart_full_texture);
		heart7.setPosition(0, 200, 1f, -20);
		heart7.setSize(0, 20, 0, 20);
		heart7.setKey("heart7");
		UIImage heart8 = new UIImage();
		heart8.setTexture(heart_full_texture);
		heart8.setPosition(0, 230, 1f, -20);
		heart8.setSize(0, 20, 0, 20);
		heart8.setKey("heart8");
		UIImage heart9 = new UIImage();
		heart9.setTexture(heart_full_texture);
		heart9.setPosition(0, 260, 1f, -20);
		heart9.setSize(0, 20, 0, 20);
		heart9.setKey("heart9");
		UIImage heart10 = new UIImage();
		heart10.setTexture(heart_full_texture);
		heart10.setPosition(0, 290, 1f, -20);
		heart10.setSize(0, 20, 0, 20);
		heart10.setKey("heart10");
		UIText dead = new UIText("You have died, press k to respawn.",1,font,1000,false);
		dead.setPosition(0.4f, 0, .5f, -20);
		dead.setKey("dead");
		dead.setEnabled(false);
		add(image,heart1,heart2,heart3,heart4,heart5,heart6,heart7,heart8,heart9,heart10,dead);
	}
	
}  