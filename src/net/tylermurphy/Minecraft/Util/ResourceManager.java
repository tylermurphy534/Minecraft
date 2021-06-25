package net.tylermurphy.Minecraft.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import net.tylermurphy.Minecraft.Main;

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
	
	public static byte[][][] expandArray(byte[] data) {
		byte[] result = decompress(data);
		byte[][][] cubes = new byte[16][256][16];
		for (int x = 0 ; x != 16 ; x++) {
		    for (int y = 0 ; y != 256 ; y++) {
		        for (int z = 0 ; z != 16 ; z++) {
		            cubes[x][y][z] = result[256 * 16 * x + 16 * y + z];
		        }
		    }
		}
		return cubes;
	}
	
	public static byte[] flattenArray(byte[][][] cubes) {
		byte[] data = new byte[16*256*16];
		for (int x = 0 ; x != 16 ; x++) {
		    for (int y = 0 ; y != 256 ; y++) {
		        for (int z = 0 ; z != 16 ; z++) {
		        	 data[256 * 16 * x + 16 * y + z] = cubes[x][y][z];
		        }
		    }
		}
		byte[] result = compress(data);
		return result;
	}
	
	public static Object loadObject(String pass_path, String fileName) {
		String path = Constants.SAVES_LOCATION+"/"+Main.currentWorld+"/"+pass_path+"/";
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
	
	public static void saveObject(String pass_path, String fileName, Object o) {
		String path = Constants.SAVES_LOCATION+"/"+Main.currentWorld+"/"+pass_path+"/";
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
	
}
