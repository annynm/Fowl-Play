package aqario.fowlplay.client.render.entity;

import net.minecraft.client.renderer.entity.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

/**
 * Base render state for all bird entities (including chicken overrides and scarecrow). Holds all
 * data that BirdModel, FlyingBirdModel, CustomChickenModel, and ScarecrowArmorModel need during
 * rendering. Populated by each renderer's extractRenderState() method every frame.
 */
public class BirdRenderState extends LivingEntityRenderState {
  // Animation timing (set by renderer from entity tick + partialTick)
  public float ageInTicks;
  public float limbSwing;
  public float limbSwingAmount;

  // Interpolated rotation values (pre-computed by renderer)
  public float bodyYaw;
  public float headYaw;
  public float headPitch;

  // Movement/environment flags
  public boolean isFlying;
  public boolean isInWaterOrBubble;
  public boolean onGround;
  public boolean isSleeping;

  // Pigeon-specific flags
  public boolean isSitting;

  // Flight-specific rotation (used by FlyingBirdModel)
  public float viewXRot;
  public float roll;

  // Common bird animation states — synced from entity each frame via extractRenderState()
  public final AnimationState standingState = new AnimationState();
  public final AnimationState swimmingState = new AnimationState();
  public final AnimationState sleepingState = new AnimationState();
  public final AnimationState glidingState = new AnimationState();
  public final AnimationState flappingState = new AnimationState();

  // Pigeon-specific animation states
  public final AnimationState sittingState = new AnimationState();

  // Sparrow-specific animation states
  public final AnimationState preeningState = new AnimationState();
  public final AnimationState scratchingState = new AnimationState();

  // Chicken-specific animation states (used by CustomChickenModel/BabyChickenModel)
  // These are only populated for chicken renderers; ignored by bird renderers.
  public final AnimationState chickenStandingState = new AnimationState();
  public final AnimationState chickenFlappingState = new AnimationState();
  public final AnimationState chickenFloatingState = new AnimationState();

  // Scarecrow-specific rotation data (used by ScarecrowArmorModel.setupAnim)
  // Only populated by ScarecrowRenderer.extractRenderState(); defaults to 0 for all other
  // renderers.
  public float headRotX;
  public float headRotY;
  public float headRotZ;
  public float bodyRotX;
  public float bodyRotY;
  public float bodyRotZ;
  public float leftArmRotX;
  public float leftArmRotY;
  public float leftArmRotZ;
  public float rightArmRotX;
  public float rightArmRotY;
  public float rightArmRotZ;
}