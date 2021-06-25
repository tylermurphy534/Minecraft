package net.tylermurphy.Minecraft.UI.UIFactory;

import java.util.HashMap;
import java.util.Map;

import net.tylermurphy.Minecraft.Render.Data.Texture;
import net.tylermurphy.Minecraft.UI.UIFont;

public class UIStore {

	public static Map<String,Integer> TEXTURES = new HashMap<String,Integer>(); 
	public static Map<String,UIFont> FONTS = new HashMap<String,UIFont>(); 
	
	public static void InitalizeStoreData() {
		
		FONTS.put("yugothic", new UIFont(Texture.loadFontAtlas("yugothic"),"yugothic"));
		TEXTURES.put("heart_full_texture",Texture.loadTexture("heart_full"));
		TEXTURES.put("heart_half_texture",Texture.loadTexture("heart_half"));
		TEXTURES.put("heart_empty_texture",Texture.loadTexture("heart_empty"));
		TEXTURES.put("crosshair",Texture.loadTexture("crosshair"));
		
	}
	
}
