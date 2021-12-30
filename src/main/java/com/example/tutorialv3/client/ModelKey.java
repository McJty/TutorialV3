package com.example.tutorialv3.client;

import net.minecraft.client.resources.model.ModelState;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A key used to identify a set of baked quads for the baked model
 */
public record ModelKey(boolean generating, boolean collecting, boolean actuallyGenerating, @Nullable ModelState modelState) { }
