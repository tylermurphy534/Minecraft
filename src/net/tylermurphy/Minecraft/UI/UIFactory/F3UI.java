package net.tylermurphy.Minecraft.UI.UIFactory;

import static net.tylermurphy.Minecraft.UI.UIMaster.add;
import static net.tylermurphy.Minecraft.UI.UIMaster.createUI;
import static net.tylermurphy.Minecraft.UI.UIMaster.setEnabled;

import net.tylermurphy.Minecraft.Render.Data.Texture;
import net.tylermurphy.Minecraft.UI.UIFont;
import net.tylermurphy.Minecraft.UI.UIText;

public class F3UI {

	static UIFont font;
	
	public static void initF3UI() {
		
		font = new UIFont(Texture.loadFontAtlas("yugothic"),"yugothic");
		
		createUI(1);
		setEnabled(false);
		
		String maxMem = "Max memory (bytes): " + Runtime.getRuntime().maxMemory();
		String freeMem = "Free memory (bytes): " + Runtime.getRuntime().freeMemory();
		String allocMem = "Allocated memory (bytes): " + Runtime.getRuntime().totalMemory();
		String cores = "Available processors (cores): "+ Runtime.getRuntime().availableProcessors();
		
		UIText maxMemText = new UIText(maxMem,1,font,1000,false);
		maxMemText.setPosition(0, 0, .125f, 0);
		UIText freeMemText = new UIText(freeMem,1,font,1000,false);
		freeMemText.setPosition(0, 0, .15f, 0);
		freeMemText.setKey("freemem");
		UIText allocMemText = new UIText(allocMem,1,font,1000,false);
		allocMemText.setPosition(0, 0, .175f, 0);
		UIText coresText = new UIText(cores,1,font,1000,false);
		coresText.setPosition(0, 0, .2f, .2f);
		
		UIText position = new UIText("",1,font,1000,false);
		position.setPosition(0, 0, 0, 0);
		position.setKey("position");
		UIText rotation = new UIText("",1,font,1000,false);
		rotation.setPosition(0, 0, .025f, 0);
		rotation.setKey("rotation");
		UIText fps = new UIText("",1,font,1000,false);
		fps.setPosition(0, 0, .05f, 0);
		fps.setKey("fps");
		UIText tps = new UIText("",1,font,1000,false);
		tps.setPosition(0, 0, 0.075f, 0);
		tps.setKey("tps");
		
		UIText block = new UIText("Block Selected: minecraft:dirt",1,font,1000,false);
		block.setPosition(0, 0, .25f, 0);
		block.setKey("block");
		
		add(maxMemText,freeMemText,allocMemText,coresText,position,rotation,fps,block,tps);
	}
	
}
