package net.tylermurphy.Minecraft.UI;

import net.tylermurphy.Minecraft.Render.Data.Texture;
import net.tylermurphy.Minecraft.UI.Text.TextMeshCreator;
import net.tylermurphy.Minecraft.UI.Text.TextMeshData;

public class UIFont {

	private int textureAtlas;
	private TextMeshCreator loader;


	public UIFont(int textureAtlas, String fontFile) {
		this.textureAtlas = textureAtlas;
		this.loader = new TextMeshCreator(fontFile);
	}
	
	public UIFont(String font) {
		this.textureAtlas = Texture.loadFontAtlas(font);
		this.loader = new TextMeshCreator(font);
	}

	public int getTextureAtlas() {
		return textureAtlas;
	}

	public TextMeshData loadText(UIText text) {
		return loader.createTextMesh(text);
	}

}
