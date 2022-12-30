package com.example.tutorialv3.blocks;

import com.example.tutorialv3.setup.Registration;
import com.example.tutorialv3.varia.CustomEnergyStorage;
import com.example.tutorialv3.varia.Tools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class GeneratorBE extends BlockEntity {

    public static final int INPUT_SLOTS = 5;
    public static final int OUTPUT_SLOTS = 1;

    // The properties that are used to communicate data to the baked model (GeneratorBakedModel)
    public static final ModelProperty<BlockState> GENERATING_BLOCK = new ModelProperty<>();
    public static final ModelProperty<Boolean> GENERATING = new ModelProperty<>();
    public static final ModelProperty<Boolean> COLLECTING = new ModelProperty<>();
    public static final ModelProperty<Boolean> ACTUALLY_GENERATING = new ModelProperty<>();

    // The actual values for these properties
    private boolean generating = false;
    private boolean collecting = false;
    private BlockState generatingBlock;
    private boolean actuallyGenerating = false;

    // For collecting
    private int collectingTicker = 0;
    private AABB collectingBox = null;

    // For generating our ores
    private int generatingCounter = 0;

    // A direct reference to our items and energy for easy access inside our block entity
    // LazyOptionals to return with getCapability()
    private final ItemStackHandler inputItems = createInputItemHandler();
    private final LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(() -> inputItems);
    private final ItemStackHandler outputItems = createOutputItemHandler();
    private final LazyOptional<IItemHandler> outputItemHandler = LazyOptional.of(() -> outputItems);
    private final LazyOptional<IItemHandler> combinedItemHandler = LazyOptional.of(this::createCombinedItemHandler);

    private final CustomEnergyStorage energy = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energy);

    public GeneratorBE(BlockPos pos, BlockState state) {
        super(Registration.GENERATOR_BE.get(), pos, state);
    }

    public boolean isGenerating() {
        return generating;
    }

    public void setGenerating(boolean generating) {
        this.generating = generating;
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public boolean isCollecting() {
        return collecting;
    }

    public void setCollecting(boolean collecting) {
        this.collecting = collecting;
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void setGeneratingBlock(BlockState generatingBlock) {
        // Only accept ores by checking the tag
        if (generatingBlock.is(Tags.Blocks.ORES)) {
            this.generatingBlock = generatingBlock;
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    // Called by the block ticker
    public void tickServer() {
        if (collecting) {
            collectingTicker--;
            if (collectingTicker <= 0) {
                collectingTicker = GeneratorConfig.COLLECTING_DELAY.get();
                collectItems();
            }
        }

        boolean areWeGenerating = false;
        if (generating) {
            areWeGenerating = generateOres();
        }
        if (areWeGenerating != actuallyGenerating) {
            actuallyGenerating = areWeGenerating;
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    private void collectItems() {
        // We calculate and cache a 3x3x3 box around our position
        if (collectingBox == null) {
            collectingBox = new AABB(getBlockPos()).inflate(1);
        }
        // Find all entities of type ItemEntity (representing items on the ground) and check if they are
        // ingots by testing with the INGOTS item tag
        List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, collectingBox,
                itemEntity -> {
                    ItemStack item = itemEntity.getItem();
                    return item.is(Tags.Items.INGOTS);
                });
        // For each of these items we try to insert it in the input buffer and kill or shrink the item on the ground
        for (ItemEntity itemEntity : entities) {
            ItemStack item = itemEntity.getItem();
            ItemStack remainder = ItemHandlerHelper.insertItem(inputItems, item, false);
            if (remainder.isEmpty()) {
                itemEntity.kill();
            } else {
                itemEntity.setItem(remainder);
            }
        }
    }

    private boolean generateOres() {
        // The player didn't select anything to generate
        if (generatingBlock == null) {
            return false;
        }
        // Not enough energy, don't even try
        if (energy.getEnergyStored() < GeneratorConfig.ENERGY_GENERATE.get()) {
            return false;
        }
        boolean areWeGenerating = false;
        for (int i = 0; i < inputItems.getSlots() ; i++) {
            ItemStack item = inputItems.getStackInSlot(i);
            if (!item.isEmpty()) {
                energy.consumeEnergy(GeneratorConfig.ENERGY_GENERATE.get());
                // The API documentation from getStackInSlot says you are not allowed to modify the itemstacks returned
                // by getStackInSlot. That's why we make a copy here
                item = item.copy();
                item.shrink(1);
                // Put back the item with one less (can be empty)
                inputItems.setStackInSlot(i, item);
                generatingCounter++;
                areWeGenerating = true;
                setChanged();
                if (generatingCounter >= GeneratorConfig.INGOTS_PER_ORE.get()) {
                    generatingCounter = 0;
                    // For each of these ores we try to insert it in the output buffer or else throw it on the ground
                    ItemStack remaining = ItemHandlerHelper.insertItem(outputItems, new ItemStack(generatingBlock.getBlock().asItem()), false);
                    Tools.spawnInWorld(level, worldPosition, remaining);
                }
            }
        }
        return areWeGenerating;
    }

    @Nonnull
    private ItemStackHandler createInputItemHandler() {
        return new ItemStackHandler(INPUT_SLOTS) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @NotNull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }
        };
    }

    @Nonnull
    private ItemStackHandler createOutputItemHandler() {
        return new ItemStackHandler(OUTPUT_SLOTS) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Nonnull
    private IItemHandler createCombinedItemHandler() {
        return new CombinedInvWrapper(inputItems, outputItems) {
            @NotNull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return stack;
            }
        };
    }

    private CustomEnergyStorage createEnergyStorage() {
        return new CustomEnergyStorage(GeneratorConfig.ENERGY_CAPACITY.get(), GeneratorConfig.ENERGY_RECEIVE.get()) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    // The getUpdateTag()/handleUpdateTag() pair is called whenever the client receives a new chunk
    // it hasn't seen before. i.e. the chunk is loaded

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveClientData(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            loadClientData(tag);
        }
    }

    // The getUpdatePacket()/onDataPacket() pair is used when a block update happens on the client
    // (a blockstate change or an explicit notificiation of a block update from the server). It's
    // easiest to implement them based on getUpdateTag()/handleUpdateTag()

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        // This is called client side: remember the current state of the values that we're interested in
        boolean oldGenerating = generating;
        boolean oldCollecting = collecting;
        boolean oldActuallyGenerating = actuallyGenerating;
        BlockState oldGeneratingBlock = generatingBlock;

        CompoundTag tag = pkt.getTag();
        // This will call loadClientData()
        handleUpdateTag(tag);

        // If any of the values was changed we request a refresh of our model data and send a block update (this will cause
        // the baked model to be recreated)
        if (oldGenerating != generating || oldCollecting != collecting ||
                oldActuallyGenerating != actuallyGenerating ||
                !Objects.equals(generatingBlock, oldGeneratingBlock)) {
            requestModelDataUpdate();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        inputItemHandler.invalidate();
        outputItemHandler.invalidate();
        combinedItemHandler.invalidate();
        energyHandler.invalidate();
    }

    @Nonnull
    @Override
    public ModelData getModelData() {
        return ModelData.builder()
                .with(GENERATING_BLOCK, generatingBlock)
                .with(GENERATING, generating)
                .with(ACTUALLY_GENERATING, actuallyGenerating)
                .with(COLLECTING, collecting)
                .build();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        saveClientData(tag);
        tag.put("Inventory", inputItems.serializeNBT());
        tag.put("Energy", energy.serializeNBT());
        CompoundTag infoTag = tag.getCompound("Info");
        infoTag.putInt("Generating", generatingCounter);
    }

    private void saveClientData(CompoundTag tag) {
        CompoundTag infoTag = new CompoundTag();
        tag.put("Info", infoTag);
        infoTag.putBoolean("generating", generating);
        infoTag.putBoolean("collecting", collecting);
        tag.putBoolean("actuallyGenerating", actuallyGenerating);
        if (generatingBlock != null) {
            infoTag.put("block", NbtUtils.writeBlockState(generatingBlock));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadClientData(tag);
        if (tag.contains("Inventory")) {
            inputItems.deserializeNBT(tag.getCompound("Inventory"));
        }
        if (tag.contains("Energy")) {
            energy.deserializeNBT(tag.get("Energy"));
        }
        if (tag.contains("Info")) {
            generatingCounter = tag.getCompound("Info").getInt("Generating");
        }
    }

    private void loadClientData(CompoundTag tag) {
        if (tag.contains("Info")) {
            CompoundTag infoTag = tag.getCompound("Info");
            generating = infoTag.getBoolean("generating");
            collecting = infoTag.getBoolean("collecting");
            if (infoTag.contains("block")) {
                generatingBlock = NbtUtils.readBlockState(infoTag.getCompound("block"));
            }
        }
        actuallyGenerating = tag.getBoolean("actuallyGenerating");
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return combinedItemHandler.cast();
            } else if (side == Direction.DOWN) {
                return outputItemHandler.cast();
            } else {
                return inputItemHandler.cast();
            }
        } else if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }
}
