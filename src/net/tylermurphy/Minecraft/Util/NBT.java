package net.tylermurphy.Minecraft.Util;

import java.io.Serializable;
import java.util.HashMap;

public class NBT implements Serializable {

	private static final long serialVersionUID = 6866388687418069074L;
	
	public final HashMap<String,byte[]> BYTE_ARRAYS = new HashMap<String,byte[]>();
	public final HashMap<String,float[]> FLOAT_ARRAYS = new HashMap<String,float[]>();
	public final HashMap<String,int[]> INT_ARRAYS = new HashMap<String,int[]>();
	public final HashMap<String,String[]> STRING_ARRAYS = new HashMap<String,String[]>();
	public final HashMap<String,boolean[]> BOOLEAN_ARRAYS = new HashMap<String,boolean[]>();
	
	public final HashMap<String,Byte> BYTES = new HashMap<String,Byte>();
	public final HashMap<String,Float> FLOATS = new HashMap<String,Float>();
	public final HashMap<String,Integer> INTS = new HashMap<String,Integer>();
	public final HashMap<String,String> STRINGS = new HashMap<String,String>();
	public final HashMap<String,Boolean> BOOLEANS = new HashMap<String,Boolean>();
	
	public final HashMap<String,NBT> NBTS = new HashMap<String,NBT>();
	
}