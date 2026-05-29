package aqario.fowlplay.client.render.entity;

import aqario.fowlplay.client.render.entity.BirdHeldItemLayer;
import aqario.fowlplay.client.render.entity.model.RavenModel;
import aqario.fowlplay.common.entity.bird.passerine.RavenEntity;
import aqario.fowlplay.core.FowlPlay;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class RavenRenderer extends MobRenderer<RavenEntity, BirdRenderState, RavenModel> {
  private static final Identifier TEXTURE = FowlPlay.id("textures/entity/raven/raven.png");

  public RavenRenderer(EntityRendererProvider.Context context) {
    super(context, new RavenModel(context.bakeLayer(RavenModel.MODEL_LAYER)), 0.3f);
    this.addLayer(
        new BirdHeldItemLayer<>(
            this, context.getItemInHandRenderer(), new Vec3(0.0, -0.05375, -0.1475)));
  }

  @Override
  public BirdRenderState createRenderState() {
    return new BirdRenderState();
  }

  @Override
  protected void extractRenderState(RavenEntity entity, BirdRenderState state, float partialTick) {
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
    state.standingState.copyFrom(entity.standingState);
    state.swimmingState.copyFrom(entity.swimmingState);
    state.sleepingState.copyFrom(entity.sleepingState);
    state.glidingState.copyFrom(entity.glidingState);
    state.flappingState.copyFrom(entity.flappingState);
  }

  @Override
  public Identifier getTextureLocation(BirdRenderState state) {
    return TEXTURE;
  }
}