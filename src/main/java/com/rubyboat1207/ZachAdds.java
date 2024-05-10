package com.rubyboat1207;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ZachAdds implements ModInitializer {
	public static final String MOD_ID = "zach_adds";
    public static final Logger LOGGER = LoggerFactory.getLogger("zach_adds");
	public static final WearableTreeItem TREE_ITEM = new WearableTreeItem(new Item.Settings());
	public static final Item SHOVEL_ITEM = new Item(new Item.Settings());


	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "tree"), TREE_ITEM);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "shovel"), SHOVEL_ITEM);

		AttackEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {
			if(player.getStackInHand(hand).isOf(TREE_ITEM)) {
				TREE_ITEM.onAttack(player.getStackInHand(hand), player, entity);
			}

			return ActionResult.PASS;
		}));

		ServerTickEvents.END_SERVER_TICK.register();

		PlayerBlockBreakEvents.AFTER.register(((world, player, pos, state, blockEntity) -> {
			if(player.getStackInHand(Hand.MAIN_HAND).isOf(SHOVEL_ITEM)) {
				BlockPos start = pos.up();

				if(world.getBlockState(start).isIn(BlockTags.LOGS)) {
					// oh my fucking god, its a TREE. GET HIM
					world.setBlockState(start, Blocks.AIR.getDefaultState());
					BlockPos lastLogBlock = start.up();

					while(world.getBlockState(lastLogBlock).isIn(BlockTags.LOGS)) {
						world.setBlockState(lastLogBlock, Blocks.AIR.getDefaultState());
						lastLogBlock = lastLogBlock.up();
					}

					clearLeavesBFS(world, lastLogBlock.down(), 5);

					world.spawnEntity(new ItemEntity(world, start.getX(), start.getY(), start.getZ(), new ItemStack(TREE_ITEM)));
				}
			}
		}));
	}

	private void clearLeavesBFS(World world, BlockPos start, int maxDepth) {
		Queue<BlockPos> queue = new LinkedList<>();
		Set<BlockPos> visited = new HashSet<>();
		queue.add(start);
		visited.add(start);

		int depth = 0;

		while (!queue.isEmpty() && depth <= maxDepth) {
			int levelSize = queue.size();
			for (int i = 0; i < levelSize; i++) {
				BlockPos current = queue.poll();

				// Check and replace the current block
				if (world.getBlockState(current).isIn(BlockTags.LEAVES)) {
					world.setBlockState(current, Blocks.AIR.getDefaultState());
				}

				// Enqueue neighboring blocks if they haven't been visited
				for (BlockPos neighbor : Arrays.asList(current.up(), current.down(), current.north(), current.south(), current.east(), current.west())) {
					if (!visited.contains(neighbor) && world.getBlockState(neighbor).isIn(BlockTags.LEAVES)) {
						queue.add(neighbor);
						visited.add(neighbor);
					}
				}
			}
			depth++;
		}
	}
}