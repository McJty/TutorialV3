package com.example.tutorialv3.client;

import net.minecraft.client.resources.model.ModelState;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A key used to identify a set of baked quads for the baked model
 */
public class ModelKey {

    private final boolean generating;
    private final boolean collecting;
    private final boolean actuallyGenerating;
    @Nullable
    private final ModelState modelState;

    public ModelKey(boolean generating, boolean collecting, boolean actuallyGenerating, @Nullable ModelState modelState) {
        this.generating = generating;
        this.collecting = collecting;
        this.actuallyGenerating = actuallyGenerating;
        this.modelState = modelState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelKey that = (ModelKey) o;
        return generating == that.generating && collecting == that.collecting && actuallyGenerating == that.actuallyGenerating && Objects.equals(modelState, that.modelState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(generating, collecting, actuallyGenerating, modelState);
    }
}
