package net.tylermurphy.Minecraft.Render.Data;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.tylermurphy.Minecraft.Render.Data.Vao;

public class Vao {
	
	private int vaoID;
	private int vertexCount;
	
	public static List<Integer> vaos = new ArrayList<Integer>();

	public Vao(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	public static int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public static Vao loadToVAO_POS_UV_IND(float[] positions, float[] textureCoords, int[] indices) {
		int vaoID = createVAO();
		AttributeLoader.bindIndicesBuffer(indices);
		AttributeLoader.storeDataInAttributeList(0,3,positions);
		AttributeLoader.storeDataInAttributeList(1,2,textureCoords);
		AttributeLoader.unbindVAO();
		return new Vao(vaoID,indices.length);
	}
	
	public static Vao loadToVAO_POS_UV(float[] positions, float[] textureCoords){
		int vaoID = createVAO();
		AttributeLoader.storeDataInAttributeList(0,3,positions);
		AttributeLoader.storeDataInAttributeList(1,2,textureCoords);
		AttributeLoader.unbindVAO();
		return new Vao(vaoID, positions.length);
	}
	
	public static Vao loadToVAO_POS_UV_BLV(float[] positions, float[] textureCoords, float[] baseLightValue){
		int vaoID = createVAO();
		AttributeLoader.storeDataInAttributeList(0,3,positions);
		AttributeLoader.storeDataInAttributeList(1,2,textureCoords);
		AttributeLoader.storeDataInAttributeList(2,1,baseLightValue);
		AttributeLoader.unbindVAO();
		return new Vao(vaoID, positions.length);
	}
	
	public static int loadToTextVAO(float[] positions, float[] textureCoords){
		int vaoID = createVAO();
		AttributeLoader.storeDataInAttributeList(0,2,positions);
		AttributeLoader.storeDataInAttributeList(1,2,textureCoords);
		AttributeLoader.unbindVAO();
		return vaoID;
	}
	
	public static Vao loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		AttributeLoader.storeDataInAttributeList(0,dimensions,positions);
		AttributeLoader.unbindVAO();
		return new Vao(vaoID,positions.length/dimensions);
	}
	
	public static void disposeVAO(int id) {
		IntBuffer amount = BufferUtils.createIntBuffer(1);
		GL20.glGetIntegerv(GL20.GL_MAX_VERTEX_ATTRIBS, amount);
		GL30.glBindVertexArray(id);
		for (int i=0; i < amount.get(0); i++) {
			IntBuffer vbo = BufferUtils.createIntBuffer(1);
			GL20.glGetVertexAttribiv(i, GL20.GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING, vbo);
			if(vbo.get(0) > 0) {
				GL15.glDeleteBuffers(vbo.get(0));
			}
		}
		GL30.glDeleteVertexArrays(id);
	}
	
	public static void cleanUp() {
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
	}

}
