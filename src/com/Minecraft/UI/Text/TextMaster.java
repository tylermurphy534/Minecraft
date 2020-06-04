package com.Minecraft.UI.Text;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Minecraft.Render.FontRenderer;
import com.Minecraft.UI.UIComponent;
import com.Minecraft.UI.UIFont;
import com.Minecraft.UI.UIText;

public class TextMaster {
	
	private static Map<UIFont, List<UIText>> texts = new HashMap<UIFont, List<UIText>>();

	private static void proccessText(UIText text) {
		UIFont fontType = text.getFont();
		List<UIText> batch = texts.get(fontType);
		if (batch != null) {
			batch.add(text);
		} else {
			List<UIText> newBatch = new ArrayList<com.Minecraft.UI.UIText>();
			newBatch.add(text);
			texts.put(fontType, newBatch);
		}
	}
	
	public static void render(List<UIComponent> components,FontRenderer fontrenderer){
		for(UIComponent component : components) {
			UIText text;
			if(component instanceof UIText) {
				text = (UIText)component;
			} else {
				continue;
			}
			loadText(text);
			proccessText(text);
		}
		fontrenderer.render(TextMaster.texts);
		TextMaster.texts.clear();
	}
	
	public static void loadText(UIText text){
		UIFont font = text.getFont();
		List<UIText> textBatch = texts.get(font);
		if(textBatch == null){
			textBatch = new ArrayList<UIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removeText(UIText text){
		List<UIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()){
			texts.remove(text.getFont());
		}
	}

}
