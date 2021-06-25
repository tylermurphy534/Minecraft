package net.tylermurphy.Minecraft.Scene.Objects;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Render.Data.Mesh;

public class SkinnedMesh extends GameObject implements Serializable {

	private static final long serialVersionUID = -5017401455504595815L;

	public SkinnedMesh(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(position, rotX, rotY, rotZ, scale);
	}
	
	public SkinnedMesh(Mesh mesh, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(position, rotX, rotY, rotZ, scale);
		this.mesh = mesh;
	}

	private Mesh mesh;
	
	public Mesh getMesh() {
		return mesh;
	}
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
}
