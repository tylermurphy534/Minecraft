package net.tylermurphy.Minecraft.Chunk;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Chunk.Cube;
import net.tylermurphy.Minecraft.Util.Constants;

import org.json.JSONObject;

public class Cube{

	public static final byte NULL  = -2;
	public static final byte AIR   = -1;
	
	Vector2f[] tc_px,tc_nx,tc_py,tc_ny,tc_pz,tc_nz;
	
	public final boolean transparent;
	public final boolean alwaysRenderNeighbors;
	public final boolean palceable;
	public final String name;
	public final long tick_update_delay;
	public final JSONObject positions;

	public static Cube[] cubes;
	
	public static final HashMap<String,JSONObject> posJson = new HashMap<String,JSONObject>();
	
	public static void init() {
		
		File dir = new File(Constants.POSITIONS_LOCATION);
		File[] directoryListing = dir.listFiles();
		for(File file : directoryListing) {
			
			StringBuilder contentBuilder = new StringBuilder();
	        try (Stream<String> stream = Files.lines( Paths.get(file.getPath()), StandardCharsets.UTF_8)){
	            stream.forEach(s -> contentBuilder.append(s).append("\n"));
	        }
	        catch (IOException e) { 
	            e.printStackTrace();
	        }

			JSONObject json = new JSONObject(contentBuilder.toString());
			posJson.put(file.getName().substring(0, file.getName().length()-5), json);
		}
		
		Cube[] temp = {
			new Cube(1,1,0,2,1,1,false,false,true,"grass",3000L,"default"),
			new Cube(2,false,false,true,"dirt",3000L,"default"),
			new Cube(3,false,false,true,"stone",0L,"default"),
			new Cube(11,false,false,true,"cobblestone",0L,"default"),
			new Cube(12,false,false,true,"mossy_cobblestone",0L,"default"),
			new Cube(14,false,false,true,"stone_bricks",0L,"default"),
			new Cube(15,false,false,true,"mossy_stone_bricks",0L,"default"),
			new Cube(16,false,false,true,"cracked_stone_bricks",0L,"default"),
			new Cube(17,false,false,true,"chisled_stone_bricks",0L,"default"),
			new Cube(18,false,false,true,"bricks",0L,"default"),
			new Cube(4,4,5,5,4,4,false,false,true,"log",0L,"default"),
			new Cube(13,false,false,true,"planks",0L,"default"),
			new Cube(6,true,false,true,"leaf",0L,"default"),
			new Cube(7,false,false,true,"sand",0L,"default"),
			new Cube(21,21,23,22,21,21,false,false,true,"sandstone",0L,"default"),
			new Cube(9,9,20,20,9,9,false,true,true,"cactus",0L,"cactus"),
			new Cube(10,true,false,true,"glass",0L,"default"),
			new Cube(8,true,false,false,"water",250L,"default"),
			new Cube(19,false,false,true,"wool",0L,"default"),
		};
		cubes = temp;
	}
	
	private Cube(int px, int nx, int py, int ny, int pz, int nz,boolean transparent,boolean alwaysRenderNeighbors,boolean placable,String name, long tick_update_delay, String positions) {
		tc_px = genTC(px);
		tc_nx = genTC(nx);
		tc_py = genTC(py);
		tc_ny = genTC(ny);
		tc_pz = genTC(pz);
		tc_nz = genTC(nz);
		this.transparent = transparent;
		this.name = name;
		this.palceable = placable;
		this.tick_update_delay = tick_update_delay;
		this.positions = posJson.get(positions);
		this.alwaysRenderNeighbors = alwaysRenderNeighbors;
	}
	
	private Cube(int i, boolean transparent,boolean alwaysRenderNeighbors,boolean placable,String name, long tick_update_delay, String positions) {
		tc_px = genTC(i);
		tc_nx = genTC(i);
		tc_py = genTC(i);
		tc_ny = genTC(i);
		tc_pz = genTC(i);
		tc_nz = genTC(i);
		this.transparent = transparent;
		this.name = name;
		this.palceable = placable;
		this.tick_update_delay = tick_update_delay;
		this.positions = posJson.get(positions);
		this.alwaysRenderNeighbors = alwaysRenderNeighbors;
	}
	
	private Vector2f[] genTC(int i) {
		Vector2f[] tc = {
				new Vector2f(1.f / 16.f*i, 1.f / 16.f*(i/16)),
				new Vector2f(1.f / 16.f*i, 1.f / 16.f*(i/16)+1.f / 16),
				new Vector2f(1.f / 16.f*(i+1), 1.f / 16.f*(i/16)+1.f / 16),
				new Vector2f(1.f / 16.f*(i+1), 1.f / 16.f*(i/16)+1.f / 16),
				new Vector2f(1.f / 16.f*(i+1), 1.f / 16.f*(i/16)),
				new Vector2f(1.f / 16.f*i, 1.f / 16.f*(i/16))	
		};
		return tc;
	}
	
	public static Vector3f[] NORMALS = {
			
			new Vector3f(0.f, 0.f, 0.f),
			new Vector3f(0.f, 0.f, 0.f),
			new Vector3f(0.f, 0.f, 0.f),
			new Vector3f(0.f, 0.f, 0.f),
			new Vector3f(0.f, 0.f, 0.f),
			new Vector3f(0.f, 0.f, 0.f)
			
	};
	
}
