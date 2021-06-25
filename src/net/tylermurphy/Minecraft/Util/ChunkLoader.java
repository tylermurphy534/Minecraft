package net.tylermurphy.Minecraft.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Render.Data.Vao;
import net.tylermurphy.Minecraft.Scene.Scene;

public class ChunkLoader {
	
	private static ArrayList<String> keys_to_be_removed = new ArrayList<String>();

	private static HashMap<String,NBT> loaded_regions = new HashMap<String,NBT>();
	
	public static void run(boolean throttle) {
		try {
			int cx = 0,cz = 0;
			int renderDistance = Chunk.RENDER_DISTANCE;
			cx = (int) ((Scene.currentScene.player.getGlobalPosition().x)/16);
			cz = (int) ((Scene.currentScene.player.getGlobalPosition().z)/16);
			
			keys_to_be_removed.clear();
			for(Map.Entry<String,Chunk> entry : Scene.currentScene.chunks.entrySet()) {
				Chunk c = entry.getValue();
				if(c.gridX < cx-renderDistance || c.gridX > cx+renderDistance || c.gridZ < cz-renderDistance || c.gridZ > cz+renderDistance) { 
					
					int x = c.gridX; int z = c.gridZ;
					int regionX = x > 0 ? x / 16 : (x - 16) / 16;
					int regionZ = z > 0 ? z / 16 : (z - 16) / 16;
					NBT region = loaded_regions.get(getRegionKey(regionX,regionZ));
					byte[] data = ResourceManager.flattenArray(c.cubes);
					region.BYTE_ARRAYS.put(getChunkKey(x,z), data);
					region.BOOLEANS.put(getChunkKey(x,z)+":wasModified",c.wasModified);
					region.BOOLEANS.put(getChunkKey(x,z)+":wasModifiedLast",c.wasModifiedLast);
					loaded_regions.put(getRegionKey(regionX,regionZ), region);
					
					if(c.opaque_mesh != null)
						Vao.disposeVAO(c.opaque_mesh.getMesh().getVao().getVaoID());
					c.opaque_mesh = null;
					if(c.transparent_mesh != null)
						Vao.disposeVAO(c.transparent_mesh.getMesh().getVao().getVaoID());
					c.transparent_mesh = null;
					c.cubes = null;
					c.entities = null;
					keys_to_be_removed.add(entry.getKey());
				}
			}
			for(String key : keys_to_be_removed) {
				Scene.currentScene.chunks.remove(key);
			}
			for(int x=cx-renderDistance; x<cx+renderDistance;x++) {
				for(int z=cz-renderDistance; z<cz+renderDistance;z++) {
					boolean found = false;
					Chunk c = Scene.currentScene.chunks.get(x+":"+z);
					if(c != null) found = true;
					if(!found) {
						Chunk chunk = loadChunk("test", x, z);
						if(chunk == null) {
							chunk = new Chunk(x,z);
							chunk.generate();
						}
						
						Scene.currentScene.chunks.put(chunk.gridX+":"+chunk.gridZ,chunk);
						
						if(throttle) {
							chunk.updateMesh();
							update(x+1,z);
							update(x-1,z);
							update(x,z+1);
							update(x,z-1);
							return;
						}
					}
				}
			}
			if(!throttle) {
				for(Chunk c : Scene.currentScene.chunks.values()) {
					c.updateMesh();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void save(String world) {
		for(Chunk c : Scene.currentScene.chunks.values()) {
			int x = c.gridX; int z = c.gridZ;
			int regionX = x > 0 ? x / 16 : (x - 16) / 16;
			int regionZ = z > 0 ? z / 16 : (z - 16) / 16;
			NBT region = loaded_regions.get(getRegionKey(regionX,regionZ));
			byte[] data = ResourceManager.flattenArray(c.cubes);
			region.BYTE_ARRAYS.put(getChunkKey(x,z), data);
			region.BOOLEANS.put(getChunkKey(x,z)+":wasModified",c.wasModified);
			region.BOOLEANS.put(getChunkKey(x,z)+":wasModifiedLast",c.wasModifiedLast);
			loaded_regions.put(getRegionKey(regionX,regionZ), region);
		}
		for(Map.Entry<String, NBT> entry : loaded_regions.entrySet()) {
			ResourceManager.saveObject("region/", entry.getKey(), entry.getValue());
		}
	}
	
	private static void update(int x, int z) {
		for(Chunk c : Scene.currentScene.chunks.values()) {
			if(c.gridX == x && c.gridZ == z){
				c.updateMesh();
				break;
			}
		}
	}
	
	private static Chunk loadChunk(String world, int gridX, int gridZ) {
		int regionX = gridX > 0 ? gridX / 16 : (gridX - 16) / 16;
		int regionZ = gridZ > 0 ? gridZ / 16 : (gridZ - 16) / 16;
		NBT region = null;
		if(!loaded_regions.containsKey(getRegionKey(regionX,regionZ))) {
			Object reg = ResourceManager.loadObject("region/", getRegionKey(regionX,regionZ));
			if(reg == null) {
				region = new NBT();
				loaded_regions.put(getRegionKey(regionX,regionZ), region);
				Chunk chunk = new Chunk(gridX,gridZ);
				chunk.generate();
				return chunk;
			} else {
				region = (NBT)reg;
				loaded_regions.put(getRegionKey(regionX,regionZ), region);
				
			}
		} else {
			region = loaded_regions.get(getRegionKey(regionX,regionZ));
		}
		if(region.BYTE_ARRAYS.containsKey(getChunkKey(gridX,gridZ))) {
			byte[] data = region.BYTE_ARRAYS.get(getChunkKey(gridX,gridZ));
			Chunk chunk = new Chunk(gridX,gridZ);
			chunk.cubes = ResourceManager.expandArray(data);
			chunk.wasModified = region.BOOLEANS.get(getChunkKey(gridX,gridZ)+":wasModified");
			chunk.wasModifiedLast = region.BOOLEANS.get(getChunkKey(gridX,gridZ)+":wasModifiedLast");
			return chunk;
		} else {
			Chunk chunk = new Chunk(gridX,gridZ);
			chunk.generate();
			return chunk;
		}
	}
	
	public static String getRegionKey(int regionX, int regionZ) {
		return regionX+"x"+regionZ+"z.region";
	}
	
	public static String getChunkKey(int gridX, int gridZ) {
		return gridX+"x"+gridZ+"z";
	}
}
