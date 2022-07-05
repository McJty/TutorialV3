package com.example.tutorialv3.client;

import javax.annotation.Nullable;

import net.minecraft.client.resources.model.ModelState;

/**
 * A key used to identify a set of baked quads for the baked model
 */
public record ModelKey(boolean generating, boolean collecting, boolean actuallyGenerating, @Nullable ModelState modelState) { }
