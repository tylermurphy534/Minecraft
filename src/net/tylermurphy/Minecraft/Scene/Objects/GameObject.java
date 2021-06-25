package net.tylermurphy.Minecraft.Scene.Objects;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Scene.Scene;

public class GameObject implements Serializable {

	private static final long serialVersionUID = 1325157934553886545L;
	
	public Vector3f position;
	public float rotX;
	public float rotY;
	public float rotZ;
	public float scale;
	public boolean visible = true;
	
	public Vector3f getGlobalPosition() {
		return new Vector3f(position.x+Scene.currentScene.world_origin.x,position.y,position.z+Scene.currentScene.world_origin.y);
	}
	
	public GameObject(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}
	
	public Vector3f getRotation() {
		return new Vector3f(rotX,rotY,rotZ);
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
}
