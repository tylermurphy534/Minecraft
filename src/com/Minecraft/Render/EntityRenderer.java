package com.Minecraft.Render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.Minecraft.Data.Model;
import com.Minecraft.Data.Vao;
import com.Minecraft.Scene.Entity;
import com.Minecraft.Scene.Scene;
import com.Minecraft.Shaders.EntityShader;
import com.Minecraft.Util.Maths;

public class EntityRenderer {

	private EntityShader shader;
	
	public EntityRenderer(EntityShader shader,Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(Map<Model, List<Entity>> entities) {
		shader.start();
		if(Scene.player.isDead)
			shader.loadTint(new Vector3f(100,0,0));
		else
			shader.loadTint(new Vector3f(0,0,0));
		shader.loadViewMatrix(Scene.camera);
		for (Model model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				if(entity.drawMode == 0 ) GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVao().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				else GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVao().getVertexCount());
			}
			unbindModel();
		}
		shader.stop();
	}
	
	private void prepareTexturedModel(Model model) {
		Vao rawModel = model.getVao();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture());
	}

	private void unbindModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(entity.getPosition().x-Scene.world_origin.x,entity.getPosition().y,entity.getPosition().z-Scene.world_origin.y),entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
