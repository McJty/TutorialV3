package com.example.tutorialv3.entities;

import com.example.tutorialv3.varia.Tools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ThiefFindChestGoal extends MoveToBlockGoal {

   private final ThiefEntity thief;
   private final Random random = new Random();

   private AtomicInteger stealingCounter = new AtomicInteger(20);

   public ThiefFindChestGoal(ThiefEntity mob, double pSpeedModifier) {
      super(mob, pSpeedModifier, 16);
      this.thief = mob;
   }

   /**
    * Reset the task's internal state. Called when this task is interrupted by another one
    */
   public void stop() {
      super.stop();
      thief.setStealing(false);
      BlockEntity be = mob.level.getBlockEntity(blockPos);
      if (be instanceof ChestBlockEntity) {
         mob.level.blockEvent(blockPos, be.getBlockState().getBlock(), 1, 0);
      }
   }

   public void tick() {
      super.tick();
      if (isReachedTarget()) {
         BlockEntity be = mob.level.getBlockEntity(blockPos);
         if (be instanceof ChestBlockEntity chest) {
            if (thief.isStealing()) {
               stealingCounter.getAndDecrement();
               if (stealingCounter.get() <= 0) {
                  stealingCounter.getAndSet(20);
                  ItemStack stack = extractRandomItem(chest);
                  if (!stack.isEmpty()) {
                     Tools.spawnInWorld(mob.level, blockPos.above(), stack);
                  }
               }
            } else {
               mob.level.blockEvent(blockPos, be.getBlockState().getBlock(), 1, 1);
               stealingCounter.getAndSet(20);
               thief.setStealing(true);
            }
         }
      }
   }

   private ItemStack extractRandomItem(BlockEntity e) {
      return e.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).map(handler -> {
         for (int i = 0 ; i < handler.getSlots() ; i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
               if (random.nextFloat() < .3f) {
                  return handler.extractItem(i, 1, false);
               }
            }
         }
         return ItemStack.EMPTY;
      }).orElse(ItemStack.EMPTY);
   }

   /**
    * Return true to set given position as destination
    */
   protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
      if (!pLevel.isEmptyBlock(pPos.above())) {
         return false;
      } else {
         BlockState blockstate = pLevel.getBlockState(pPos);
         return blockstate.is(Blocks.CHEST);
      }
   }
}