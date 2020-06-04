package com.Minecraft.Scene;

import java.io.Serializable;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;

import com.Minecraft.Chunk.Chunk;
import com.Minecraft.Chunk.Cube;
import com.Minecraft.Data.Model;
import com.Minecraft.Util.Display;
import com.Minecraft.Util.Input;

import com.Minecraft.Scene.Entity;

public class Player extends Entity implements Serializable {
	
	private static final long serialVersionUID = 5135364541978251987L;
	
	private static final float RUN_SPEED = 4;
	private static final float JUMP_POWER = 6;
	private static final float GRAVITY = -15;
	
	private float currentForwardSpeed = 0;
	private float currentSideSpeed = 0;
	private float upwardsSpeed = 0;
	
	public boolean isInAir = true;
	public boolean isFlying = false;
	public boolean isSwiming = false;
	public boolean isFalling = false;
	public boolean wasFalling = false;
	public boolean isDead = false;
	
	public int health = 20;
	
	public Player(Model model, float px, float py, float pz, float rx, float ry, float rz, float scale) {
		super(model, new Vector3f(px,py,pz), rx,ry,rz,scale);
	}

	public void move() {
		checkInputs();
		float distance = (float) (currentForwardSpeed * Display.getFrameTimeSeconds() * (isFlying == true ? 2 : 1) * (isSwiming && !isFlying  ? .25 : 1) );
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotation().y)));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotation().y)));
		float distance2 = (float) (currentSideSpeed * Display.getFrameTimeSeconds() * (isFlying == true ? 2 : 1) * (isSwiming && !isFlying  ? .25 : 1) );
		dx += (float) (distance2 * Math.sin(Math.toRadians(90-super.getRotation().y)));
		dz += (float) (distance2 * Math.cos(Math.toRadians(90+super.getRotation().y)));
		if(isInAir && !(isFlying || isSwiming)) upwardsSpeed += GRAVITY * Display.getFrameTimeSeconds();
		upwardsSpeed *= (isSwiming && !isFlying  ? .25 : 1);
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
	
	private boolean willCollide(float dx, float dy, float dz) {
		float px = getPosition().x;
		float py = getPosition().y;
		float pz = getPosition().z;
		if(getPosition().x<0)
			px--;
		if(getPosition().z<0)
			pz--;
		int[] nbc = {
				(int) (px+dx+.25f),
				(int) (py+dy),
				(int) (pz+dz+.25f),
				(int) (px+dx+.75f),
				(int) (py+dy+1.9f),
				(int) (pz+dz+.75f)
			};
		
		for(int x = nbc[0]; x<=nbc[3]; x++) {
			for(int y = nbc[1]; y<=nbc[4]; y++) {
				for(int z = nbc[2]; z<=nbc[5]; z++) {
					byte block = Chunk.getBlock(x,y,z);
					byte block_below = Chunk.getBlock(x,y-2,z);
					if(block == 17 || block_below == 17) isSwiming = true;
					else isSwiming = false;
					if(dy != 0) {
						if(block != Cube.AIR && block != 17) {
							if(y <= getPosition().y) isInAir = false;
							else isInAir = true;
							return true;
						} else isInAir = true;
					}else {
						if(block != Cube.AIR && block != 17) return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean collides(float x, float y, float z) {
		float px = getPosition().x;
		float py = getPosition().y;
		float pz = getPosition().z;
		if(getPosition().x<0)
			px--;
		if(getPosition().z<0)
			pz--;
		int[] nbc = {
				(int) (px+.25f),
				(int) (py),
				(int) (pz+.25f),
				(int) (px+.75f),
				(int) (py+1.9f),
				(int) (pz+.75f)
			};
		if(
				x >= nbc[0] && x <= nbc[3] &&
				y >= nbc[1] && y <= nbc[4] &&
				z >= nbc[2] && z <= nbc[5]
			) return true;
		return false;
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
