package net.tylermurphy.Minecraft.Audio;

import java.util.ArrayList;

import java.util.List;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

public class SoundManager {

    private static long device;
    private static long context;
    private static List<Sound> sounds;
    
    public static void init() {
    	String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
    	device = ALC10.alcOpenDevice(defaultDeviceName);
    	int[] attributes = {0};
    	context = ALC10.alcCreateContext(device, attributes);
    	ALC10.alcMakeContextCurrent(context);
    	ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
    	ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
    	if(alCapabilities.OpenAL10) {
    	    //OpenAL 1.0 is supported
    	}
    	sounds = new ArrayList<Sound>();
    }
    
    public static Sound loadSound(String fileName) {
    	Sound sound = new Sound(fileName);
    	sounds.add(sound);
    	return sound;
    }
    
    public static void cleanUp() {
    	for(Sound sound : sounds) {
    		sound.cleanUp();
    	}
    	ALC10.alcDestroyContext(context);
    	ALC10.alcCloseDevice(device);
    }
    
    public static void setListenerData(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
}