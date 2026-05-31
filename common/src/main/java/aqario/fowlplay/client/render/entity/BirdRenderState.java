package aqario.fowlplay.client.render.entity;

import aqario.fowlplay.common.entity.variant.GooseVariant;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.item.ItemStack; // Added import

public class BirdRenderState extends LivingEntityRenderState {
  public float ageInTicks;
  public float limbSwing;
  public float limbSwingAmount;

  public float bodyYaw;
  public float headYaw;
  public float headPitch;

  public boolean isFlying;
  public boolean isInWaterOrBubble;
  public boolean onGround;
  public boolean isSleeping;
  public boolean isSitting;
  public boolean isDomestic;

  public float viewXRot;
  public float roll;

  public String customName;
  public Identifier variantTexture;
  public GooseVariant.ModelType gooseModelType;

  public final AnimationState standingState = new AnimationState();
  public final AnimationState swimmingState = new AnimationState();
  public final AnimationState sleepingState = new AnimationState();
  public final AnimationState glidingState = new AnimationState();
  public final AnimationState flappingState = new AnimationState();

  public final AnimationState sittingState = new AnimationState();
  public final AnimationState preeningState = new AnimationState();
  public final AnimationState scratchingState = new AnimationState();

  public final AnimationState chickenStandingState = new AnimationState();
  public final AnimationState chickenFlappingState = new AnimationState();
  public final AnimationState chickenFloatingState = new AnimationState();

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

  // Added for BirdHeldItemLayer
  public ItemStack heldItem = ItemStack.EMPTY;
}