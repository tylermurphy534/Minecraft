package net.tylermurphy.Minecraft.Render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Render.Shaders.ChunkShader;
import net.tylermurphy.Minecraft.Render.Util.FrustumCuller;
import net.tylermurphy.Minecraft.Scene.Scene;
import net.tylermurphy.Minecraft.Scene.Objects.SkinnedMesh;
import net.tylermurphy.Minecraft.Util.Constants;
import net.tylermurphy.Minecraft.Util.Maths;

public class ChunkRenderer {

	private ChunkShader shader;
	
	public ChunkRenderer(Matrix4f projectionMatrix) {
		this.shader = new ChunkShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(Matrix4f projectionMatrix) {
		FrustumCuller frustum = FrustumCuller.getFrustum(Maths.createViewMatrix(Scene.currentScene.camera),projectionMatrix);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(Constants.RED, Constants.GREEN, Constants.BLUE, 1);
		shader.start();
		if(Scene.currentScene.player.isDead)
			shader.loadTint(new Vector3f(100,0,0));
		else
			shader.loadTint(new Vector3f(0,0,0));
		shader.loadViewMatrix(Scene.currentScene.camera);
		shader.loadPlayerPosition(Scene.currentScene.player.getPosition());
		shader.loadRenderDistance(Chunk.RENDER_DISTANCE);
		shader.loadSkyColor(new Vector3f(Constants.RED,Constants.GREEN,Constants.BLUE));
		for (Chunk chunk : Scene.currentScene.chunks.values()) {
			if(chunk==null) continue;
			if(!frustum.cubeInFrustum( chunk.gridX*16 - Scene.currentScene.world_origin.x, 0,  chunk.gridZ*16 - Scene.currentScene.world_origin.y, chunk.gridX*16 + 16 - Scene.currentScene.world_origin.x , 256,  chunk.gridZ*16 + 16 - Scene.currentScene.world_origin.y )) {
				continue;
			}
			chunk.loadMesh();
			if(chunk.opaque_mesh != null) 
				renderChunkMesh(chunk.opaque_mesh);
			if(chunk.transparent_mesh != null) 
				renderChunkMesh(chunk.transparent_mesh);
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
			GL30.glBindVertexArray(0);
		}
		GL11.glDisable(GL11.GL_CULL_FACE);
		shader.stop();
	}
	
	private void renderChunkMesh(SkinnedMesh skinedMesh) {
		GL30.glBindVertexArray(skinedMesh.getMesh().getVao().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, skinedMesh.getMesh().getTexture());
		prepareInstance(skinedMesh);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, skinedMesh.getMesh().getVao().getVertexCount());
	}
	
	private void prepareInstance(SkinnedMesh skinedMesh) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				new Vector3f(
						skinedMesh.getPosition().x - Scene.currentScene.world_origin.x,
						skinedMesh.getPosition().y,
						skinedMesh.getPosition().z - Scene.currentScene.world_origin.y
				),
				skinedMesh.getRotX(), skinedMesh.getRotY(), skinedMesh.getRotZ(), skinedMesh.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

}
