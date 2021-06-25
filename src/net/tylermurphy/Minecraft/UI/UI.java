package net.tylermurphy.Minecraft.UI;

import java.util.ArrayList;
import java.util.List;

public class UI {

		public boolean enabled = true;
		
		protected List<UIComponent> children = new ArrayList<UIComponent>();
		
		protected UIComponent findKey(String key) {
			for(UIComponent component : children) {
				UIComponent c = component.findKey(key);
				if(c!=null) return c;
			}
			return null;
		}
		
		protected UIText findText(String key) {
			return (UIText) findKey(key);
		}
		
		protected UIImage findImage(String key) {
			return (UIImage) findKey(key);
		}
		
		protected void add(UIComponent component) {
			children.add(component);
		};
		
		protected void prepare() {
			if(!enabled) return;
			for(UIComponent component : children)
				component.prepare(null);
		}
		
	}