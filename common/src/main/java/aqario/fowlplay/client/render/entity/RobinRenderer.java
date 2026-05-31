package aqario.fowlplay.client.render.entity;

import aqario.fowlplay.client.render.entity.layer.BirdHeldItemLayer;
import aqario.fowlplay.client.render.entity.model.RobinModel;
import aqario.fowlplay.common.entity.bird.passerine.RobinEntity;
import aqario.fowlplay.core.FowlPlay;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class RobinRenderer extends MobRenderer<RobinEntity, BirdRenderState, RobinModel> {
  private static final Identifier TEXTURE = FowlPlay.id("textures/entity/robin/american_robin.png");

  public RobinRenderer(EntityRendererProvider.Context context) {
    super(context, new RobinModel(context.bakeLayer(RobinModel.MODEL_LAYER)), 0.15f);
    this.addLayer(
        new BirdHeldItemLayer<>(
            this, net.minecraft.client.Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer(), new Vec3(0.0, -0.085, -0.1475)));
  }

  @Override
  public BirdRenderState createRenderState() {
    return new BirdRenderState();
  }

  @Override
  public void extractRenderState(RobinEntity entity, BirdRenderState state, float partialTick) {
    super.extractRenderState(entity, state, partialTick);
    state.ageInTicks = entity.tickCount + partialTick;
    state.limbSwing = entity.walkAnimation.position();
    state.limbSwingAmount = entity.walkAnimation.speed();
    state.bodyYaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
    state.headYaw = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
    state.headPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
    state.isFlying = entity.isFlying();
    state.isInWaterOrBubble = entity.isInWater();
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