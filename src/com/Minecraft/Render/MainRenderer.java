package com.Minecraft.Render;

import org.lwjgl.util.vector.Vector4f;

import com.Minecraft.Audio.SoundManager;
import com.Minecraft.Data.Texture;
import com.Minecraft.Data.Vao;
import com.Minecraft.Data.Vbo;
import com.Minecraft.Scene.Scene;
import com.Minecraft.UI.UIMaster;
import com.Minecraft.Util.MousePicker;

public class MainRenderer {
	
	SceneRenderer sceneRenderer;
	GuiRenderer guiRenderer;
	FontRenderer fontRenderer;
	
	public MainRenderer() {
		sceneRenderer = new SceneRenderer();
		guiRenderer = new GuiRenderer();
		fontRenderer = new FontRenderer();
		MousePicker.init(sceneRenderer.getProjectionMatrix());
	}
	
	private void updatePlayer(){
		if(!Scene.player.isDead) {
			Scene.player.move();
			Scene.player.setRotX(Scene.camera.getPitch());
			Scene.player.setRotY(-Scene.camera.getYaw());
			Scene.player.setRotZ(Scene.camera.getRoll());
			Scene.camera.move();
		}
	}
	
	private void renderScene() {
		sceneRenderer.renderScene(new Vector4f(0, 1, 0, 100000));
	}
	
	private void renderGuis() {
		UIMaster.renderUI(guiRenderer, fontRenderer);
	}
	
	public void update() {
		renderScene();
		updatePlayer();
		renderGuis();
		MousePicker.update();
		SoundManager.setListenerData(Scene.player.getPosition().x, Scene.player.getPosition().y ,Scene.player.getPosition().z);
	}
	
	public void close() {
		guiRenderer.cleanUp();
		sceneRenderer.cleanUp();
		Vao.cleanUp();
		Vbo.cleanUp();
		Texture.cleanUp();
	}
	
}
