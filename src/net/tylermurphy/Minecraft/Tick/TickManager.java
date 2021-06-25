package net.tylermurphy.Minecraft.Tick;

import java.util.List;

import net.tylermurphy.Minecraft.Tick.GameTicks.GrassTick;
import net.tylermurphy.Minecraft.Tick.GameTicks.WaterTick;


public class TickManager {

	public static void doTick() {
		List<BlockUpdate> block_updates = BlockUpdate.loadBirthedUpdates();
		for(BlockUpdate update : block_updates) {
			if(update.new_block_id == 17 || update.new_block_id == -1) {
				WaterTick.doWaterTick(update);
			}
			if(update.new_block_id == 0 || update.new_block_id == 1) {
				GrassTick.doGrassTick(update);
			}
		}
		BlockUpdate.purgeUpdates();
	}
	
}
