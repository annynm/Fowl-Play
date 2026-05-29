package aqario.fowlplay.client.render.entity;

import aqario.fowlplay.client.render.entity.BirdHeldItemLayer;
import aqario.fowlplay.client.render.entity.model.DuckModel;
import aqario.fowlplay.common.entity.bird.waterfowl.DuckEntity;
import aqario.fowlplay.core.FowlPlay;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class DuckRenderer extends MobRenderer<DuckEntity, BirdRenderState, DuckModel> {
  private static final Identifier QUACKERS_TEXTURE =
      FowlPlay.id("textures/entity/duck/quackers.png");

  public DuckRenderer(EntityRendererProvider.Context context) {
    super(context, new DuckModel(context.bakeLayer(DuckModel.MODEL_LAYER)), 0.3f);
    this.addLayer(
        new BirdHeldItemLayer<>(
            this, context.getItemInHandRenderer(), new Vec3(0.0, -0.05375, -0.1475)));
  }

  @Override
  public BirdRenderState createRenderState() {
    return new BirdRenderState();
  }

  @Override
  protected void extractRenderState(DuckEntity entity, BirdRenderState state, float partialTick) {
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
    state.viewXRot = entity.getViewXRot(partialTick);
    state.roll = entity.getRoll(partialTick);
    state.customName = ChatFormatting.stripFormatting(entity.getName().getString());
    state.isDomestic = entity.isDomestic();
    state.variantTexture = entity.getVariant().value().texture(false, entity.isDomestic());
    state.standingState.copyFrom(entity.standingState);
    state.swimmingState.copyFrom(entity.swimmingState);
    state.sleepingState.copyFrom(entity.sleepingState);
    state.glidingState.copyFrom(entity.glidingState);
    state.flappingState.copyFrom(entity.flappingState);
  }

  @Override
  public Identifier getTextureLocation(BirdRenderState state) {
    if ("Quackers".equals(state.customName)) {
      return QUACKERS_TEXTURE;
    }
    return state.variantTexture;
  }
}