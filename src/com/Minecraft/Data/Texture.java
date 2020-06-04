package com.Minecraft.Data;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import com.Minecraft.Util.Constants;

import com.Minecraft.Data.Texture;

public class Texture {
	
    public ByteBuffer getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return heigh;
    }

    private ByteBuffer image;
    private int width, heigh;

    private static List<Integer> ids = new ArrayList<Integer>();
    
    Texture(int width, int heigh, ByteBuffer image) {
        this.image = image;
        this.heigh = heigh;
        this.width = width;
    }
    
    public static Texture loadRawTextureData(String fileName) {
        ByteBuffer image;
        int width, heigh;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            image = STBImage.stbi_load(Constants.PNG_LOCATION + fileName + ".png", w, h, comp, 4);
            if (image == null) {
                System.err.println("Couldn't load " + Constants.PNG_LOCATION + fileName + ".png");
            }
            width = w.get();
            heigh = h.get();
            return new Texture(width, heigh, image);
        }
    }
    
    private static int loadImage(String fileName, float loadBias) {
    	int id = GL11.glGenTextures();
    	Texture image = loadRawTextureData(fileName);
    	GL13.glActiveTexture(GL13.GL_TEXTURE0);
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image.getImage());
    	GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, loadBias);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		ids.add(id);
		return id;
    }
    
    public static int loadTexture(String fileName) {
    	return loadImage(fileName, 0);
    }
    
    public static int loadFontAtlas(String fileName) {
    	return loadImage(fileName, 0);
    }
    
    public static int loadCubeMap(String[] textureFiles) {
		int id = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, id);
		for(int i=0;i<textureFiles.length;i++) {
			Texture data = loadRawTextureData(textureFiles[i]);
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getImage());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		ids.add(id);
		return id;
	}
    
    public static void cleanUp(){
		for(int id:ids){
			GL11.glDeleteTextures(id);
		}
	}
}