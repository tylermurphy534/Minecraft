package net.tylermurphy.Minecraft.Scene.Objects;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Chunk.Cube;
import net.tylermurphy.Minecraft.Render.Data.Mesh;

public class Entity extends SkinnedMesh implements Serializable{

	private static final long serialVersionUID = -1273546184611580473L;
	
	public int health;
	
	protected static final float RUN_SPEED = 4;
	protected static final float JUMP_POWER = 6;
	protected static final float GRAVITY = -15;
	
	protected float currentForwardSpeed = 0;
	protected float currentSideSpeed = 0;
	protected float upwardsSpeed = 0;
	
	public boolean isInAir = true;
	public boolean isFlying = false;
	public boolean isSwiming = false;
	public boolean isBobbing = false;
	public boolean isFalling = false;
	public boolean wasFalling = false;
	public boolean isDead = false;
	
	public Entity(Mesh mesh, Vector3f position, float rotX, float rotY, float rotZ,float scale) {
		super(mesh,position,rotX,rotY,rotZ,scale);
	}
	
	public Entity(Vector3f position, float rotX, float rotY, float rotZ,float scale) {
		super(null,position,rotX,rotY,rotZ,scale);
	}
	
	public boolean willCollide(float dx, float dy, float dz) {
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
					byte block_head = Chunk.getBlock(x,y,z);
					byte block_current_feet = Chunk.getBlock(x,(int)(py),z);
					byte block_current_upper = Chunk.getBlock(x,(int)(py+1f),z);
					byte block_current_lower = Chunk.getBlock(x,(int)(py+.75f),z);
					if(block_current_feet == 17 ) {
						isSwiming = true;
						isBobbing = false;
					}
					if(block_current_feet == 17 && block_current_lower == 17 && block_current_upper == Cube.AIR){
						isSwiming = false;
						isBobbing = true;
					}
					if(block_current_feet != 17) {
						isSwiming = false;
						isBobbing = false;
					}
					if(dy != 0) {
						if(block_head != Cube.AIR && block_head != 17) {
							if(y <= getGlobalPosition().y) isInAir = false;
							else isInAir = true;
							return true;
						} else isInAir = true;
					}else {
						if(block_head != Cube.AIR && block_head != 17) return true;
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

}
