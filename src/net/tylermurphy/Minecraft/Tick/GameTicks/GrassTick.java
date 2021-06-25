package net.tylermurphy.Minecraft.Tick.GameTicks;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Tick.BlockUpdate;

public class GrassTick {
	
	private static boolean foundGrass(int x, int y, int z)
	{
		for(int a = x-1; a<x+2; a++) {
			for(int b = y-1; b<y+2; b++) {
				for(int c = z-1; c<z+2; c++) {
					if(x == a && c == z) continue;
					if(Chunk.getBlock(a, b, c) == 0) return true;
				}
			}
		}
		return false;
	}
	
	private static void searchForDirt(int x, int y, int z)
	{
		for(int a = x-1; a<x+2; a++)
			for(int b = y-1; b<y+2; b++)
				for(int c = z-1; c<z+2; c++) {
					if(x == a && c == z)
						continue;
					if(Chunk.getBlock(a, b, c) == 1 && Chunk.getBlock(a, b+1, c) == -1) {
						BlockUpdate.createBlockUpdate(a, b, c, (byte)1, (byte)1);
					}
				}
	}
	
	public static void doGrassTick(BlockUpdate update) {
		if(update.new_block_id == 1) {
			int rand = (int)(Math.random()*100);
			if(rand>95) {
				if(Chunk.getBlock(update.x,update.y+1,update.z) == -1 && foundGrass(update.x,update.y,update.z)) {
					Chunk.setBlock(update.x,update.y,update.z, (byte)0);
				}
			} else {
				BlockUpdate.createBlockUpdate(update.x,update.y,update.z, (byte)1, (byte)1);
			}
		}
		if(update.new_block_id == 0) {
			searchForDirt(update.x,update.y,update.z);
		}
	}
	
}
