package aqario.fowlplay.client.render.entity.model;

import aqario.fowlplay.client.render.entity.animation.PenguinAnimations;
import aqario.fowlplay.common.entity.bird.penguin.PenguinEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/**
 * Runtime animation and posing for adult penguins. Static mesh data is defined in {@link
 * aqario.fowlplay.client.render.entity.layer.PenguinLayer}.
 */
public class PenguinModel extends BirdModel<PenguinEntity> {

  public PenguinModel(ModelPart root) {
    super(root);
  }

  @Override
  protected void setAnimations(
      PenguinEntity entity,
      float limbSwing,
      float limbSwingAmount,
      float ageInTicks,
      float netHeadYaw,
      float headPitch,
      float partialTick) {
    if (entity.isSwimming()) {
      this.root.yRot = netHeadYaw * (float) (Math.PI / 180.0);
      this.root.xRot = headPitch * (float) (Math.PI / 180.0);
    }
    if (!entity.isSwimming() && !entity.isSliding()) {
      this.updateHeadRotation(netHeadYaw, headPitch);
      this.animateWalk(PenguinAnimations.WALKING, limbSwing, limbSwingAmount, 7F, 7F);
    }
    this.animate(entity.standingState, PenguinAnimations.STANDING, ageInTicks);
    this.animate(entity.slidingState, PenguinAnimations.SLIDING, ageInTicks);
    this.animate(
        entity.slidingTransitionState, PenguinAnimations.SLIDING_TRANSITION, ageInTicks, 1.0F);
    this.animate(
        entity.standingTransitionState, PenguinAnimations.STANDING_TRANSITION, ageInTicks, 1.0F);
    this.animate(entity.swimmingState, PenguinAnimations.SWIMMING, ageInTicks);
    this.animate(entity.dancingState, PenguinAnimations.DANCING, ageInTicks);
  }

  @Override
  protected void updateHeadRotation(float headYaw, float headPitch) {
    headYaw = Mth.clamp(headYaw, -75.0F, 75.0F);
    headPitch = Mth.clamp(headPitch, -45.0F, 45.0F);
    this.neck.yRot = headYaw * (float) (Math.PI / 180.0);
    this.neck.xRot = headPitch * (float) (Math.PI / 180.0);
  }
}