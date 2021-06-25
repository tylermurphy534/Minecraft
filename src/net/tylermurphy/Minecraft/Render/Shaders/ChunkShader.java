package net.tylermurphy.Minecraft.Render.Shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Render.Util.ShaderProgram;
import net.tylermurphy.Minecraft.Scene.Camera;
import net.tylermurphy.Minecraft.Util.Maths;

public class ChunkShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "Chunk_VS.glsl";
	private static final String FRAGMENT_FILE = "Chunk_FS.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_modelTexture;
	private int location_tint;
	private int location_playerPosition;
	private int location_renderDistance;
	private int location_skyColor;
	
	public ChunkShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "baseLightingValue");
	}

	@Override
	public void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_modelTexture = super.getUniformLocation("modelTexture");
		location_tint = super.getUniformLocation("tint");
		location_playerPosition = super.getUniformLocation("playerPosition");
		location_renderDistance = super.getUniformLocation("renderDistance");
		location_skyColor = super.getUniformLocation("skyColor");
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_modelTexture, 0);
	}
	
	public void loadTint(Vector3f tint) {
		super.loadVector(location_tint, tint);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadPlayerPosition(Vector3f position) {
		super.loadVector(location_playerPosition, position);
	}
	
	public void loadRenderDistance(int distance) {
		super.loadInt(location_renderDistance, distance);
	}
	
	public void loadSkyColor(Vector3f color) {
		super.loadVector(location_skyColor, color);
	}

}
