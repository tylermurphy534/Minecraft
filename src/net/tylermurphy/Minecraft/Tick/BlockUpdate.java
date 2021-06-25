package net.tylermurphy.Minecraft.Tick;

import java.util.ArrayList;
import java.util.List;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Chunk.Cube;

public class BlockUpdate {

	public int x,y,z;
	public byte old_block_id;
	public byte new_block_id;
	private long delay_of_update, creation_of_update;
	
	private static List<BlockUpdate> current_block_updates = new ArrayList<BlockUpdate>();
	private static List<BlockUpdate> waiting_block_updates = new ArrayList<BlockUpdate>();
	
	public static void createBlockUpdate(int x, int y, int z, byte old_block_id, byte new_block_id) {
		BlockUpdate update = new BlockUpdate();
		update.x = x;
		update.y = y;
		update.z = z;
		update.old_block_id = old_block_id;
		update.new_block_id = new_block_id;
		if(new_block_id != -1)
			update.delay_of_update = Cube.cubes[new_block_id].tick_update_delay*1000000;
		else
			update.delay_of_update = Cube.cubes[17].tick_update_delay*1000000;
		update.creation_of_update = System.nanoTime();
		waiting_block_updates.add(update);
	}
	
	public static List<BlockUpdate> loadBirthedUpdates() {
		int j = waiting_block_updates.size();
		for(int i = 0; i < j; i++) {
			BlockUpdate update = waiting_block_updates.get(i);
			if(Chunk.getBlock(update.x, update.y, update.z) != update.new_block_id) continue;
			if(System.nanoTime() - update.creation_of_update >= update.delay_of_update) {
				waiting_block_updates.remove(i);
				j--;
				i--;
				current_block_updates.add(update);
			}
		}
		return current_block_updates;
	}
	
	public static void purgeUpdates() {
		current_block_updates.clear();
	}
	
}
