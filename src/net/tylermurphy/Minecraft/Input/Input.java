package net.tylermurphy.Minecraft.Input;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import org.lwjgl.glfw.GLFWCharCallback;

public class Input {
	
	private static List<IInput> inputs = new ArrayList<IInput>();
	
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	private static boolean[] mouseButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	private static double mouseX, mouseY;
	private static double mouseX_last, mouseY_last;
	
	private static GLFWKeyCallback keyboard;
	private static GLFWCursorPosCallback mouseMove;
	private static GLFWMouseButtonCallback mouseButton;
	private static GLFWCharCallback character; 
	
	public static void init() {
		
		keyboard = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				switch(action) {
				case GLFW.GLFW_PRESS:
					keys[key] = true;
					for(IInput input : inputs) input.keyPressed(key);
					break;
				case GLFW.GLFW_RELEASE:
					keys[key] = false;
					for(IInput input : inputs) input.keyRelesed(key);
					break;
				}
				
			}
		};
		
		mouseMove = new GLFWCursorPosCallback() {
			public void invoke(long window, double xPos, double yPos) {
				mouseX = xPos; mouseY = yPos;
			}
		};
		
		mouseButton = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				switch(action) {
				case GLFW.GLFW_PRESS:
					mouseButtons[button] = true;
					for(IInput input : inputs) input.mousePressed(button);
					break;
				case GLFW.GLFW_RELEASE:
					mouseButtons[button] = false;
					for(IInput input : inputs) input.mouseRelesed(button);
					break;
				}
			}
		};
		
		character = new GLFWCharCallback() {
			public void invoke(long window, int key) {
				for(IInput input : inputs) input.charAction((char)key);
			}
		};
	}
	
	public static void addInput(IInput input) {
		inputs.add(input);
	}
	
	public static void destroy() {
		keyboard.free();
		mouseMove.free();
		mouseButton.free();
		character.free();
	}
	
	public static boolean isKeyDown(int key) {
		return keys[key];
	}
	
	public static boolean isButtonDown(int button) {
		return mouseButtons[button];
	}

	public static double getMouseX() {
		return mouseX;
	}

	public static double getMouseY() {
		return mouseY;
	}
	
	public static double getMouseDX() {
		double d = -(mouseX_last - mouseX);
		mouseX_last = mouseX;
		return d;
	}
	
	public static double getMouseDY() {
		double d = mouseY_last - mouseY;
		mouseY_last = mouseY;
		return d;
	}

	public static GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

	public static GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}

	public static GLFWMouseButtonCallback getMouseButtonCallback() {
		return mouseButton;
	}
	
	public static GLFWCharCallback getCharCallback() {
		return character;
	}

	
}
