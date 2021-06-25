package net.tylermurphy.Minecraft.Audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.system.*;

import net.tylermurphy.Minecraft.Util.Constants;

import java.nio.*;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.*;

public class Sound {

	int bufferId;
	int sourceId;
	
	protected Sound(String fileName) {
		ShortBuffer rawAudioBuffer;

		int channels;
		int sampleRate;
		
		try (MemoryStack stack = stackPush()) {
		    IntBuffer channelsBuffer   = stack.mallocInt(1);
		    IntBuffer sampleRateBuffer = stack.mallocInt(1);
		    rawAudioBuffer = stb_vorbis_decode_filename(Constants.OGG_LOCATION + fileName +".ogg", channelsBuffer, sampleRateBuffer);
		    channels = channelsBuffer.get(0);
		    sampleRate = sampleRateBuffer.get(0);
		}
		
		int format = -1;
		if (channels == 1) {
		    format = AL_FORMAT_MONO16;
		} else if (channels == 2) {
		    format = AL_FORMAT_STEREO16;
		}
		
		bufferId = alGenBuffers();
		alBufferData(bufferId, format, rawAudioBuffer, sampleRate);
		free(rawAudioBuffer);
		sourceId = alGenSources();
		alSourcei(sourceId, AL_BUFFER, bufferId);
	}
	
	public void play() {
		stop();
		alSourcePlay(sourceId);
	}
	
	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public void stop() {
		alSourceStop(sourceId);
	}
	
	public void pause() {
		alSourcePause(sourceId);
	}
	
	public void resume() {
		alSourcePlay(sourceId);
	}
	
	public void setPosition(int x,int y,int z) {
		AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
	}
	
	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? 1 : 0);
	}
	
	protected void cleanUp() {
		alDeleteSources(sourceId);
		alDeleteBuffers(bufferId);
	}
	
}
