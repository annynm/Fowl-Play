package aqario.fowlplay.client.render.entity;

import aqario.fowlplay.client.render.entity.StuckArrowsLayer;
import aqario.fowlplay.client.render.entity.model.ScarecrowArmorModel;
import aqario.fowlplay.client.render.entity.model.ScarecrowModel;
import aqario.fowlplay.common.entity.ScarecrowEntity;
import aqario.fowlplay.core.FowlPlay;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.Identifier;

public class ScarecrowRenderer
    extends LivingEntityRenderer<ScarecrowEntity, BirdRenderState, ScarecrowModel> {
  public static final Identifier TEXTURE = FowlPlay.id("textures/entity/scarecrow/scarecrow.png");

  public ScarecrowRenderer(EntityRendererProvider.Context ctx) {
    super(ctx, new ScarecrowModel(ctx.bakeLayer(ScarecrowModel.MODEL_LAYER)), 0.0F);
    this.addLayer(
        new HumanoidArmorLayer<>(
            this,
            new ScarecrowArmorModel(ctx.bakeLayer(ScarecrowModel.INNER_ARMOR)),
            new ScarecrowArmorModel(ctx.bakeLayer(ScarecrowModel.OUTER_ARMOR)),
            ctx.getModelManager()));
    this.addLayer(new StuckArrowsLayer<>(ctx, this));
    this.addLayer(new ItemInHandLayer<>(this, ctx.getItemInHandRenderer()));
    this.addLayer(new WingsLayer<>(this, ctx.getModelSet()));
    this.addLayer(new CustomHeadLayer<>(this, ctx.getModelSet(), ctx.getItemInHandRenderer()));
  }

  @Override
  public BirdRenderState createRenderState() {
    return new BirdRenderState();
  }

  @Override
  protected void extractRenderState(
      ScarecrowEntity entity, BirdRenderState state, float partialTick) {
    super.extractRenderState(entity, state, partialTick);
    state.ageInTicks = entity.tickCount + partialTick;
    state.headRotX = (float) (Math.PI / 180.0) * entity.getHeadRotation().getX();
    state.headRotY = (float) (Math.PI / 180.0) * entity.getHeadRotation().getY();
    state.headRotZ = (float) (Math.PI / 180.0) * entity.getHeadRotation().getZ();
    state.bodyRotX = (float) (Math.PI / 180.0) * entity.getBodyRotation().getX();
    state.bodyRotY = (float) (Math.PI / 180.0) * entity.getBodyRotation().getY();
    state.bodyRotZ = (float) (Math.PI / 180.0) * entity.getBodyRotation().getZ();
    state.leftArmRotX = (float) (Math.PI / 180.0) * entity.getLeftArmRotation().getX();
    state.leftArmRotY = (float) (Math.PI / 180.0) * entity.getLeftArmRotation().getY();
    state.leftArmRotZ = (float) (Math.PI / 180.0) * entity.getLeftArmRotation().getZ();
    state.rightArmRotX = (float) (Math.PI / 180.0) * entity.getRightArmRotation().getX();
    state.rightArmRotY = (float) (Math.PI / 180.0) * entity.getRightArmRotation().getY();
    state.rightArmRotZ = (float) (Math.PI / 180.0) * entity.getRightArmRotation().getZ();
  }

  @Override
  protected void scale(BirdRenderState state, PoseStack matrices, float amount) {
    matrices.scale(0.9375F, 0.9375F, 0.9375F);
  }

  @Override
  protected boolean shouldShowName(BirdRenderState state) {
    return false;
  }

  @Override
  public Identifier getTextureLocation(BirdRenderState state) {
    return TEXTURE;
  }
}