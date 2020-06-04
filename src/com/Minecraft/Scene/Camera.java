package com.Minecraft.Scene;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector3f;

import com.Minecraft.Util.Display;
import com.Minecraft.Util.Input;

public class Camera implements Serializable {
	
	private static final long serialVersionUID = -2612670500762524881L;
	
	protected Vector3f position = new Vector3f(0, 0, 0);
	protected float pitch = 0;
	protected float yaw = 0;
	protected float roll = 0;
	
	public void move(){
		calculatePitch();
		calculateYaw();
		calculateCameraPosition();
		
		yaw%=360;
	}
	
	public void invertPitch(){
		this.pitch = -pitch;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	public void calculateCameraPosition(){
		position.x = Scene.player.getPosition().x;
		position.z = Scene.player.getPosition().z;
		position.y = Scene.player.getPosition().y + 10;
	}
	
	protected void calculatePitch(){
		if(!Display.closed()) {
			float pitchChange = (float) (Input.getMouseDY() * 0.3f);
			pitch -= pitchChange;
			if(pitch < -90){
				pitch = -90;
			}else if(pitch > 90){
				pitch = 90;
			}
		}
	}
	
	protected void calculateYaw(){
		if(!Display.closed()) {
			float angleChange = (float) (Input.getMouseDX() * 0.4f);
			yaw += angleChange;
		}
	}

}
