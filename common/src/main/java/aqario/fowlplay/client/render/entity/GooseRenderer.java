package aqario.fowlplay.client.render.entity;

import aqario.fowlplay.client.render.entity.layer.BirdHeldItemLayer;
import aqario.fowlplay.client.render.entity.model.AdultBabyModelPair;
import aqario.fowlplay.client.render.entity.model.BabyGooseModel;
import aqario.fowlplay.client.render.entity.model.DomesticGooseModel;
import aqario.fowlplay.client.render.entity.model.GooseModel;
import aqario.fowlplay.common.entity.bird.waterfowl.GooseEntity;
import aqario.fowlplay.common.entity.variant.GooseVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class GooseRenderer extends MobRenderer<GooseEntity, BirdRenderState, GooseModel> {
  private final Map<GooseVariant.ModelType, AdultBabyModelPair<BirdRenderState, GooseModel>> models;

  public GooseRenderer(EntityRendererProvider.Context context) {
    super(context, new GooseModel(context.bakeLayer(GooseModel.MODEL_LAYER)), 0.3f);
    this.addLayer(
        new BirdHeldItemLayer<>(
            this, context.getItemInHandRenderer(), new Vec3(0.0, -0.05375, -0.1475)));
    this.models =
        Map.of(
            GooseVariant.ModelType.WILD,
            new AdultBabyModelPair<>(
                new GooseModel(context.bakeLayer(GooseModel.MODEL_LAYER)),
                new BabyGooseModel(context.bakeLayer(BabyGooseModel.MODEL_LAYER))),
            GooseVariant.ModelType.DOMESTIC,
            new AdultBabyModelPair<>(
                new DomesticGooseModel(context.bakeLayer(DomesticGooseModel.MODEL_LAYER)),
                new BabyGooseModel(context.bakeLayer(BabyGooseModel.MODEL_LAYER))));
  }

  @Override
  public BirdRenderState createRenderState() {
    return new BirdRenderState();
  }

  @Override
  protected void extractRenderState(GooseEntity entity, BirdRenderState state, float partialTick) {
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
    state.isDomestic = entity.isDomestic();
    state.gooseModelType = entity.getVariant().value().modelType(entity.isDomestic());
    state.variantTexture =
        entity.getVariant().value().texture(entity.isBaby(), entity.isDomestic());
    state.standingState.copyFrom(entity.standingState);
    state.swimmingState.copyFrom(entity.swimmingState);
    state.sleepingState.copyFrom(entity.sleepingState);
    state.glidingState.copyFrom(entity.glidingState);
    state.flappingState.copyFrom(entity.flappingState);
  }

  @Override
  public void render(
      BirdRenderState state,
      PoseStack matrices,
      MultiBufferSource vertexConsumerProvider,
      int packedLight) {
    this.model = this.models.get(state.gooseModelType).getModel(state.isBaby);
    super.render(state, matrices, vertexConsumerProvider, packedLight);
  }

  @Override
  public Identifier getTextureLocation(BirdRenderState state) {
    return state.variantTexture;
  }
}