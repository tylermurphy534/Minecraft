package net.tylermurphy.Minecraft.Tick.GameTicks;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Tick.BlockUpdate;

public class WaterTick {
	
	public static void doWaterTick(BlockUpdate update) {
		if(update.new_block_id == 17) {
			if(Chunk.getBlock(update.x, update.y-1, update.z) == -1) {
				Chunk.setBlock(update.x, update.y-1, update.z, (byte)17);
			}
			if(Chunk.getBlock(update.x-1, update.y, update.z) == -1) {
				Chunk.setBlock(update.x-1, update.y, update.z, (byte)17);
			}
			if(Chunk.getBlock(update.x+1, update.y, update.z) == -1) {
				Chunk.setBlock(update.x+1, update.y, update.z, (byte)17);
			}
			if(Chunk.getBlock(update.x, update.y, update.z-1) == -1) {
				Chunk.setBlock(update.x, update.y, update.z-1, (byte)17);
			}
			if(Chunk.getBlock(update.x, update.y, update.z+1) == -1) {
				Chunk.setBlock(update.x, update.y, update.z+1, (byte)17);
			}
		}
		if(update.new_block_id == -1) {
			if(Chunk.getBlock(update.x, update.y+1, update.z) == 17) {
				Chunk.setBlock(update.x, update.y+1, update.z, (byte)17);
			}
			if(Chunk.getBlock(update.x-1, update.y, update.z) == 17) {
				Chunk.setBlock(update.x-1, update.y, update.z, (byte)17);
			}
			if(Chunk.getBlock(update.x+1, update.y, update.z) == 17) {
				Chunk.setBlock(update.x+1, update.y, update.z, (byte)17);
			}
			if(Chunk.getBlock(update.x, update.y, update.z-1) == 17) {
				Chunk.setBlock(update.x, update.y, update.z-1, (byte)17);
			}
			if(Chunk.getBlock(update.x, update.y, update.z+1) == 17) {
				Chunk.setBlock(update.x, update.y, update.z+1, (byte)17);
			}
		}
	}
	
}
