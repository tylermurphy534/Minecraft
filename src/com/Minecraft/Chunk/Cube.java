package com.Minecraft.Chunk;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.Minecraft.Chunk.Cube;


public class Cube{

	public static final byte NULL  = -2;
	public static final byte AIR   = -1;
	
	Vector2f[] tc_px,tc_nx,tc_py,tc_ny,tc_pz,tc_nz;
	
	public final boolean transparent;
	public final boolean palceable;
	public final String name;

	public static Cube[] cubes = {
		new Cube(1,1,0,2,1,1,false,"grass"),
		new Cube(2,false,"dirt"),
		new Cube(3,false,"stone"),
		new Cube(11,false,"cobblestone"),
		new Cube(12,false,"mossy_cobblestone"),
		new Cube(14,false,"stone_bricks"),
		new Cube(15,false,"mossy_stone_bricks"),
		new Cube(16,false,"cracked_stone_bricks"),
		new Cube(17,false,"chisled_stone_bricks"),
		new Cube(18,false,"bricks"),
		new Cube(4,4,5,5,4,4,false,"log"),
		new Cube(13,false,"planks"),
		new Cube(6,true,"leaf"),
		new Cube(7,false,"sand"),
		new Cube(21,21,23,22,21,21,false,"sandstone"),
		new Cube(9,9,20,20,9,9,false,"cactus"),
		new Cube(10,true,"glass"),
		new Cube(8,true,false,"water"),
		new Cube(19,false,"wool"),
	};
	
	private Cube(int px, int nx, int py, int ny, int pz, int nz,boolean transparent,String name) {
		tc_px = genTC(px);
		tc_nx = genTC(nx);
		tc_py = genTC(py);
		tc_ny = genTC(ny);
		tc_pz = genTC(pz);
		tc_nz = genTC(nz);
		this.transparent = transparent;
		this.name = name;
		this.palceable = true;
	}
	
	private Cube(int i, boolean transparent,String name) {
		tc_px = genTC(i);
		tc_nx = genTC(i);
		tc_py = genTC(i);
		tc_ny = genTC(i);
		tc_pz = genTC(i);
		tc_nz = genTC(i);
		this.transparent = transparent;
		this.name = name;
		this.palceable = true;
	}
	
	private Cube(int i, boolean transparent,boolean placable,String name) {
		tc_px = genTC(i);
		tc_nx = genTC(i);
		tc_py = genTC(i);
		tc_ny = genTC(i);
		tc_pz = genTC(i);
		tc_nz = genTC(i);
		this.transparent = transparent;
		this.name = name;
		this.palceable = placable;
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
	
	public static Vector3f[] PX_POS = {
			
			new Vector3f(0.5f,0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,0.5f),
			new Vector3f(0.5f,-0.5f,0.5f),
			new Vector3f(0.5f,0.5f,0.5f),
			new Vector3f(0.5f,0.5f,-0.5f)
			
	};
	
	public static Vector3f[] NX_POS = {
			
			new Vector3f(-0.5f,0.5f,-0.5f),
			new Vector3f(-0.5f,-0.5f,-0.5f),
			new Vector3f(-0.5f,-0.5f,0.5f),
			new Vector3f(-0.5f,-0.5f,0.5f),
			new Vector3f(-0.5f,0.5f,0.5f),
			new Vector3f(-0.5f,0.5f,-0.5f)
			
	};
	
	public static Vector3f[] PY_POS = {
			
			new Vector3f(-0.5f,0.5f,0.5f),
			new Vector3f(-0.5f,0.5f,-0.5f),
			new Vector3f(0.5f,0.5f,-0.5f),
			new Vector3f(0.5f,0.5f,-0.5f),
			new Vector3f(0.5f,0.5f,0.5f),
			new Vector3f(-0.5f,0.5f,0.5f)
			
	};
	
	public static Vector3f[] NY_POS = {
			
			new Vector3f(-0.5f,-0.5f,0.5f),
			new Vector3f(-0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,0.5f),
			new Vector3f(-0.5f,-0.5f,0.5f)
			
	};
	
	public static Vector3f[] PZ_POS = {
			
			new Vector3f(-0.5f,0.5f,0.5f),
			new Vector3f(-0.5f,-0.5f,0.5f),
			new Vector3f(0.5f,-0.5f,0.5f),
			new Vector3f(0.5f,-0.5f,0.5f),
			new Vector3f(0.5f,0.5f,0.5f),
			new Vector3f(-0.5f,0.5f,0.5f)
			
	};
	
	public static Vector3f[] NZ_POS = {
			
			new Vector3f(-0.5f,0.5f,-0.5f),
			new Vector3f(-0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,0.5f,-0.5f),
			new Vector3f(-0.5f,0.5f,-0.5f)
			
	};
	
}
