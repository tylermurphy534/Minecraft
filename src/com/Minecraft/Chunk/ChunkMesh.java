package com.Minecraft.Chunk;

import static com.Minecraft.Chunk.Cube.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.Minecraft.Scene.Scene;

public class ChunkMesh {
	
	private List<Vertex> verticies;
	private List<Vertex> tranaparentVerticies;
	private List<Float> positionsList;
	private List<Float> tcsList;
	
	public float[] positions, tcs;
	
	private Chunk chunk;
	
	public ChunkMesh() {
		verticies = new ArrayList<Vertex>();
		tranaparentVerticies = new ArrayList<Vertex>();
		positionsList = new ArrayList<Float>();
		tcsList = new ArrayList<Float>();
	}
	
	public void update(Chunk chunk) {
		this.chunk = chunk;
		buildMesh();
		pouplateLists();
		this.chunk = null;
	}
	
	private void buildMesh() {		
		for(int x=0;x<16;x++) {
			for(int y=0; y<256; y++) {
				for(int z=0; z<16; z++) {
					
					if(chunk.cubes[x][y][z] == Cube.AIR) continue;
					
					boolean px = true, nx = true, py = true, ny = true, pz = true, nz = true;
					
					if(x<15) px = chunk.cubes[x+1][y][z] == Cube.AIR || transparentCheck(x+1,y,z,x,y,z) ?  false : true;
					else px = getCube(chunk.gridX+1,chunk.gridZ,0,y,z) == Cube.AIR || transparentCheck(getCube(chunk.gridX+1,chunk.gridZ,0,y,z),x,y,z) ? false : true;
					if(x>0) nx = chunk.cubes[x-1][y][z] == Cube.AIR || transparentCheck(x-1,y,z,x,y,z) ?  false : true;
					else nx = getCube(chunk.gridX-1,chunk.gridZ,15,y,z) == Cube.AIR || transparentCheck(getCube(chunk.gridX-1,chunk.gridZ,15,y,z),x,y,z) ? false : true;
					if(y<255) py = chunk.cubes[x][y+1][z] == Cube.AIR || transparentCheck(x,y+1,z,x,y,z) ?  false : true;
					if(y>0) ny = chunk.cubes[x][y-1][z] == Cube.AIR || transparentCheck(x,y-1,z,x,y,z) ?  false : true;
					if(z<15) pz = chunk.cubes[x][y][z+1] == Cube.AIR || transparentCheck(x,y,z+1,x,y,z) ?  false : true;
					else pz = getCube(chunk.gridX,chunk.gridZ+1,x,y,0) == Cube.AIR || transparentCheck(getCube(chunk.gridX,chunk.gridZ+1,x,y,0),x,y,z) ? false : true;
					if(z>0) nz = chunk.cubes[x][y][z-1] == Cube.AIR || transparentCheck(x,y,z-1,x,y,z) ?  false : true;
					else nz = getCube(chunk.gridX,chunk.gridZ-1,x,y,15) == Cube.AIR || transparentCheck(getCube(chunk.gridX,chunk.gridZ-1,x,y,15),x,y,z) ? false : true;
					
					if(!px) { for(int i=0;i<6;i++) { verticies.add(new Vertex(new Vector3f(PX_POS[i].x + x,PX_POS[i].y + y,PX_POS[i].z + z),cubes[chunk.cubes[x][y][z]].tc_px[i])); } }
					if(!nx) { for(int i=0;i<6;i++) { verticies.add(new Vertex(new Vector3f(NX_POS[i].x + x,NX_POS[i].y + y,NX_POS[i].z + z),cubes[chunk.cubes[x][y][z]].tc_nx[i])); } }
					if(!py) { for(int i=0;i<6;i++) { verticies.add(new Vertex(new Vector3f(PY_POS[i].x + x,PY_POS[i].y + y,PY_POS[i].z + z),cubes[chunk.cubes[x][y][z]].tc_py[i])); } }
					if(!ny) { for(int i=0;i<6;i++) { verticies.add(new Vertex(new Vector3f(NY_POS[i].x + x,NY_POS[i].y + y,NY_POS[i].z + z),cubes[chunk.cubes[x][y][z]].tc_ny[i])); } }
					if(!pz) { for(int i=0;i<6;i++) { verticies.add(new Vertex(new Vector3f(PZ_POS[i].x + x,PZ_POS[i].y + y,PZ_POS[i].z + z),cubes[chunk.cubes[x][y][z]].tc_pz[i])); } }
					if(!nz) { for(int i=0;i<6;i++) { verticies.add(new Vertex(new Vector3f(NZ_POS[i].x + x,NZ_POS[i].y + y,NZ_POS[i].z + z),cubes[chunk.cubes[x][y][z]].tc_nz[i])); } }
				}
			}
		}
	}
	
	private byte getCube(int gridX, int gridZ, int x, int y, int z) {
		for(Chunk chunk : Scene.chunks) {
			if(chunk.gridX == gridX && chunk.gridZ == gridZ) {
				return chunk.cubes[x][y][z];
			}
		}
		return Cube.AIR;
	}
	
	private boolean transparentCheck(int x1,int y1,int z1,int x2,int y2,int z2) {
		return transparentCheck(chunk.cubes[x1][y1][z1],x2,y2,z2);
	}
	
	private boolean transparentCheck(byte block,int x2,int y2,int z2) {
		if(cubes[block].transparent != true && cubes[chunk.cubes[x2][y2][z2]].transparent != true && block == chunk.cubes[x2][y2][z2])
			return false;
		else if(cubes[block].transparent != cubes[chunk.cubes[x2][y2][z2]].transparent && cubes[block].transparent == true)
			return true;
		return false;
	}
	
	class Vertex{
		Vector3f positions;
		Vector2f uvs;
		Vertex(Vector3f positions, Vector2f uvs){
			this.positions = positions;
			this.uvs = uvs;
		}
	}
	
	private void pouplateLists() {
		for(Vertex vertex : verticies) {
			positionsList.add(vertex.positions.x);
			positionsList.add(vertex.positions.y);
			positionsList.add(vertex.positions.z);
			tcsList.add(vertex.uvs.x);
			tcsList.add(vertex.uvs.y);
		}
		
		positions = new float[positionsList.size()];
		tcs = new float[tcsList.size()];
		
		for(int i=0;i<positions.length;i++) positions[i] = positionsList.get(i);
		for(int i=0;i<tcs.length;i++) tcs[i] = tcsList.get(i);
		
		verticies.clear();
		tranaparentVerticies.clear();
		positionsList.clear();
		tcsList.clear();
		
	}
	
}
