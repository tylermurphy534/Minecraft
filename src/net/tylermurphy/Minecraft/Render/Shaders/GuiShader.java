package net.tylermurphy.Minecraft.Render.Shaders;

import org.lwjgl.util.vector.Matrix4f;

import net.tylermurphy.Minecraft.Render.Util.ShaderProgram;

public class GuiShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "Gui_VS.glsl";
	private static final String FRAGMENT_FILE = "Gui_FS.glsl";
	
	private int location_transformationMatrix;

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	
	

}
