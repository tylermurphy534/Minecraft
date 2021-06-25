package net.tylermurphy.Minecraft.Render;

import org.lwjgl.util.vector.Matrix4f;

import net.tylermurphy.Minecraft.Audio.SoundManager;
import net.tylermurphy.Minecraft.Render.Data.Texture;
import net.tylermurphy.Minecraft.Render.Data.Vao;
import net.tylermurphy.Minecraft.Render.Data.Vbo;
import net.tylermurphy.Minecraft.Scene.Scene;
import net.tylermurphy.Minecraft.UI.UIMaster;
import net.tylermurphy.Minecraft.Util.Constants;
import net.tylermurphy.Minecraft.Util.Display;
import net.tylermurphy.Minecraft.Util.MousePicker;

public class MainRenderer {
	
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;
	private ChunkRenderer chunkRenderer;
	
	private Matrix4f projectionMatrix;
	
	public MainRenderer() {
		guiRenderer = new GuiRenderer();
		fontRenderer = new FontRenderer();
		createProjectionMatrix();
		MousePicker.init(projectionMatrix);
		chunkRenderer = new ChunkRenderer(projectionMatrix);
	}
	
	public void update() {
		if(Display.wasResized) {
			Display.wasResized = false;
			createProjectionMatrix();
		}
		chunkRenderer.render(projectionMatrix);
		if(!Scene.currentScene.player.isDead) {
			Scene.currentScene.player.move();
			Scene.currentScene.player.setRotX(Scene.currentScene.camera.getPitch());
			Scene.currentScene.player.setRotY(-Scene.currentScene.camera.getYaw());
			Scene.currentScene.player.setRotZ(Scene.currentScene.camera.getRoll());
			Scene.currentScene.camera.move();
		}
		UIMaster.renderUI(guiRenderer, fontRenderer);
		MousePicker.update();
		SoundManager.setListenerData(Scene.currentScene.player.getPosition().x, Scene.currentScene.player.getPosition().y ,Scene.currentScene.player.getPosition().z);
	}
	
	public void close() {
		guiRenderer.cleanUp();
		chunkRenderer.cleanUp();
		Vao.cleanUp();
		Vbo.cleanUp();
		Texture.cleanUp();
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
	
	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}
	
}
