package com.Minecraft.UI;

import org.lwjgl.util.vector.Vector2f;

import com.Minecraft.Util.Display;

import com.Minecraft.UI.UIComponent;

public class UIImage extends UIComponent {

	private int texture;
	
	public void setTexture(int texture) {
		this.texture = texture;
	}
	
	public int getTexture() {
		return texture;
	}
	
	public boolean clicked() {
		Vector2f pos = Display.getCursorPos();
		float x1 = super.getConvertedPosition().x*Display.getWidth();
		float y1 = super.getConvertedPosition().y*Display.getHeight();
		float x2 = super.getConvertedPosition().x*Display.getWidth()+super.getConvertedSize().x*Display.getWidth();
		float y2 = super.getConvertedPosition().y*Display.getHeight()+super.getConvertedSize().y*Display.getHeight();
		if(pos.x >= x1 && pos.x <= x2 && pos.y >= y1 && pos.y <= y2)
			return true;
		return false;
	}
	
}
