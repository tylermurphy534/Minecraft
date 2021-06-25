package net.tylermurphy.Minecraft.Render.Shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import net.tylermurphy.Minecraft.Render.Util.ShaderProgram;

public class QuadShader extends ShaderProgram{

	private static final String VERTEX_FILE = "Quad_VS.glsl";
	private static final String FRAGMENT_FILE = "Quad_FS.glsl";
	
	private int location_transformationMatrix;
	private int location_color;

	public QuadShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_color = super.getUniformLocation("color");
	}

	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void loadColor(Vector4f color) {
		super.loadVector(location_color, color);
	}

}
