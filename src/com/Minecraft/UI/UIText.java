package com.Minecraft.UI;

import org.lwjgl.util.vector.Vector3f;

import com.Minecraft.Data.Vao;
import com.Minecraft.UI.UIComponent;
import com.Minecraft.UI.UIFont;
import com.Minecraft.UI.Text.TextMaster;
import com.Minecraft.UI.Text.TextMeshData;


public class UIText extends UIComponent{

	private String textString;
	private float fontSize;

	private int textMeshVao;
	private int vertexCount;
	private Vector3f colour = new Vector3f(255f, 255f, 255f);
	
	private float lineMaxSize;
	private int numberOfLines;

	private UIFont font;

	private boolean centerText = false;

	public UIText(String text, float fontSize, UIFont font, float maxLineLength ,boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		TextMeshData data = font.loadText(this);
		int vao = Vao.loadToTextVAO(data.getVertexPositions(), data.getTextureCoords());
		setMeshInfo(vao, data.getVertexCount());
		TextMaster.loadText(this);
	}

	public void remove() {
		TextMaster.removeText(this);
	}

	public UIFont getFont() {
		return font;
	}

	public void setColour(float r, float g, float b) {
		colour.set(r, g, b);
	}

	public Vector3f getColour() {
		return colour;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}
	
	public int getMesh() {
		return textMeshVao;
	}

	public void setMeshInfo(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}

	public int getVertexCount() {
		return this.vertexCount;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	public boolean isCentered() {
		return centerText;
	}

	public float getMaxLineSize() {
		return lineMaxSize;
	}

	public String getTextString() {
		return textString;
	}

	public void setText(String textString) {
		this.textString = textString; 
		Vao.disposeVAO(textMeshVao);
		TextMeshData data = font.loadText(this);
		int vao = Vao.loadToTextVAO(data.getVertexPositions(), data.getTextureCoords());
		setMeshInfo(vao, data.getVertexCount());
	}

}
