package net.tylermurphy.Minecraft.Chunk;

import static net.tylermurphy.Minecraft.Chunk.Cube.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Render.Data.Mesh;
import net.tylermurphy.Minecraft.Render.Data.Vao;
import net.tylermurphy.Minecraft.Scene.Scene;
import net.tylermurphy.Minecraft.Scene.Objects.SkinnedMesh;

public class ChunkMesh {
	
	private List<Vertex> verticies;
	private List<Vertex> tranaparentVerticies;
	private List<Float> positionsList;
	private List<Float> normalsList;
	private List<Float> tcsList;
	private List<Float> lightingList;
	
	public float[] positions, tcs, normals,lighting;
	
	private Chunk chunk;
	
	private boolean transparent;
	
	public boolean completed = false;
	
	public void update(Chunk chunk, boolean transparent) {
		
		this.chunk = chunk;
		this.transparent = transparent;
		this.completed = false;
		
		verticies = new ArrayList<Vertex>();
		tranaparentVerticies = new ArrayList<Vertex>();
		positionsList = new ArrayList<Float>();
		normalsList = new ArrayList<Float>();
		tcsList = new ArrayList<Float>();
		lightingList = new ArrayList<Float>();
		
		buildMesh();
		pouplateLists();
		
		this.completed = true;
	}
	
	private void buildMesh() {		
		for(int x=0;x<16;x++) {
			for(int y=0; y<256; y++) {
				for(int z=0; z<16; z++) {
					
					if(chunk.cubes[x][y][z] == Cube.AIR) continue;
					
					if(Cube.cubes[chunk.cubes[x][y][z]].transparent != transparent) continue;
					
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
					
					JSONObject positions = Cube.cubes[chunk.cubes[x][y][z]].positions;
					if(chunk.cubes[x][y][z]==17 && ( y==255 || chunk.cubes[x][y+1][z] != 17 )) {
						
						if(y<255) {
							byte xp,xn,zp,zn;
							
							if(x<15) xp = chunk.cubes[x+1][y+1][z];
							else xp = getCube(chunk.gridX+1,chunk.gridZ,0,y+1,z);
							if(x<0) xn = chunk.cubes[x-1][y+1][z];
							else xn = getCube(chunk.gridX-1,chunk.gridZ,15,y+1,z);
							if(z<15) zp = chunk.cubes[x][y+1][z+1];
							else zp = getCube(chunk.gridX,chunk.gridZ+1,x,y+1,0);
							if(z<0) zn = chunk.cubes[x][y+1][z-1];
							else zn = getCube(chunk.gridX,chunk.gridZ-1,x,y+1,15);
							
							if(xp != 17 && xn != 17 && zp != 17 && zn != 17) {
								positions = Cube.posJson.get("water");
								py = false;
							}
						} else {
							positions = Cube.posJson.get("water");
							py = false;
						}
						
					}
					
					if(Cube.cubes[chunk.cubes[x][y][z]].transparent && chunk.cubes[x][y][z] != 17 && (y == 0 || chunk.cubes[x][y-1][z] == 17)) {
						ny = false;
					}
					
					JSONArray PX_POS = positions.getJSONArray("PX_POS");
					JSONArray NX_POS = positions.getJSONArray("NX_POS");
					JSONArray PY_POS = positions.getJSONArray("PY_POS");
					JSONArray NY_POS = positions.getJSONArray("NY_POS");
					JSONArray PZ_POS = positions.getJSONArray("PZ_POS");
					JSONArray NZ_POS = positions.getJSONArray("NZ_POS");
					
					if(!px) { for(int i=0;i<6;i++) { verticies.add(new Vertex(new Vector3f((float)PX_POS.getJSONArray(i).getDouble(0) + x,(float)PX_POS.getJSONArray(i).getDouble(1) + y,(float)PX_POS.getJSONArray(i).getDouble(2) + z),cubes[chunk.cubes[x][y][z]].tc_px[i], NORMALS[i],.95f)); } }
					if(!nx) { for(int i=0;i<6;i++) { verticies.add(new Vertex(new Vector3f((float)NX_POS.getJSONArray(i).getDouble(0) + x,(float)NX_POS.getJSONArray(i).getDouble(1) + y,(float)NX_POS.getJSONArray(i).getDouble(2) + z),cubes[chunk.cubes[x][y][z]].tc_nx[i], NORMALS[i],.95f)); } }
					if(!py) { for(int i=0;i<6;i++) { verticies.add(new Vertex(new Vector3f((float)PY_POS.getJSONArray(i).getDouble(0) + x,(float)PY_POS.getJSONArray(i).getDouble(1) + y,(float)PY_POS.getJSONArray(i).getDouble(2) + z),cubes[chunk.cubes[x][y][z]].tc_py[i], NORMALS[i],1f)); } }
					if(!ny) { for(int i=0;i<6;i++) { verticies.add(new Vertex(new Vector3f((float)NY_POS.getJSONArray(i).getDouble(0) + x,(float)NY_POS.getJSONArray(i).getDouble(1) + y,(float)NY_POS.getJSONArray(i).getDouble(2) + z),cubes[chunk.cubes[x][y][z]].tc_ny[i], NORMALS[i],1f)); } }
					if(!pz) { for(int i=0;i<6;i++) { verticies.add(new Vertex(new Vector3f((float)PZ_POS.getJSONArray(i).getDouble(0) + x,(float)PZ_POS.getJSONArray(i).getDouble(1) + y,(float)PZ_POS.getJSONArray(i).getDouble(2) + z),cubes[chunk.cubes[x][y][z]].tc_pz[i], NORMALS[i],.9f)); } }
					if(!nz) { for(int i=0;i<6;i++) { verticies.add(new Vertex(new Vector3f((float)NZ_POS.getJSONArray(i).getDouble(0) + x,(float)NZ_POS.getJSONArray(i).getDouble(1) + y,(float)NZ_POS.getJSONArray(i).getDouble(2) + z),cubes[chunk.cubes[x][y][z]].tc_nz[i], NORMALS[i],.9f)); } }
				}
			}
		}
	}
	
	private byte getCube(int gx, int gz, int x, int y, int z) {
		Chunk c = Scene.currentScene.chunks.get(gx+":"+gz);
		if(c == null) return 0;
		return c.cubes[x][y][z];
	}
	
	private boolean transparentCheck(int x1,int y1,int z1,int x2,int y2,int z2) {
		return transparentCheck(chunk.cubes[x1][y1][z1],x2,y2,z2);
	}
	
	private boolean transparentCheck(byte block,int x2,int y2,int z2) {
		if(cubes[block].alwaysRenderNeighbors || cubes[chunk.cubes[x2][y2][z2]].alwaysRenderNeighbors)
			return true;
		else if(cubes[block].transparent != true && cubes[chunk.cubes[x2][y2][z2]].transparent != true && block == chunk.cubes[x2][y2][z2])
			return false;
		else if(cubes[block].transparent != cubes[chunk.cubes[x2][y2][z2]].transparent && cubes[block].transparent == true)
			return true;
		return false;
	}
	
	class Vertex{
		Vector3f positions;
		Vector2f uvs;
		Vector3f normals;
		float light;
		Vertex(Vector3f positions, Vector2f uvs, Vector3f normals, float light){
			this.positions = positions;
			this.uvs = uvs;
			this.normals = normals;
			this.light = light;
		}
	}
	
	private void pouplateLists() {
		for(Vertex vertex : verticies) {
			positionsList.add(vertex.positions.x);
			positionsList.add(vertex.positions.y);
			positionsList.add(vertex.positions.z);
			tcsList.add(vertex.uvs.x);
			tcsList.add(vertex.uvs.y);
			normalsList.add(vertex.normals.x);
			normalsList.add(vertex.normals.y);
			normalsList.add(vertex.normals.z);
			lightingList.add(vertex.light);
		}
		
		positions = new float[positionsList.size()];
		tcs = new float[tcsList.size()];
		normals = new float[normalsList.size()];
		lighting = new float[lightingList.size()];
		
		for(int i=0;i<positions.length;i++) positions[i] = positionsList.get(i);
		for(int i=0;i<normals.length;i++) normals[i] = normalsList.get(i);
		for(int i=0;i<tcs.length;i++) tcs[i] = tcsList.get(i);
		for(int i=0;i<lighting.length;i++) lighting[i] = lightingList.get(i);
		
		verticies.clear();
		tranaparentVerticies.clear();
		positionsList.clear();
		normalsList.clear();
		tcsList.clear();
		lightingList.clear();
	}
	
	public SkinnedMesh createChunkMesh() {
		Vao vao = Vao.loadToVAO_POS_UV_BLV(positions, tcs, lighting);
		
		Mesh mesh = new Mesh(vao, Chunk.TEXTURE, 1);
		SkinnedMesh skinedMesh = new SkinnedMesh(mesh,new Vector3f(chunk.gridX*16,9,chunk.gridZ*16), 0, 0, 0, 1f);
		this.positions = null;
		this.tcs = null;
		this.normals = null;
		this.lighting = null;
		this.chunk = null;
		this.completed = false;
		return skinedMesh;
	}
	
}
