package aqario.fowlplay.client.render.entity.model;

import aqario.fowlplay.client.render.entity.BirdRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/**
 * Abstract base model for flying birds. Extends BirdModel with open-wing rendering and
 * flight-specific rotation logic. Wing visibility toggling and flight rotations are driven entirely
 * by BirdRenderState.
 */
public abstract class FlyingBirdModel<S extends BirdRenderState> extends BirdModel<S> {
  public final ModelPart leftWingOpen;
  public final ModelPart rightWingOpen;

  public FlyingBirdModel(ModelPart root) {
    super(root);
    this.leftWingOpen = this.body.getChild("left_wing_open");
    this.rightWingOpen = this.body.getChild("right_wing_open");
  }

  @Override
  public void setupAnim(S state) {
    // Reset all parts to default pose
    this.root().getAllParts().forEach(ModelPart::resetPose);

    float relativeHeadYaw = Mth.wrapDegrees(state.headYaw - state.bodyYaw);
    float headPitch = state.headPitch;

    // Flight-specific root rotation overrides normal head/body rotation
    if (state.isFlying) {
      this.root.xRot = state.viewXRot * (float) (Math.PI / 180.0);
      this.root.zRot = state.roll * (float) (Math.PI / 180.0);
    } else {
      // Only apply head rotation when not flying
      this.updateHeadRotation(relativeHeadYaw, headPitch);
    }

    // Toggle wing visibility based on flight state
    if (this.shouldRenderWings(state)) {
      this.leftWingOpen.visible = true;
      this.rightWingOpen.visible = true;
      this.leftWing.visible = false;
      this.rightWing.visible = false;
    } else {
      this.leftWingOpen.visible = false;
      this.rightWingOpen.visible = false;
      this.leftWing.visible = true;
      this.rightWing.visible = true;
    }

    // Delegate to subclass for entity-specific animations
    this.setAnimations(state);
  }

  /**
   * Determines whether open wings should be rendered instead of folded wings. Default
   * implementation shows open wings during flight. Override for custom wing visibility logic (e.g.,
   * gliding vs flapping).
   */
  protected boolean shouldRenderWings(S state) {
    return state.isFlying;
  }
}