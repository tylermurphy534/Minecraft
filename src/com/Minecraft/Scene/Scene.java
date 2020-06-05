package com.Minecraft.Scene;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.Minecraft.Chunk.Chunk;

import com.Minecraft.Scene.Scene;

public class Scene {

	public static List<Chunk> chunks = new ArrayList<Chunk>();;
	public static List<Entity> global_entities = new ArrayList<Entity>();;
	
	public static Camera camera;
	public static Player player;

	public static Vector2f world_origin;
	
	public static void createPlayer(float px,float py,float pz,float rx,float ry,float rz,float scale) {
		player = new Player(null,px,py,pz,rx,ry,rz, scale);
	}
	
	public static void addCamera(Camera camera) {
		Scene.camera = camera;
	}
	
}