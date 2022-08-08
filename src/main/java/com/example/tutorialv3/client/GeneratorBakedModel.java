package com.example.tutorialv3.client;

import com.example.tutorialv3.TutorialV3;
import com.example.tutorialv3.blocks.GeneratorBE;
import com.example.tutorialv3.blocks.GeneratorBlock;
import com.example.tutorialv3.varia.ClientTools;
import com.mojang.math.Matrix4f;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

import static com.example.tutorialv3.varia.ClientTools.v;
import static java.lang.Boolean.TRUE;

public class GeneratorBakedModel implements IDynamicBakedModel {

    private final ModelState modelState;
    private final Function<Material, TextureAtlasSprite> spriteGetter;
    private final Map<ModelKey, List<BakedQuad>> quadCache = new HashMap<>();
    private final ItemOverrides overrides;
    private final ItemTransforms itemTransforms;

    /**
     * @param modelState represents the transformation (orientation) of our model. This is generated from the FACING property that our blockstate uses
     * @param spriteGetter gives a way to convert materials to actual sprites on the main atlas
     * @param overrides this is used for using this baked model when it is rendered in an inventory (as an item)
     * @param itemTransforms these represent the transforms to use for the item model
     */
    public GeneratorBakedModel(ModelState modelState, Function<Material, TextureAtlasSprite> spriteGetter,
                               ItemOverrides overrides, ItemTransforms itemTransforms) {
        this.modelState = modelState;
        this.spriteGetter = spriteGetter;
        this.overrides = overrides;
        this.itemTransforms = itemTransforms;
        generateQuadCache();
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    /**
     * Whenever a chunk where our block is in needs to be rerendered this method is called to return the quads (polygons)
     * for our model. Typically this will be called seven times: one time for every direction and one time in general.
     * If you have a block that is solid at one of the six sides it can be a good idea to render that face only for that
     * direction. That way Minecraft knows that it can get rid of that face when another solid block is adjacent to that.
     * All faces or quads that are generated for side == null are not going to be culled away like that
     * @param state the blockstate for our block
     * @param side the six directions or null for quads that are not at a specific direction
     * @param rand random generator that you can use to add variations to your model (usually for textures)
     * @param extraData this represents the data that is given to use from our block entity
     * @return a list of quads
     */
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, @Nullable RenderType layer) {

        // Are we on the solid render type and are we rendering for side == null
        if (side != null || (layer != null && !layer.equals(RenderType.solid()))) {
            return Collections.emptyList();
        }

        // Get the data from our block entity
        boolean generating = TRUE == extraData.get(GeneratorBE.GENERATING);
        boolean collecting = TRUE == extraData.get(GeneratorBE.COLLECTING);
        boolean actuallyGenerating = TRUE == extraData.get(GeneratorBE.ACTUALLY_GENERATING);

        // Generate the quads from the block (ore) that we are generating
        var quads = getQuadsForGeneratingBlock(state, rand, extraData, layer);

        // ModelKey represents a unique configuration. We can use this to get our cached quads
        ModelKey key = new ModelKey(generating, collecting, actuallyGenerating, modelState);
        quads.addAll(quadCache.get(key));

        return quads;
    }

    private void generateQuadCache() {
        quadCache.put(new ModelKey(true, false, false, modelState), generateQuads(true, false, false));
        quadCache.put(new ModelKey(true, true, false, modelState), generateQuads(true, true, false));
        quadCache.put(new ModelKey(true, false, true, modelState), generateQuads(true, false, true));
        quadCache.put(new ModelKey(true, true, true, modelState), generateQuads(true, true, true));
        quadCache.put(new ModelKey(false, false, false, modelState), generateQuads(false, false, false));
        quadCache.put(new ModelKey(false, true, false, modelState), generateQuads(false, true, false));
        quadCache.put(new ModelKey(false, false, true, modelState), generateQuads(false, false, true));
        quadCache.put(new ModelKey(false, true, true, modelState), generateQuads(false, true, true));
    }

    /**
     * Generate the quads for a given configuration. This is done in the constructor in order to populate
     * our quad cache.
     */
    @NotNull
    private List<BakedQuad> generateQuads(boolean generating, boolean collecting, boolean actuallyGenerating) {
        var quads = new ArrayList<BakedQuad>();
        float l = 0;
        float r = 1;
        float p = 13f / 16f; // Relative position of panel

        float bl = 1f/16f;   // Left side of button
        float br = 7f/16f;   // Right side of button

        float h = .5f;       // Half

        Transformation rotation = modelState.getRotation();

        TextureAtlasSprite textureSide = spriteGetter.apply(GeneratorModelLoader.MATERIAL_SIDE);
        TextureAtlasSprite textureFrontPowered = spriteGetter.apply(GeneratorModelLoader.MATERIAL_FRONT_POWERED);
        TextureAtlasSprite textureFront = spriteGetter.apply(GeneratorModelLoader.MATERIAL_FRONT);
        TextureAtlasSprite textureOn = spriteGetter.apply(GeneratorModelLoader.MATERIAL_ON);
        TextureAtlasSprite textureOff = spriteGetter.apply(GeneratorModelLoader.MATERIAL_OFF);

        // The base
        quads.add(ClientTools.createQuad(v(r, p, r), v(r, p, l), v(l, p, l), v(l, p, r), rotation, actuallyGenerating ? textureFrontPowered : textureFront));      // Top side
        quads.add(ClientTools.createQuad(v(l, l, l), v(r, l, l), v(r, l, r), v(l, l, r), rotation, textureSide));
        quads.add(ClientTools.createQuad(v(r, p, r), v(r, l, r), v(r, l, l), v(r, p, l), rotation, textureSide));
        quads.add(ClientTools.createQuad(v(l, p, l), v(l, l, l), v(l, l, r), v(l, p, r), rotation, textureSide));
        quads.add(ClientTools.createQuad(v(r, p, l), v(r, l, l), v(l, l, l), v(l, p, l), rotation, textureSide));
        quads.add(ClientTools.createQuad(v(l, p, r), v(l, l, r), v(r, l, r), v(r, p, r), rotation, textureSide));

        // The collecting button
        float s = collecting ? 14f/16f : r;
        float offset = 0;
        quads.add(ClientTools.createQuad(v(br, s, br+offset), v(br, s, bl+offset), v(bl, s, bl+offset), v(bl, s, br+offset), rotation, collecting ? textureOn : textureOff));
        quads.add(ClientTools.createQuad(v(br, s, br+offset), v(br, p, br+offset), v(br, p, bl+offset), v(br, s, bl+offset), rotation, textureSide));
        quads.add(ClientTools.createQuad(v(bl, s, bl+offset), v(bl, p, bl+offset), v(bl, p, br+offset), v(bl, s, br+offset), rotation, textureSide));
        quads.add(ClientTools.createQuad(v(br, s, bl+offset), v(br, p, bl+offset), v(bl, p, bl+offset), v(bl, s, bl+offset), rotation, textureSide));
        quads.add(ClientTools.createQuad(v(bl, s, br+offset), v(bl, p, br+offset), v(br, p, br+offset), v(br, s, br+offset), rotation, textureSide));

        // The generating button
        s = generating ? 14f/16f : r;
        offset = h;
        quads.add(ClientTools.createQuad(v(br, s, br+offset), v(br, s, bl+offset), v(bl, s, bl+offset), v(bl, s, br+offset), rotation, generating ? textureOn : textureOff));
        quads.add(ClientTools.createQuad(v(br, s, br+offset), v(br, p, br+offset), v(br, p, bl+offset), v(br, s, bl+offset), rotation, textureSide));
        quads.add(ClientTools.createQuad(v(bl, s, bl+offset), v(bl, p, bl+offset), v(bl, p, br+offset), v(bl, s, br+offset), rotation, textureSide));
        quads.add(ClientTools.createQuad(v(br, s, bl+offset), v(br, p, bl+offset), v(bl, p, bl+offset), v(bl, s, bl+offset), rotation, textureSide));
        quads.add(ClientTools.createQuad(v(bl, s, br+offset), v(bl, p, br+offset), v(br, p, br+offset), v(br, s, br+offset), rotation, textureSide));
        return quads;
    }

    /**
     * Get the quads from the block we are generating.
     */
    private List<BakedQuad> getQuadsForGeneratingBlock(@Nullable BlockState state, @NotNull RandomSource rand, @NotNull ModelData extraData, RenderType layer) {
        var quads = new ArrayList<BakedQuad>();
        BlockState generatingBlock = extraData.get(GeneratorBE.GENERATING_BLOCK);
        if (generatingBlock != null && !(generatingBlock.getBlock() instanceof GeneratorBlock)) {
            if (layer == null || getRenderTypes(generatingBlock, rand, extraData).contains(layer)) {
                BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(generatingBlock);
                try {
                    Direction facing = state == null ? Direction.SOUTH : state.getValue(BlockStateProperties.FACING);
                    Transformation rotation = modelState.getRotation();
                    Transformation translate = transformGeneratingBlock(facing, rotation);
                    IQuadTransformer transformer = QuadTransformers.applying(translate);

                    // Get the quads for every side, transform it and add it to the list of quads
                    for (Direction s : Direction.values()) {
                        List<BakedQuad> modelQuads = model.getQuads(generatingBlock, s, rand, ModelData.EMPTY, layer);
                        for (BakedQuad quad : modelQuads) {
                            quads.add(transformer.process(quad));
                        }
                    }
                } catch (Exception e) {
                    // In case a certain mod has a bug we don't want to cause everything to crash. Instead we log the problem
                    ResourceLocation key = ForgeRegistries.BLOCKS.getKey(generatingBlock.getBlock());
                    TutorialV3.LOGGER.log(Level.ERROR, "A block '" + key.toString() + "' caused a crash!");
                }
            }
        }
        return quads;
    }

    /**
     * Generate a transform that will transform the ore block that we're generating to a smaller version
     * that fits nicely into our front panel.
     */
    @NotNull
    private Transformation transformGeneratingBlock(Direction facing, Transformation rotation) {
        // Note: when composing a transformation like this you have to imagine these transformations in reverse order.
        // So this routine makes most sense if you read it from end to beginning

        // Facing refers to the front face of our block. So dX, dY, dZ are the offsets pointing
        // in that direction. We want to move our model slightly to the front and top-left corner
        float dX = facing.getStepX();
        float dY = facing.getStepY();
        float dZ = facing.getStepZ();
        // Correct depending on face. After this dX, dY, dZ will be the offset perpendicular to the direction of our face
        switch (facing) {
            case DOWN ->  { dX = 1; dY = 0; dZ = -1; }
            case UP ->    { dX = 1; dY = 0; dZ = 1; }
            case NORTH -> { dX = 1; dY = 1; dZ = 0; }
            case SOUTH -> { dX = -1; dY = 1; dZ = 0; }
            case WEST ->  { dX = 0; dY = 1; dZ = -1; }
            case EAST ->  { dX = 0; dY = 1; dZ = 1; }
        }
        // Calculate the first translation (before scaling/rotating). Basically we move in three steps:
        //   - Move in the direction that our front face is facing (divided by 4)
        //   - Move perpendicular to that direction (also divided by 4)
        //   - Move half a block so that rotation and scaling will happen relative to the center
        float stepX = facing.getStepX() / 4f + dX / 4f + .5f;
        float stepY = facing.getStepY() / 4f + dY / 4f + .5f;
        float stepZ = facing.getStepZ() / 4f + dZ / 4f + .5f;

        // As the final step (remember, read from end to start) we position our correctly rotated and scaled
        // block to the top-left corner of our main block
        Transformation translate = new Transformation(Matrix4f.createTranslateMatrix(stepX, stepY, stepZ));

        // Now our block is correctly positioned where we want to rotate and scale it
        translate = translate.compose(new Transformation(Matrix4f.createScaleMatrix(.2f, .2f, .2f)));
        translate = translate.compose(rotation);    // The rotation from our main model

        // This will happen first: translate our subblock so it's center is at 0,0,0. That way scaling and rotating will be correct and
        // not change the position
        translate = translate.compose(new Transformation(Matrix4f.createTranslateMatrix(-.5f, -.5f, -.5f)));
        return translate;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return spriteGetter.apply(GeneratorModelLoader.MATERIAL_SIDE);
    }

    @Override
    public ItemOverrides getOverrides() {
        return overrides;
    }

    @Override
    public ItemTransforms getTransforms() {
        return itemTransforms;
    }
}
