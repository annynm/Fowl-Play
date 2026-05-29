package aqario.fowlplay.client.render.entity;

import aqario.fowlplay.client.render.entity.BirdHeldItemLayer;
import aqario.fowlplay.client.render.entity.layer.PigeonBundleLayer;
import aqario.fowlplay.client.render.entity.model.PigeonModel;
import aqario.fowlplay.common.entity.bird.dove.PigeonEntity;
import aqario.fowlplay.core.FowlPlay;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class PigeonRenderer extends MobRenderer<PigeonEntity, BirdRenderState, PigeonModel> {
  private static final Identifier MARTHA_TEXTURE = FowlPlay.id("textures/entity/pigeon/martha.png");

  public PigeonRenderer(EntityRendererProvider.Context context) {
    super(context, new PigeonModel(context.bakeLayer(PigeonModel.MODEL_LAYER)), 0.2f);
    this.addLayer(
        new BirdHeldItemLayer<>(
            this, context.getItemInHandRenderer(), new Vec3(0.0, -0.0225, -0.1475)));
    this.addLayer(new PigeonBundleLayer(this, context.getItemInHandRenderer()));
  }

  @Override
  public BirdRenderState createRenderState() {
    return new BirdRenderState();
  }

  @Override
  protected void extractRenderState(PigeonEntity entity, BirdRenderState state, float partialTick) {
    super.extractRenderState(entity, state, partialTick);
    state.ageInTicks = entity.tickCount + partialTick;
    state.limbSwing = entity.walkAnimation.position();
    state.limbSwingAmount = entity.walkAnimation.speed();
    state.bodyYaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
    state.headYaw = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
    state.headPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
    state.isFlying = entity.isFlying();
    state.isInWaterOrBubble = entity.isInWaterOrBubble();
    state.onGround = entity.onGround();
    state.isSleeping = entity.isSleeping();
    state.isSitting = entity.isInSittingPose();
    state.viewXRot = entity.getViewXRot(partialTick);
    state.roll = entity.getRoll(partialTick);
    state.customName = ChatFormatting.stripFormatting(entity.getName().getString());
    state.variantTexture = entity.getVariant().value().texture();
    state.standingState.copyFrom(entity.standingState);
    state.swimmingState.copyFrom(entity.swimmingState);
    state.sleepingState.copyFrom(entity.sleepingState);
    state.glidingState.copyFrom(entity.glidingState);
    state.flappingState.copyFrom(entity.flappingState);
    state.sittingState.copyFrom(entity.sittingState);
  }

  @Override
  public Identifier getTextureLocation(BirdRenderState state) {
    if ("Martha".equals(state.customName)) {
      return MARTHA_TEXTURE;
    }
    return state.variantTexture;
  }
}