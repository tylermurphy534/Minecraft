package com.Minecraft.World;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.lwjgl.util.vector.Vector2f;

import com.Minecraft.Chunk.Chunk;
import com.Minecraft.Scene.Camera;
import com.Minecraft.Scene.Player;
import com.Minecraft.Scene.Scene;
import com.Minecraft.Util.Constants;

public class ResourceManager {

	static String saveName;
	
	protected static void init(String saveName) {
		ResourceManager.saveName = saveName;
	}
	
	private static byte[] compress(byte[] data){
		ByteArrayOutputStream baos = null;
		Deflater dfl = new Deflater();
		dfl.setLevel(Deflater.BEST_COMPRESSION);
		dfl.setInput(data);
		dfl.finish();
		baos = new ByteArrayOutputStream();
		byte[] tmp = new byte[4*1024];
		try {
			while(!dfl.finished()) {
				int size = dfl.deflate(tmp);
				baos.write(tmp, 0, size);
			}
		} catch(Exception e) {
			
		} finally {
			try {
				if(baos != null) baos.close();
			} catch(Exception e) {}
		}
		return baos.toByteArray();
	}
	
	protected static byte[] decompress(byte[] data){
		ByteArrayOutputStream baos = null;
		Inflater ifl = new Inflater();
		ifl.setInput(data);
		baos = new ByteArrayOutputStream();
		byte[] tmp = new byte[4*1024];
		try {
			while(!ifl.finished()) {
				int size = ifl.inflate(tmp);
				baos.write(tmp, 0, size);
			}
		} catch(Exception e) {
			
		} finally {
			try {
				if(baos != null) baos.close();
			} catch(Exception e) {}
		}
		return baos.toByteArray();
	}
	
	private static byte[][][] expandArray(byte[] data) {
		byte[][][] cubes = new byte[16][256][16];
		for (int x = 0 ; x != 16 ; x++) {
		    for (int y = 0 ; y != 256 ; y++) {
		        for (int z = 0 ; z != 16 ; z++) {
		            cubes[x][y][z] = data[256 * 16 * x + 16 * y + z];
		        }
		    }
		}
		return cubes;
	}
	
	private static byte[] flattenArray(byte[][][] cubes) {
		byte[] data = new byte[16*256*16];
		for (int x = 0 ; x != 16 ; x++) {
		    for (int y = 0 ; y != 256 ; y++) {
		        for (int z = 0 ; z != 16 ; z++) {
		        	 data[256 * 16 * x + 16 * y + z] = cubes[x][y][z];
		        }
		    }
		}
		return data;
	}
	
	private static Object loadObject(String path, String fileName) {
		ObjectInputStream is = null;
		try {
			 is = new ObjectInputStream(new FileInputStream(new File(path+fileName)));
			 Object object = is.readObject();
			 is.close();
			 return object;
		} catch(Exception e) {
		} finally {
			try {
				if(is!=null) is.close();
			} catch(Exception e) {
			}
		}
		return null;
	}
	
	private static void saveObject(String path, String fileName, Object o) {
		ObjectOutputStream os = null;
		try {
			File folder = new File(path);
			folder.mkdirs();
			File file = new File(path+fileName);
			if(!file.exists()) file.createNewFile();
			os = new ObjectOutputStream(new FileOutputStream(file));
			os.writeObject(o);
			os.close();
		} catch(Exception e) {
			
		} finally {
			try {
				if(os!=null) os.close();
			} catch(Exception e) {}
		}
	}
	
	public static Chunk loadChunk(String saveName, int x, int z) {
		String path = Constants.SAVES_LOCATION+"/"+saveName+"/chunk/"+x+"."+z+"/";
		String fileName = "cubes";
		byte[] data;
		try {
			File file = new File(path+fileName);
			data = Files.readAllBytes(file.toPath());
			data = decompress(data);
		} catch(Exception e) {
			return null;
		}
		Chunk c = new Chunk(x,z);
		c.cubes = expandArray(data);
		ChunkData cdata = (ChunkData) loadObject(Constants.SAVES_LOCATION+"/"+saveName+"/chunk/"+x+"."+z+"/","data");
		if(cdata != null) {
			c.wasModified = cdata.wasModified;
			c.wasModifiedLast = cdata.wasModifiedLast;
		}
		return c;
	}
	
	public static void saveChunk(String saveName, Chunk c) {
		if(c.hasBeenModified == false) return;
		ChunkData cdata = new ChunkData();
		cdata.wasModified = c.wasModified;
		cdata.wasModifiedLast = c.wasModifiedLast;
		saveObject(Constants.SAVES_LOCATION+"/"+saveName+"/chunk/"+c.gridX+"."+c.gridZ+"/","data",cdata);
		byte [] data = flattenArray(c.cubes);
		String path = Constants.SAVES_LOCATION+"/"+saveName+"/chunk/"+c.gridX+"."+c.gridZ+"/";
		String fileName = "cubes";
		data = compress(data);
		try {
			File folder = new File(path);
			folder.mkdirs(); 
			File file = new File(path+fileName);
			if(!file.exists()) file.createNewFile();
			Files.write(file.toPath(), data);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class WorldData implements Serializable {
		private static final long serialVersionUID = 6114369009417939853L;
		public int SEED;
		public Vector2f WORLD_ORIGIN;
	}
	
	private static class ChunkData implements Serializable {
		private static final long serialVersionUID = -597046523883120149L;
		public boolean wasModified;
		public boolean wasModifiedLast;
	}
	
	public static boolean loadWorldData(String saveName) {
		WorldData data = (WorldData) loadObject(Constants.SAVES_LOCATION+"/"+saveName+"/","data");
		if(data != null) {
			Chunk.SEED = data.SEED;
			Scene.world_origin = data.WORLD_ORIGIN;
			return true;
		} else {
			return false;
		}
	}
	
	public static void saveWorldData(String saveName) {
		WorldData data = new WorldData();
		data.SEED = Chunk.SEED;
		data.WORLD_ORIGIN = Scene.world_origin;
		saveObject(Constants.SAVES_LOCATION+"/"+saveName+"/","data",data);
	}
	
	public static Camera loadCamera(String saveName) {
		return (Camera) loadObject(Constants.SAVES_LOCATION+"/"+saveName+"/","camera");
	}
	
	public static void saveCamera(String saveName,Camera camera) {
		saveObject(Constants.SAVES_LOCATION+"/"+saveName+"/","camera",camera);
	}
	
	public static Player loadPlayer(String saveName) {
		return (Player) loadObject(Constants.SAVES_LOCATION+"/"+saveName+"/","player");
	}
	
	public static void savePlayer(String saveName,Player player) {
		saveObject(Constants.SAVES_LOCATION+"/"+saveName+"/","player",player);
	}
}
