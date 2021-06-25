package net.tylermurphy.Minecraft.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Scene.Scene;
import net.tylermurphy.Minecraft.Scene.Objects.Entity;

public class Scene {
	
	public static Scene currentScene;

	public HashMap<String,Chunk> chunks = new HashMap<String,Chunk>();;
	public List<Entity> global_entities = new ArrayList<Entity>();
	
	public static int LCGX=0,LCGZ=0;
	
	public Camera camera;
	public Player player;

	public Vector2f world_origin;
	
	public void createPlayer(float px,float py,float pz,float rx,float ry,float rz,float scale) {
		player = new Player(px,py,pz,rx,ry,rz, scale);
	}
	
	public void addCamera(Camera camera) {
		this.camera = camera;
	}

}