package aqario.fowlplay.client.render.entity.model;

import net.minecraft.client.model.geom.ModelPart;

/**
 * Runtime animation and posing for baby penguins. Static mesh data is defined in {@link
 * aqario.fowlplay.client.render.entity.layer.BabyPenguinLayer}.
 *
 * <p>Baby penguins share the same animation logic as adults but use a different mesh. Override
 * setAnimations if baby-specific animations are needed.
 */
public class BabyPenguinModel extends PenguinModel {

  public BabyPenguinModel(ModelPart root) {
    super(root);
  }

  // Baby penguins inherit adult animations by default.
  // Override setAnimations() here if baby-specific animations diverge.
}