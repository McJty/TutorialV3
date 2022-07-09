package com.example.tutorialv3.varia;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;

public class ClientTools {

	// @todo change this. the new QuadBakingVertexConsumer doesn't care about ordering.
//    private static void putVertex(QuadBakingVertexConsumer builder, Vector3f normal, Vector4f vector,
//                                  float u, float v, TextureAtlasSprite sprite) {
//
//        var elements = builder.getVertexFormat().getElements();
//        for (int j = 0 ; j < elements.size() ; j++) {
//            var e = elements.get(j);
//            switch (e.getUsage()) {
//                case POSITION -> builder.(j, vector.x(), vector.y(), vector.z(), 1.0f);
//                case COLOR    -> builder.put(j, 1.0f, 1.0f, 1.0f, 1.0f);
//                case UV       -> putVertexUV(builder, u, v, sprite, j, e);
//                case NORMAL   -> builder.put(j, normal.x(), normal.y(), normal.z());
//                default       -> builder.put(j);
//            }
//        }
//    }

    private static void putVertexUV(QuadBakingVertexConsumer builder, float u, float v, TextureAtlasSprite sprite, int j, VertexFormatElement e) {
        switch (e.getIndex()) {
            case 0  -> builder.vertex(j, sprite.getU(u), sprite.getV(v));
            case 2  -> builder.vertex(j, (short) 0, (short) 0);
            //default -> builder.put(j);
        }
    }

//    public static BakedQuad createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Transformation rotation, TextureAtlasSprite sprite) {
//        Vector3f normal = v3.copy();
//        normal.sub(v2);
//        Vector3f temp = v1.copy();
//        temp.sub(v2);
//        normal.cross(temp);
//        normal.normalize();
//
//        int tw = sprite.getWidth();
//        int th = sprite.getHeight();
//
//        rotation = rotation.blockCenterToCorner();
//        rotation.transformNormal(normal);
//
//        Vector4f vv1 = new Vector4f(v1); rotation.transformPosition(vv1);
//        Vector4f vv2 = new Vector4f(v2); rotation.transformPosition(vv2);
//        Vector4f vv3 = new Vector4f(v3); rotation.transformPosition(vv3);
//        Vector4f vv4 = new Vector4f(v4); rotation.transformPosition(vv4);
//
//        //var builder = new QuadBakingVertexConsumer(sprite);
//        //builder.setDirection(Direction.getNearest(normal.x(), normal.y(), normal.z()));
//        //putVertex(builder, normal, vv1, 0, 0, sprite);
//        //putVertex(builder, normal, vv2, 0, th, sprite);
//        //putVertex(builder, normal, vv3, tw, th, sprite);
//        //putVertex(builder, normal, vv4, tw, 0, sprite);
//    }

    public static Vector3f v(float x, float y, float z) {
        return new Vector3f(x, y, z);
    }
}
