package net.tylermurphy.Minecraft.UI;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import net.tylermurphy.Minecraft.UI.UIComponent;
import net.tylermurphy.Minecraft.Util.Display;
import net.tylermurphy.Minecraft.Util.Maths;

public abstract class UIComponent {
	
	private List<UIComponent> children = new ArrayList<UIComponent>();

	private Vector4f size = new Vector4f(0,100,0,100);
	private Vector4f position = new Vector4f(0,0,0,0);
	
	protected boolean enabled = true;
	
	private String key;
	
	private int zIndex = 1;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public int getZIndex() {
		return zIndex;
	}

	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
	}
	
	public UIComponent findKey(String key) {
		if(this.key != null && this.key.equals(key)) return this;
		for(UIComponent component : children) {
			UIComponent c = component.findKey(key);
			if(c!=null) return c;
		}
		return null;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	};
	
	public void add(UIComponent component) {
		children.add(component);
	};
	
	protected List<UIComponent> getComponents() {
		return children;
	};
	
	public void prepare(UIComponent parent) {
		if(this.enabled) UIMaster.componentBatch.add(this);
		for(UIComponent component : children) {
			component.prepare(component);
		}
	}
	
	public void setSize(float xScale, float xOffset, float yScale, float yOffset) {
		this.size = new Vector4f(xScale,xOffset,yScale,yOffset);
	}
	
	public Vector4f getSize() {
		return size;
	}
	
	public void setPosition(float xScale, float xOffset, float yScale, float yOffset) {
		this.position = new Vector4f(xScale,xOffset,yScale,yOffset);
	}
	
	public Vector4f getPosition() {
		return position;
	}
	
	public Vector2f getConvertedSize() {
		return new Vector2f(
				size.x + size.y/Display.getWidth(),
				size.z + size.w/Display.getHeight()
				);
	}
	
	public Vector2f getConvertedPosition() {
		return new Vector2f(
				(position.x + position.y/Display.getWidth()),
				(position.w/Display.getHeight() + position.z)
				);
	}
	
	public Matrix4f getMatrix() {
		return Maths.createTransformationMatrix(new Vector2f(getConvertedPosition().x*2-1,getConvertedPosition().y*2-1), getConvertedSize());
	}
	
}
