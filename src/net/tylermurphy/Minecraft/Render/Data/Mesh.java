package net.tylermurphy.Minecraft.Render.Data;

import java.io.Serializable;

public class Mesh implements Serializable {
	
	private static final long serialVersionUID = -8137846451745511907L;
	
	private Vao vao;
	private int texture;
	private int drawMode = 0;
	
	public Mesh(Vao vao, int texture, int drawMode){
		this.vao = vao;
		this.texture = texture;
		this.drawMode = drawMode;
	}

	public Vao getVao() {
		return vao;
	}

	public int getTexture() {
		return texture;
	}
	
	public int getDrawMode() {
		return drawMode;
	}

}
