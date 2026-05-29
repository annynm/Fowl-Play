package aqario.fowlplay.client.render.entity;

import aqario.fowlplay.client.render.entity.model.AdultBabyModelPair;
import aqario.fowlplay.client.render.entity.model.CustomBabyChickenModel;
import aqario.fowlplay.client.render.entity.model.CustomChickenModel;
import aqario.fowlplay.common.entity.bird.VariantHolder;
import aqario.fowlplay.common.entity.variant.ChickenVariant;
import aqario.fowlplay.common.util.ChickenAnimationHolder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.chicken.Chicken;

public class CustomChickenRenderer
    extends MobRenderer<Chicken, BirdRenderState, CustomChickenModel> {
  private final AdultBabyModelPair<BirdRenderState, CustomChickenModel> modelPair;

  public CustomChickenRenderer(EntityRendererProvider.Context context) {
    super(context, new CustomChickenModel(context.bakeLayer(CustomChickenModel.MODEL_LAYER)), 0.3f);
    this.modelPair =
        new AdultBabyModelPair<>(
            new CustomChickenModel(context.bakeLayer(CustomChickenModel.MODEL_LAYER)),
            new CustomBabyChickenModel(context.bakeLayer(CustomBabyChickenModel.MODEL_LAYER)));
  }

  @Override
  public BirdRenderState createRenderState() {
    return new BirdRenderState();
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void extractRenderState(Chicken entity, BirdRenderState state, float partialTick) {
    super.extractRenderState(entity, state, partialTick);
    state.ageInTicks = entity.tickCount + partialTick;
    state.limbSwing = entity.walkAnimation.position();
    state.limbSwingAmount = entity.walkAnimation.speed();
    state.bodyYaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
    state.headYaw = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
    state.headPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
    state.isInWaterOrBubble = entity.isInWaterOrBubble();
    state.onGround = entity.onGround();
    state.customName = ChatFormatting.stripFormatting(entity.getName().getString());
    state.variantTexture =
        ((VariantHolder<ChickenVariant>) entity).getVariant().value().texture(entity.isBaby());
    ChickenAnimationHolder holder = (ChickenAnimationHolder) entity;
    state.chickenStandingState.copyFrom(holder.fowlplay$getStandingState());
    state.chickenFlappingState.copyFrom(holder.fowlplay$getFlappingState());
    state.chickenFloatingState.copyFrom(holder.fowlplay$getFloatingState());
  }

  @Override
  public void render(
      BirdRenderState state,
      PoseStack matrices,
      MultiBufferSource vertexConsumerProvider,
      int packedLight) {
    this.model = this.modelPair.getModel(state.isBaby);
    if (state.isBaby) {
      matrices.pushPose();
      matrices.scale(0.8F, 0.8F, 0.8F);
      super.render(state, matrices, vertexConsumerProvider, packedLight);
      matrices.popPose();
    } else {
      super.render(state, matrices, vertexConsumerProvider, packedLight);
    }
  }

  @Override
  public Identifier getTextureLocation(BirdRenderState state) {
    return state.variantTexture;
  }
}