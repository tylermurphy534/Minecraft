package com.Minecraft.Render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.Minecraft.Chunk.Chunk;
import com.Minecraft.Data.Model;
import com.Minecraft.Scene.Entity;
import com.Minecraft.Scene.Scene;
import com.Minecraft.Shaders.EntityShader;
import com.Minecraft.Util.Constants;
import com.Minecraft.Util.Display;
import com.Minecraft.Util.Frustum;
import com.Minecraft.Util.Maths;

public class SceneRenderer {

	private Matrix4f projectionMatrix;

	private EntityShader shader = new EntityShader();
	private EntityRenderer entityRenderer;
	
	private Map<Model, List<Entity>> entities = new HashMap<Model, List<Entity>>();
	
	public SceneRenderer() {
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(shader, projectionMatrix);
	}

	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}

	public void renderScene(Vector4f clipPlane) {
		if(Display.wasResized) {
			Display.wasResized = false;
			createProjectionMatrix();
		}
		Frustum frustum = Frustum.getFrustum(Maths.createViewMatrix(Scene.camera),projectionMatrix);
		for(Chunk chunk : Scene.chunks) {
			for (Entity entity : chunk.entities) {
				if(entity.visible == true) processEntity(entity);
			}
			if(chunk.mesh != null && frustum.cubeInFrustum(chunk.gridX*16 - Scene.world_origin.x, 0, chunk.gridZ*16 - Scene.world_origin.y, chunk.gridX*16 + 16 - Scene.world_origin.x , 256, chunk.gridZ*16 + 16 - Scene.world_origin.y)) processEntity(chunk.mesh);
		}
		for(Entity entity : Scene.global_entities) {
			if(entity.visible == true) processEntity(entity);
		}
		render();
	}

	private void render() {
		prepare();
		entityRenderer.render(entities);
		entities.clear();
	}

	private void processEntity(Entity entity) {
		Model entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(Constants.RED, Constants.GREEN, Constants.BLUE, 1);
	}

	private void createProjectionMatrix(){
    	projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(Constants.FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = Constants.FAR_DISTANCE - Constants.NEAR_DISTANCE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((Constants.FAR_DISTANCE + Constants.NEAR_DISTANCE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * Constants.NEAR_DISTANCE * Constants.FAR_DISTANCE) / frustum_length);
		projectionMatrix.m33 = 0;
    }

}
