package net.tylermurphy.Minecraft.Util;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import net.tylermurphy.Minecraft.Input.Input;
import net.tylermurphy.Minecraft.Render.Data.Texture;
import net.tylermurphy.Minecraft.Util.Display;

public class Display {

	private static long monitor;
	private static long window;
	
	private static int width,height;
	
	private static double previousTime = GLFW.glfwGetTime();
	private static double passedTime = 0;
	
	private static boolean contextReady = false;
	
	public static boolean wasResized = false;
	
	public static boolean fullscreen = false;
	private static int backup_xpos,backup_ypos,backup_xsize,backup_ysize;
	
	public static void create(int width, int height, String title, Texture icon) {
		
		Display.width = width;
		Display.height = height;
		
		if(!GLFW.glfwInit()) {
			System.err.println("Error: Coudnt init GLFW");
			System.exit(-1);
		}
		
		Input.init();
		
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		
		monitor = GLFW.glfwGetPrimaryMonitor();
		window = GLFW.glfwCreateWindow(width, height, title, 0, 0);
		
		GLFW.glfwSetKeyCallback(window, Input.getKeyboardCallback());
		GLFW.glfwSetCursorPosCallback(window, Input.getMouseMoveCallback());
		GLFW.glfwSetMouseButtonCallback(window, Input.getMouseButtonCallback());
		GLFW.glfwSetCharCallback(window, Input.getCharCallback());
		
		GLFWFramebufferSizeCallback framebufferCallback = new GLFWFramebufferSizeCallback() {
			public void invoke(long window, int width, int height) {
				if(contextReady) {
					Display.width = width;
					Display.height = height;
					Display.wasResized = true;
					GL11.glViewport(0, 0, width, height);
				}
			}
		};
		
		GLFW.glfwSetFramebufferSizeCallback(window, framebufferCallback);
		
		if(window==0) {
			System.err.println("Error: Coudnt create window");
			System.exit(-1);
		}
		
		if(icon != null) {
			GLFWImage image = GLFWImage.malloc(); GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
			image.set(icon.getWidth(), icon.getHeight(), icon.getImage());
			imagebf.put(0, image);
			GLFW.glfwSetWindowIcon(window, imagebf);
		}
		
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(0);
		GLFW.glfwShowWindow(window);
		
		GL.createCapabilities();
		grabCursor();
		
		contextReady = true;
		
	}
	
	public static boolean closed() {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public static void update() {
		double currentTime = GLFW.glfwGetTime();
		passedTime = currentTime - previousTime;
		previousTime = currentTime;
		GLFW.glfwPollEvents(); 
	}
	
	public static void swapBuffers() {
		GLFW.glfwSwapBuffers(window);
	}
	
	public static void close() {
		Input.destroy();
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		GL.destroy();
	}
	
	public static int getWidth() {
		return width;
	}
	
	public static int getHeight() {
		return height;
	}
	
	public static float getFrameTimeSeconds() {
		return (float) passedTime;
	}
	
	public static void grabCursor() {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	}
	
	public static void releaseCursor() {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}
	
	public static Vector2f getCursorPos() {
		DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(window, xpos, ypos);
		return new Vector2f((float)xpos.get(),(float)ypos.get());
	}
	
	public static void setFullScreen(boolean fullscreen) {
		
		if(Display.fullscreen == fullscreen) 
			return;
		
		Display.fullscreen = fullscreen;
		
		if(fullscreen) {
			
			IntBuffer xpos = BufferUtils.createIntBuffer(1);
			IntBuffer ypos = BufferUtils.createIntBuffer(1);
			GLFW.glfwGetWindowPos(window, xpos, ypos);
			
			IntBuffer xsize = BufferUtils.createIntBuffer(1);
			IntBuffer ysize = BufferUtils.createIntBuffer(1);
			GLFW.glfwGetWindowSize(window, xsize, ysize);
			
			backup_xpos = xpos.get();
			backup_ypos = ypos.get();
			backup_xsize = xsize.get();
			backup_ysize = ysize.get();
			
			GLFWVidMode mode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			
			GLFW.glfwSetWindowMonitor(window, monitor, backup_xpos, backup_ypos, mode.width(), mode.height(), 0);
		} else {
			GLFW.glfwSetWindowMonitor(window, 0, backup_xpos, backup_ypos, backup_xsize, backup_ysize, 0);
		}
	}
	
}
