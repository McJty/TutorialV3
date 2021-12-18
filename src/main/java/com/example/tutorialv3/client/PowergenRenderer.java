package com.example.tutorialv3.client;

import com.example.tutorialv3.TutorialV3;
import com.example.tutorialv3.blocks.PowergenBE;
import com.example.tutorialv3.setup.Registration;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static java.lang.Boolean.TRUE;

public class PowergenRenderer implements BlockEntityRenderer<PowergenBE> {

    public static final ResourceLocation HALO = new ResourceLocation(TutorialV3.MODID, "effect/halo");

    public PowergenRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PowergenBE powergen, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

        Boolean powered = powergen.getBlockState().getValue(BlockStateProperties.POWERED);
        if (TRUE != powered) {
            return;
        }

        int brightness = LightTexture.FULL_BRIGHT;
        float s = (System.currentTimeMillis() % 1000) / 1000.0f;
        if (s > 0.5f) {
            s = 1.0f - s;
        }
        float scale = 0.1f + s * .3f;

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(HALO);
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        Quaternion rotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
        poseStack.mulPose(rotation);
        VertexConsumer buffer = bufferSource.getBuffer(CustomRenderType.ADD);
        Matrix4f matrix = poseStack.last().pose();
        buffer.vertex(matrix, -scale, -scale, 0.0f).color(1.0f, 1.0f, 1.0f, 0.3f).uv(sprite.getU0(), sprite.getV0()).uv2(brightness).normal(1,0,0).endVertex();
        buffer.vertex(matrix, -scale, scale, 0.0f).color(1.0f, 1.0f, 1.0f, 0.3f).uv(sprite.getU0(), sprite.getV1()).uv2(brightness).normal(1,0,0).endVertex();
        buffer.vertex(matrix, scale, scale, 0.0f).color(1.0f, 1.0f, 1.0f, 0.3f).uv(sprite.getU1(), sprite.getV1()).uv2(brightness).normal(1,0,0).endVertex();
        buffer.vertex(matrix, scale, -scale, 0.0f).color(1.0f, 1.0f, 1.0f, 0.3f).uv(sprite.getU1(), sprite.getV0()).uv2(brightness).normal(1,0,0).endVertex();
        poseStack.popPose();
    }

    public static void register() {
        BlockEntityRenderers.register(Registration.POWERGEN_BE.get(), PowergenRenderer::new);
    }

}
