package com.Minecraft.Render;

import java.util.List;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.Minecraft.Data.Vao;
import com.Minecraft.Shaders.GuiShader;
import com.Minecraft.UI.UIComponent;
import com.Minecraft.UI.UIImage;
import com.Minecraft.Util.Maths;


public class GuiRenderer {

	private final Vao quad;
	private GuiShader shader;
	
	public GuiRenderer(){
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = Vao.loadToVAO(positions, 2);
		shader = new GuiShader();
	}
	
	public void render(List<UIComponent> components){
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(UIComponent cp : components) {
			if(!(cp instanceof UIImage)) continue;
			UIImage gui = (UIImage)cp;
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(new Vector2f(gui.getConvertedPosition().x*2-1,(1-gui.getConvertedPosition().y)*2-1), gui.getConvertedSize());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
}
