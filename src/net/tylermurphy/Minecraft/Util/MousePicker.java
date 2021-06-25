package net.tylermurphy.Minecraft.Util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Chunk.Cube;
import net.tylermurphy.Minecraft.Scene.Scene;

public class MousePicker {
	
	private static Vector3f currentRay = new Vector3f(0,0,0);
	public static Vector3f breakPos = new Vector3f();
	public static Vector3f placePos = new Vector3f();
	
	private static Matrix4f projectionMatrix;
	private static Matrix4f viewMatrix;
	
	public static void init(Matrix4f projection) {
		projectionMatrix = projection;
	}

	public static void update() {
		viewMatrix = Maths.createViewMatrix(Scene.currentScene.camera);
		currentRay = calculateMouseRay();
		breakPos = search(currentRay,Scene.currentScene.player.getPosition(), 0);
		if(breakPos == null) placePos = null;
	}

	private static Vector3f calculateMouseRay() {
		float mouseX = (float) Display.getWidth()/2;
		float mouseY = (float) Display.getHeight()/2;
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}

	private static Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}

	private static  Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	private static Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / Display.getWidth() - 1f;
		float y = (2.0f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}
	
	private static Vector3f search(Vector3f ray, Vector3f position, int recursionCount) {
		Vector3f ppos = Scene.currentScene.player.getPosition();
		if(Math.abs(position.x-ppos.x) > 4 || Math.abs(position.y-ppos.y) > 4 || Math.abs(position.z-ppos.z) > 4 ) return null;
		if(recursionCount > 200) return null;
		int intX = Math.round(position.x);
		int intY = Math.round(position.y+1);
		int intZ = Math.round(position.z);
		if(Chunk.getBlock(intX, intY, intZ) != Cube.AIR && Chunk.getBlock(intX, intY, intZ) != 17)
			return new Vector3f(intX,intY,intZ);
		placePos = new Vector3f(intX,intY,intZ);
		return search(ray,new Vector3f(position.x+ray.x/4,position.y+ray.y/4,position.z+ray.z/4), recursionCount++);
	}

}
