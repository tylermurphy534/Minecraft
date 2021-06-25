package net.tylermurphy.Minecraft.Input;

public interface IInput {
	public void keyPressed(int keyCode);
	public void keyRelesed(int keyCode);
	public void mousePressed(int mouseButton);
	public void mouseRelesed(int mouseButton);
	public void charAction(char c);
}