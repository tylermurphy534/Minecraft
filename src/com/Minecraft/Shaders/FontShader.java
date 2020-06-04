package com.Minecraft.Shaders;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.Minecraft.Shaders.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE ="Font_VS.glsl";
	private static final String FRAGMENT_FILE = "Font_FS.glsl";
	
	private int location_color;
	private int location_translation;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_color = super.getUniformLocation("color");
		location_translation = super.getUniformLocation("translation");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	public void loadColor(Vector3f colour){
		super.loadVector(location_color, colour);
	}
	
	public void loadTranslation(Vector2f vector2f){
		super.load2DVector(location_translation, vector2f);
	}
	
}
