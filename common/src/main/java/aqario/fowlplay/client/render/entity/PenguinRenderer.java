package aqario.fowlplay.client.render.entity;

import aqario.fowlplay.client.render.entity.layer.BirdHeldItemLayer;
import aqario.fowlplay.client.render.entity.model.AdultBabyModelPair;
import aqario.fowlplay.client.render.entity.model.BabyPenguinModel;
import aqario.fowlplay.client.render.entity.model.PenguinModel;
import aqario.fowlplay.common.entity.bird.penguin.PenguinEntity;
import aqario.fowlplay.core.FowlPlay;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class PenguinRenderer extends MobRenderer<PenguinEntity, BirdRenderState, PenguinModel> {
  private static final Identifier TEXTURE = FowlPlay.id("textures/entity/penguin/penguin.png");
  private static final Identifier BABY_TEXTURE =
      FowlPlay.id("textures/entity/penguin/penguin_baby.png");
  private final AdultBabyModelPair<BirdRenderState, PenguinModel> modelPair;

  public PenguinRenderer(EntityRendererProvider.Context context) {
    super(context, new PenguinModel(context.bakeLayer(PenguinModel.MODEL_LAYER)), 0.3f);
    this.addLayer(
        new BirdHeldItemLayer<>(
            this,
            Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer(),
            new Vec3(0.0, -0.145, -0.1475)));
    this.modelPair =
        new AdultBabyModelPair<>(
            new PenguinModel(context.bakeLayer(PenguinModel.MODEL_LAYER)),
            new BabyPenguinModel(context.bakeLayer(BabyPenguinModel.MODEL_LAYER)));
  }

  @Override
  public BirdRenderState createRenderState() {
    return new BirdRenderState();
  }

  @Override
  public void extractRenderState(PenguinEntity entity, BirdRenderState state, float partialTick) {
    super.extractRenderState(entity, state, partialTick);
    state.ageInTicks = entity.tickCount + partialTick;
    state.limbSwing = entity.walkAnimation.position();
    state.limbSwingAmount = entity.walkAnimation.speed();
    state.bodyYaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
    state.headYaw = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
    state.headPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
    state.isFlying = false;
    state.isInWaterOrBubble = entity.isInWater();
    state.onGround = entity.onGround();
    state.isSleeping = entity.isSleeping();
    state.customName = ChatFormatting.stripFormatting(entity.getName().getString());
    state.standingState.copyFrom(entity.standingState);
    state.swimmingState.copyFrom(entity.swimmingState);
    state.sleepingState.copyFrom(entity.sleepingState);
  }

  @Override
  public void render(
      BirdRenderState state, PoseStack matrices, MultiBufferSource buffer, int packedLight) {
    this.model = this.modelPair.getModel(state.isBaby);
    if (state.isBaby) {
      matrices.pushPose();
      matrices.scale(0.8F, 0.8F, 0.8F);
      super.render(state, matrices, buffer, packedLight);
      matrices.popPose();
    } else {
      super.render(state, matrices, buffer, packedLight);
    }
  }

  @Override
  public Identifier getTextureLocation(BirdRenderState state) {
    return state.isBaby ? BABY_TEXTURE : TEXTURE;
  }

  @Override
  protected void scale(BirdRenderState state, PoseStack matrices, float amount) {
    super.scale(state, matrices, amount);
    if (state.customName != null) {
      switch (state.customName.toLowerCase()) {
        case "rico" -> matrices.scale(1.1F, 1F, 1F);
        case "skipper" -> matrices.scale(1.25F, 0.9F, 1F);
        case "kowalski" -> matrices.scale(1F, 1.1F, 1F);
        case "private" -> matrices.scale(1.2F, 0.85F, 1F);
      }
    }
  }
}