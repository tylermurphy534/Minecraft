package net.tylermurphy.Minecraft.Scene;

import java.io.Serializable;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Input.Input;
import net.tylermurphy.Minecraft.Scene.Objects.Entity;
import net.tylermurphy.Minecraft.Util.Display;

public class Player extends Entity implements Serializable {
	
	private static final long serialVersionUID = 5135364541978251987L;
	
	public int health = 20;
	
	public Player(float px, float py, float pz, float rx, float ry, float rz, float scale) {
		super(new Vector3f(px,py,pz), rx,ry,rz,scale);
	}

	public void move() {
		checkInputs();
		float distance = (float) (currentForwardSpeed * Display.getFrameTimeSeconds() * (isFlying == true ? 2 : 1) * ((isSwiming || isBobbing) && !isFlying  ? .25 : 1) );
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotation().y)));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotation().y)));
		float distance2 = (float) (currentSideSpeed * Display.getFrameTimeSeconds() * (isFlying == true ? 2 : 1) * ((isSwiming || isBobbing) && !isFlying  ? .25 : 1) );
		dx += (float) (distance2 * Math.sin(Math.toRadians(90-super.getRotation().y)));
		dz += (float) (distance2 * Math.cos(Math.toRadians(90+super.getRotation().y)));
		if(isInAir && !(isFlying || isSwiming)) upwardsSpeed += GRAVITY * Display.getFrameTimeSeconds();
		upwardsSpeed *= (isSwiming && !isFlying  ? .5 : 1);
		upwardsSpeed *= (isBobbing) ? -1 : 1;
		float dy = (float) (upwardsSpeed * Display.getFrameTimeSeconds());
		
		if(!willCollide(dx,0,0)) 
			super.increasePosition(dx, 0, 0);
		
		if(!willCollide(0,dy,0)) { 
			super.increasePosition(0, dy, 0); 
		}
		
		if(!willCollide(0,0,dz)) 
			super.increasePosition(0, 0, dz);
		
		if(super.getPosition().y < 0) {
			super.increasePosition(0, 256, 0);
		}
	}
	
	private void checkInputs() {
		int keys_pressed = 0;
		
		if(Input.isKeyDown(GLFW.GLFW_KEY_W)) {
			this.currentForwardSpeed = -RUN_SPEED; keys_pressed++;
		}else if(Input.isKeyDown(GLFW.GLFW_KEY_S)) {
			this.currentForwardSpeed = RUN_SPEED; keys_pressed++;
		}else {
			this.currentForwardSpeed = 0;
		}
		
		if(Input.isKeyDown(GLFW.GLFW_KEY_A)) {
			this.currentSideSpeed = -RUN_SPEED; keys_pressed++;
		}else if(Input.isKeyDown(GLFW.GLFW_KEY_D)) {
			this.currentSideSpeed = RUN_SPEED; keys_pressed++;
		}else {
			this.currentSideSpeed = 0;
		}
		
		if(keys_pressed == 2) {
			this.currentForwardSpeed /=1.25;
			this.currentSideSpeed /=1.25;
		}
		
		if(Input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
			this.currentForwardSpeed *= 1.5;
			this.currentSideSpeed *= 1.5;
		}
	
		if(Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			if(!isInAir && !(isFlying || isSwiming)) {
				this.upwardsSpeed = JUMP_POWER;
				isInAir = true;
			}else if(isFlying || isSwiming) {
				this.upwardsSpeed = JUMP_POWER;
			}
		} else if(Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) && isFlying || isSwiming && !isFlying) {
			this.upwardsSpeed = -JUMP_POWER;
		} else if(isFlying) {
			this.upwardsSpeed = 0;
		}
	}
	
}
