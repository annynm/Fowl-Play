package aqario.fowlplay.client.render.entity.layer;

import net.minecraft.client.model.geom.builders.LayerDefinition;

/**
 * Abstract base for bird entity layer definitions. In 1.21.11, LayerDefinitions are registered
 * separately from EntityModels and baked once at startup into the EntityModelSet.
 *
 * <p>Subclasses provide the static mesh data; the corresponding EntityModel subclass handles
 * runtime animation and posing using the baked ModelPart.
 */
public abstract class BirdLayer {

  /**
   * Creates the LayerDefinition containing the mesh data for this bird entity. Called once during
   * model layer registration, not during rendering.
   */
  public abstract LayerDefinition createLayerDefinition();
}