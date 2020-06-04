package com.Minecraft.Chunk;

import java.util.ArrayList;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.Minecraft.Data.Model;
import com.Minecraft.Data.Texture;
import com.Minecraft.Data.Vao;
import com.Minecraft.Scene.Entity;
import com.Minecraft.Scene.Scene;

import com.Minecraft.Chunk.Chunk;

public class Chunk{

	
	public static final int TEXTURE = Texture.loadTexture("Blocks");
	public static int SEED;
	public static int RENDER_DISTANCE = 5;
	
	public int gridX;
	public int gridZ;
	
	public List<Entity> entities;
	
	public byte[][][] cubes;
	private ChunkMesh mesh_data;
	private Vao mesh_vao;
	public Entity mesh;
	
	public boolean hasBeenModified = false;
	public boolean wasModified = false;
	public boolean wasModifiedLast = false;
	
	public int getVaoID() {
		return mesh_vao.getVaoID();
	}
	
	public Chunk(int gridX,int gridZ) {
		this.gridX = gridX;
		this.gridZ = gridZ;
		entities = new ArrayList<Entity>();
		cubes = new byte[16][256][16];
		mesh_data = new ChunkMesh();
	}
	
	public void updateMesh() {
		if(mesh_vao != null) Vao.disposeVAO(mesh_vao.getVaoID());
		mesh_data.update(this);
		mesh_vao = Vao.loadToVAO(mesh_data.positions, mesh_data.tcs);
		mesh_data.positions = null;
		mesh_data.tcs = null;
		mesh = new Entity(new Model(mesh_vao, TEXTURE), new Vector3f(gridX*16,9,gridZ*16), 0, 0, 0, 1f);
		mesh.drawMode = 1;
	}
	
	public static void updateMesh(int gridX, int gridZ) {
		for(Chunk chunk : Scene.chunks) {
			if(chunk.gridX == gridX && chunk.gridZ == gridZ) {
				chunk.updateMesh();
			}
		}
	}
	
	public void generate() {
		Generator.generate(this);
	}


	
	public static byte getBlock(float fx, float fy, float fz) {
		int x = (int)(fx);
		int y = (int)(fy);
		int z = (int)(fz);
		if(y<0 || y> 255) return Cube.AIR;
		int cx,cz;
		if(x>-1)
			cx = (int) ((x+Scene.world_origin.x)/16);
		else
			cx = (int) (((x+Scene.world_origin.x)-15)/16);
		if(z>-1)
			cz = (int) ((z+Scene.world_origin.y)/16);
		else
			cz = (int) (((z+Scene.world_origin.y)-15)/16);
		for(Chunk chunk : Scene.chunks) {
			if(chunk.gridX == cx && chunk.gridZ == cz) {
				int rx,rz;
				if(x>-1) 
					rx = (int)(x+Scene.world_origin.x)%16;
				else {
					int r16 = (int)(Math.abs((x+Scene.world_origin.x)/16)+1);
					rx = (int)((r16*16-Math.abs(x+Scene.world_origin.x))%16);
				}
				if(z>-1) 
					rz = (int)(z+Scene.world_origin.y)%16;
				else {
					int r16 = (int)(Math.abs((z+Scene.world_origin.y)/16)+1);
					rz = (int)((r16*16-Math.abs(z+Scene.world_origin.y))%16);
				}
				return chunk.cubes[rx][(int)y][rz];
			}
		}
		return Cube.NULL;
	}
	
	public static int getHighestBlock(int fx, int fz) {
		for(int y=255;y>=0;y--) {
			if(getBlock(fx,y,fz) != -1) return y;
		}
		return 255;
	}
	
	public static boolean setBlock(float fx, float fy,float fz, byte id) {
		int x = (int)(fx);
		int y = (int)(fy);
		int z = (int)(fz);
		if(y<0 || y> 255) return false;
		int cx,cz;
		if(x>-1)
			cx = (int) ((x+Scene.world_origin.x)/16);
		else
			cx = (int) (((x+Scene.world_origin.x)-15)/16);
		if(z>-1)
			cz = (int) ((z+Scene.world_origin.y)/16);
		else
			cz = (int) (((z+Scene.world_origin.y)-15)/16);
		for(Chunk chunk : Scene.chunks) {
			if(chunk.gridX == cx && chunk.gridZ == cz) {
				int rx,rz;
				if(x>-1) 
					rx = (int)(x+Scene.world_origin.x%16)%16;
				else {
					int r16 = (int)(Math.abs((x+Scene.world_origin.x%16)/16)+1);
					rx = (int)((r16*16-Math.abs(x+Scene.world_origin.x%16))%16);
				}
				if(z>-1) 
					rz = (int)(z+Scene.world_origin.y%16)%16;
				else {
					int r16 = (int)(Math.abs((z+Scene.world_origin.y%16)/16)+1);
					rz = (int)((r16*16-Math.abs(z+Scene.world_origin.y%16))%16);
				}
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
		}
		return false;
	}
	
	public static Chunk getChunk(int x,int z) {
		for(Chunk c : Scene.chunks) {
			if(c.gridX == x && c.gridZ == z)
				return c;
		}
		return null;
	}
	
}
