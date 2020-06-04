package com.Minecraft.World;

import com.Minecraft.Chunk.Chunk;
import com.Minecraft.Data.Vao;
import com.Minecraft.Scene.Scene;

public class ChunkLoader {

	public static void run() {
		try {
			int cx = 0,cz = 0;
			int renderDistance = Chunk.RENDER_DISTANCE;
			cx = (int) ((Scene.player.getPosition().x+Scene.world_origin.x)/16);
			cz = (int) ((Scene.player.getPosition().z+Scene.world_origin.y)/16);
			for(Chunk c : Scene.chunks) {
				if(c.gridX < cx-renderDistance || c.gridX > cx+renderDistance || c.gridZ < cz-renderDistance || c.gridZ > cz+renderDistance) { 
					ResourceManager.saveChunk("test", c);
					Vao.disposeVAO(c.getVaoID());
					c.mesh = null;
					c.cubes = null;
					c.entities = null;
					Scene.chunks.remove(c);
				}
			}
			for(int x=cx-renderDistance; x<cx+renderDistance;x++) {
				for(int z=cz-renderDistance; z<cz+renderDistance;z++) {
					boolean found = false;
					for(Chunk c : Scene.chunks) {
						if(c.gridX == x && c.gridZ == z){
							found = true;
							break;
						}
					}
					if(!found) {
						Chunk chunk = ResourceManager.loadChunk("test",x,z);
						if(chunk == null) {
							chunk = new Chunk(x,z);
							chunk.generate();
						}
						Scene.chunks.add(chunk);
						chunk.updateMesh();
						update(x+1,z);
						update(x-1,z);
						update(x,z+1);
						update(x,z-1);
						return;
					}
				}
			}
		}catch(Exception e) {}
	}
	
	private static void update(int x, int z) {
		for(Chunk c : Scene.chunks) {
			if(c.gridX == x && c.gridZ == z){
				c.updateMesh();
				break;
			}
		}
	}
}
