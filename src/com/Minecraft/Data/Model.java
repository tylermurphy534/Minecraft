package com.Minecraft.Data;

import java.io.Serializable;

import com.Minecraft.Data.Vao;

public class Model implements Serializable {
	
	private static final long serialVersionUID = -8137846451745511907L;
	
	private Vao vao;
	private int texture;
	
	public Model(Vao vao, int texture){
		this.vao = vao;
		this.texture = texture;
	}

	public Vao getVao() {
		return vao;
	}

	public int getTexture() {
		return texture;
	}

}
