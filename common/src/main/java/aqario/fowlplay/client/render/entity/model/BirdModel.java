package aqario.fowlplay.client.render.entity.model;

import aqario.fowlplay.client.render.entity.state.BirdRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/**
 * Abstract base model for all bird entities. Parameterized by BirdRenderState (not entity type) per
 * 1.21.11 rendering architecture. Subclasses implement setAnimations() using data from the render
 * state.
 */
public abstract class BirdModel<S extends BirdRenderState> extends EntityModel<S> {
  public final ModelPart root;
  public final ModelPart body;
  public final ModelPart neck;
  public final ModelPart head;
  public final ModelPart torso;
  public final ModelPart leftWing;
  public final ModelPart rightWing;
  public final ModelPart leftLeg;
  public final ModelPart rightLeg;
  public final ModelPart tail;

  public BirdModel(ModelPart root) {
    this.root = root.getChild("root");
    this.body = this.root.getChild("body");
    this.neck = this.body.getChild("neck");
    this.head = this.neck.getChild("head");
    this.torso = this.body.getChild("torso");
    this.leftWing = this.body.getChild("left_wing");
    this.rightWing = this.body.getChild("right_wing");
    this.leftLeg = this.root.getChild("left_leg");
    this.rightLeg = this.root.getChild("right_leg");
    this.tail = this.body.getChild("tail");
  }

  @Override
  public ModelPart root() {
    return this.root;
  }

  @Override
  public void setupAnim(S state) {
    // Reset all parts to default pose before applying animations
    this.root().getAllParts().forEach(ModelPart::resetPose);

    // Calculate interpolated rotation values from render state
    float relativeHeadYaw = Mth.wrapDegrees(state.headYaw - state.bodyYaw);
    float headPitch = state.headPitch;

    // Apply head/body rotation (always applied regardless of animation state)
    this.updateHeadRotation(relativeHeadYaw, headPitch);

    // Delegate to subclass for entity-specific animations
    this.setAnimations(state);
  }

  /**
   * Applies entity-specific animations based on the current render state. Subclasses should read
   * animation flags and states from the BirdRenderState and call animate()/animateWalk()
   * accordingly.
   *
   * @param state the current render state containing all animation data
   */
  protected abstract void setAnimations(S state);

  /**
   * Applies head and neck rotation based on interpolated yaw/pitch values. Can be overridden by
   * subclasses to customize rotation behavior (e.g., FlyingBirdModel clamps differently when
   * flying).
   */
  protected void updateHeadRotation(float headYaw, float headPitch) {
    headYaw = Mth.clamp(headYaw, -135.0F, 135.0F);
    headPitch = Mth.clamp(headPitch, -45.0F, 45.0F);
    this.neck.yRot = headYaw * (float) (Math.PI / 180.0);
    this.neck.xRot = headPitch * (float) (Math.PI / 180.0);
  }
}