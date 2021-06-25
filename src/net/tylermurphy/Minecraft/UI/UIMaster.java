package net.tylermurphy.Minecraft.UI;

import java.util.ArrayList;
import java.util.List;

import net.tylermurphy.Minecraft.Render.FontRenderer;
import net.tylermurphy.Minecraft.Render.GuiRenderer;
import net.tylermurphy.Minecraft.UI.Text.TextMaster;

public class UIMaster {

	private static List<UI> uis = new ArrayList<UI>();
	private static int bindedID = 0;
	protected static List<UIComponent> componentBatch = new ArrayList<UIComponent>();
	
	public static void createUI(int id) {
		uis.add(id,new UI());
		bindedID = id;
	}
	
	private static UI getUI() {
		return uis.get(bindedID);
	}
	
	public static void add(UIComponent... components) {
		for(UIComponent component : components) {
			getUI().add(component);
		}
	}
	
	public static UIImage getImage(String key) {
		return (UIImage) getUI().findKey(key);
	}
	
	public static UIText getText(String key) {
		return (UIText) getUI().findKey(key);
	}
	
	public static void bindUI(int id) {
		bindedID = id;
	}
	
	public static void setEnabled(boolean value) {
		getUI().enabled = value;
	}
	
	public static boolean isEnabled() {
		return getUI().enabled;
	}
	
	public static void renderUI(GuiRenderer renderer, FontRenderer fontrenderer) {
		for(UI ui : uis) {
			if(!ui.enabled) continue;
			ui.prepare();
		}
		try{
			UILayerQuickSort.quickSort(componentBatch, 0, componentBatch.size()-1);
		} catch (Exception ignore) {}
		renderer.render(componentBatch);
		TextMaster.render(componentBatch,fontrenderer);
		componentBatch.clear();
	}
	
}

