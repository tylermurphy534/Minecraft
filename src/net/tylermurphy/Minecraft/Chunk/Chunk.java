package net.tylermurphy.Minecraft.Chunk;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Render.Data.Texture;
import net.tylermurphy.Minecraft.Render.Data.Vao;
import net.tylermurphy.Minecraft.Scene.Scene;
import net.tylermurphy.Minecraft.Scene.Objects.Entity;
import net.tylermurphy.Minecraft.Scene.Objects.SkinnedMesh;
import net.tylermurphy.Minecraft.Tick.BlockUpdate;

public class Chunk{

	
	public static final int TEXTURE = Texture.loadTexture("Blocks");
	public static int SEED;
	public static int RENDER_DISTANCE = 12;
	public static int TOTAL_CHUNK_DISTANCE = RENDER_DISTANCE*2+1;
	private static ExecutorService threadpool = Executors.newCachedThreadPool();
	
	public int gridX;
	public int gridZ;
	public int chunk_seed;
	
	public List<Entity> entities;
	
	public byte[][][] cubes;
	public byte[][][] id;
	
	public SkinnedMesh opaque_mesh, transparent_mesh;
	
	protected ChunkMesh chunkMesher, transparentChunkMesher;
	
	public boolean hasBeenModified = false;
	public boolean wasModified = false;
	public boolean wasModifiedLast = false;
	
	public Chunk(int gridX,int gridZ) {
		this.gridX = gridX;
		this.gridZ = gridZ;
		this.chunk_seed = gridX * SEED ^ gridZ;
		entities = new ArrayList<Entity>();
		cubes = new byte[16][256][16];
	}
	
	public void updateMesh() {
		chunkMesher = new ChunkMesh();
		threadpool.submit(() -> chunkMesher.update(this, false));
		transparentChunkMesher = new ChunkMesh();
		threadpool.submit(() -> transparentChunkMesher.update(this, true));
	}
	
	public void loadMesh() {
		if(chunkMesher != null && chunkMesher.completed == true) {
			if(opaque_mesh != null && opaque_mesh.getMesh().getVao() != null)
				Vao.disposeVAO(opaque_mesh.getMesh().getVao().getVaoID());
			opaque_mesh = chunkMesher.createChunkMesh();
			chunkMesher = null;
		}
		if(transparentChunkMesher != null && transparentChunkMesher.completed == true) {
			if(transparent_mesh != null && transparent_mesh.getMesh().getVao() != null)
				Vao.disposeVAO(transparent_mesh.getMesh().getVao().getVaoID());
			transparent_mesh = transparentChunkMesher.createChunkMesh();
			transparentChunkMesher = null;
		}
	}
	
	public static void updateMesh(int gridX, int gridZ) {
		for(Chunk chunk : Scene.currentScene.chunks.values()) {
			if(chunk.gridX == gridX && chunk.gridZ == gridZ) {
				chunk.updateMesh();
			}
		}
	}
	
	public void generate() {
		Generator.generate(this);
	}
	
	public static byte getBlock(int x, int y, int z) {

		if(y<0 || y> 255) return Cube.AIR;
		int cx,cz;
		if(x>-1)
			cx = (int) ((x)/16);
		else
			cx = (int) (((x)-15)/16);
		if(z>-1)
			cz = (int) ((z)/16);
		else
			cz = (int) (((z)-15)/16);
		
		cx += Scene.currentScene.world_origin.x/16;
		cz += Scene.currentScene.world_origin.y/16;
		
		Chunk chunk = Scene.currentScene.chunks.get(cx+":"+cz);
		if(chunk!=null) {
			int rx,rz;
			if(x>-1) 
				rx = (int)(x)%16;
			else {
				int r16 = (int)(Math.abs((x)/16)+1);
				rx = (int)((r16*16-Math.abs(x))%16);
			}
			if(z>-1) 
				rz = (int)(z)%16;
			else {
				int r16 = (int)(Math.abs((z)/16)+1);
				rz = (int)((r16*16-Math.abs(z))%16);
			}
			return chunk.cubes[rx][(int)y][rz];
		}
		return Cube.NULL;
	}
	
	public static int getHighestBlock(int fx, int fz) {
		for(int y=255;y>=0;y--) {
			if(getBlock(fx,y,fz) != -1) return y;
		}
		return 255;
	}
	
	public static boolean setBlock(int x, int y,int z, byte id) {
		
		if(y<0 || y> 255) return false;
		int cx,cz;
		if(x>-1)
			cx = (int) ((x)/16);
		else
			cx = (int) (((x)-15)/16);
		if(z>-1)
			cz = (int) ((z)/16);
		else
			cz = (int) (((z)-15)/16);
		
		cx += Scene.currentScene.world_origin.x/16;
		cz += Scene.currentScene.world_origin.y/16;
		
		Chunk chunk = Scene.currentScene.chunks.get(cx+":"+cz);
		if(chunk!=null) {
			int rx,rz;
			if(x>-1) 
				rx = (int)(x%16)%16;
			else {
				int r16 = (int)(Math.abs((x%16)/16)+1);
				rx = (int)((r16*16-Math.abs(x%16))%16);
			}
			if(z>-1) 
				rz = (int)(z%16)%16;
			else {
				int r16 = (int)(Math.abs((z%16)/16)+1);
				rz = (int)((r16*16-Math.abs(z%16))%16);
			}
			
			BlockUpdate.createBlockUpdate((int)x, (int)y, (int)z, chunk.cubes[rx][(int)y][rz], id);
			
			chunk.hasBeenModified = true;
			chunk.wasModified = true;
			chunk.cubes[rx][(int)y][rz] = id;
			if(id != -1 && y > 0 && chunk.cubes[rx][(int)y - 1][rz] == 0)
				chunk.cubes[rx][(int)y-1][rz] = 1;
			int ox = (int)(rx%16) == 0 ? -1 : (int)(rx%16) == 15 ? 1 : 0;
			int oz = (int)(rz%16) == 0 ? -1 : (int)(rz%16) == 15 ? 1 : 0;
			Chunk coc = null,coz = null;
			if(ox != 0) coc = Chunk.getChunk(chunk.gridX + ox, chunk.gridZ);
			if(oz != 0) coz = Chunk.getChunk(chunk.gridX, chunk.gridZ + oz);
			if(coc != null) {
				coc.hasBeenModified = true;
				coc.wasModified = true;
			}
			if(coz != null) {
				coz.hasBeenModified = true;
				coz.wasModified = true;
			}
			return true;
		}
		return false;
	}
	
	public static Chunk getChunk(int x,int z) {
		for(Chunk c : Scene.currentScene.chunks.values()) {
			if(c.gridX == x && c.gridZ == z)
				return c;
		}
		return null;
	}
	
}
