package net.tylermurphy.Minecraft.Render;

import java.util.List;

import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import net.tylermurphy.Minecraft.Render.Data.Vao;
import net.tylermurphy.Minecraft.Render.Shaders.FontShader;
import net.tylermurphy.Minecraft.Render.Shaders.QuadShader;
import net.tylermurphy.Minecraft.UI.UIFont;
import net.tylermurphy.Minecraft.UI.UIText;
import net.tylermurphy.Minecraft.Util.Maths;

public class FontRenderer {

	private final Vao quad;
	private FontShader fontShader;
	private QuadShader quadShader;

	public FontRenderer() {
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = Vao.loadToVAO(positions, 2);
		fontShader = new FontShader();
		quadShader = new QuadShader();
	}
	
	public void render(Map<UIFont, List<UIText>> texts){
		for(UIFont font : texts.keySet()){
			for(UIText text : texts.get(font)){
				renderQuad(text);
			}
		}
		prepare();
		for(UIFont font : texts.keySet()){
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			for(UIText text : texts.get(font)){
				renderText(text);
			}
		}
		endRendering();
	}

	public void cleanUp(){
		fontShader.cleanUp();
		quadShader.cleanUp();
	}
	
	private void prepare(){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		fontShader.start();
	}
	
	private void renderText(UIText text){
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		fontShader.loadColor(text.getColour());
		fontShader.loadTranslation(text.getConvertedPosition());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void renderQuad(UIText text) {
		quadShader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		Matrix4f matrix = Maths.createTransformationMatrix(new Vector2f(text.getConvertedPosition().x*2-1,(1-text.getConvertedPosition().y-.0125f*text.getFontSize()-.0025f)*2-1), new Vector2f((float) text.getMaxX()*2,.025f*text.getFontSize()));
		quadShader.loadTransformation(matrix);
		quadShader.loadColor(new Vector4f(150/255f,150/255f,150/255f,.5f));
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		quadShader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	private void endRendering(){
		fontShader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
