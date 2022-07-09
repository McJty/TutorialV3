package com.example.tutorialv3.client;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.example.tutorialv3.TutorialV3;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

public class GeneratorModelLoader implements IGeometryLoader<GeneratorModelLoader.GeneratorModelGeometry> {

    public static final ResourceLocation GENERATOR_LOADER = new ResourceLocation(TutorialV3.MODID, "generatorloader");

    public static final ResourceLocation GENERATOR_FRONT_POWERED = new ResourceLocation(TutorialV3.MODID, "block/generator_front_powered");
    public static final ResourceLocation GENERATOR_FRONT = new ResourceLocation(TutorialV3.MODID, "block/generator_front");
    public static final ResourceLocation GENERATOR_SIDE = new ResourceLocation(TutorialV3.MODID, "block/generator_side");
    public static final ResourceLocation GENERATOR_ON = new ResourceLocation(TutorialV3.MODID, "block/generator_on");
    public static final ResourceLocation GENERATOR_OFF = new ResourceLocation(TutorialV3.MODID, "block/generator_off");

    public static final Material MATERIAL_FRONT_POWERED = ForgeHooksClient.getBlockMaterial(GENERATOR_FRONT_POWERED);
    public static final Material MATERIAL_FRONT = ForgeHooksClient.getBlockMaterial(GENERATOR_FRONT);
    public static final Material MATERIAL_SIDE = ForgeHooksClient.getBlockMaterial(GENERATOR_SIDE);
    public static final Material MATERIAL_ON = ForgeHooksClient.getBlockMaterial(GENERATOR_ON);
    public static final Material MATERIAL_OFF = ForgeHooksClient.getBlockMaterial(GENERATOR_OFF);

    @Override
    public GeneratorModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        return new GeneratorModelGeometry();
    }

    
    public static class GeneratorModelGeometry implements IUnbakedGeometry<GeneratorModelGeometry> {
    	
    	@Override
    	public BakedModel bake(IGeometryBakingContext context, ModelBakery bakery,
    			Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides,
    			ResourceLocation modelLocation) {
            return new GeneratorBakedModel(modelState, spriteGetter, overrides, context.getTransforms());
        }

    	@Override
    	public Collection<Material> getMaterials(IGeometryBakingContext context,
    			Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
            return List.of(MATERIAL_FRONT, MATERIAL_FRONT_POWERED, MATERIAL_SIDE, MATERIAL_ON, MATERIAL_OFF);
        }
    }
}
