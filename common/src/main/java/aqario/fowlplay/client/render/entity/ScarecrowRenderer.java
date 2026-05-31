package aqario.fowlplay.client.render.entity;

import aqario.fowlplay.client.render.entity.layer.StuckArrowsLayer;
import aqario.fowlplay.client.render.entity.model.ScarecrowArmorModel;
import aqario.fowlplay.client.render.entity.model.ScarecrowModel;
import aqario.fowlplay.common.entity.ScarecrowEntity;
import aqario.fowlplay.core.FowlPlay;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
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
            ctx.getModelSet())); // Changed: getModelManager() -> getModelSet()
    this.addLayer(new StuckArrowsLayer<>(ctx, this));
    // Changed: ctx.getItemInHandRenderer() replaced by ctx.getItemModelResolver() (or similar)
    // For 1.21.11, ItemInHandLayer constructor expects ItemModelResolver
    this.addLayer(new ItemInHandLayer<>(this, ctx.getItemModelResolver()));
    this.addLayer(new WingsLayer<>(this, ctx.getModelSet()));
    // CustomHeadLayer also uses ItemModelResolver
    this.addLayer(new CustomHeadLayer<>(this, ctx.getModelSet(), ctx.getItemModelResolver()));
  }

  @Override
  public BirdRenderState createRenderState() {
    return new BirdRenderState();
  }

  @Override
  public void extractRenderState(ScarecrowEntity entity, BirdRenderState state, float partialTick) {
    super.extractRenderState(entity, state, partialTick);
    state.ageInTicks = entity.tickCount + partialTick;
    // Changed: Rotations record accessors .x(), .y(), .z() instead of .getX(), .getY(), .getZ()
    state.headRotX = (float) (Math.PI / 180.0) * entity.getHeadRotation().x();
    state.headRotY = (float) (Math.PI / 180.0) * entity.getHeadRotation().y();
    state.headRotZ = (float) (Math.PI / 180.0) * entity.getHeadRotation().z();
    state.bodyRotX = (float) (Math.PI / 180.0) * entity.getBodyRotation().x();
    state.bodyRotY = (float) (Math.PI / 180.0) * entity.getBodyRotation().y();
    state.bodyRotZ = (float) (Math.PI / 180.0) * entity.getBodyRotation().z();
    state.leftArmRotX = (float) (Math.PI / 180.0) * entity.getLeftArmRotation().x();
    state.leftArmRotY = (float) (Math.PI / 180.0) * entity.getLeftArmRotation().y();
    state.leftArmRotZ = (float) (Math.PI / 180.0) * entity.getLeftArmRotation().z();
    state.rightArmRotX = (float) (Math.PI / 180.0) * entity.getRightArmRotation().x();
    state.rightArmRotY = (float) (Math.PI / 180.0) * entity.getRightArmRotation().y();
    state.rightArmRotZ = (float) (Math.PI / 180.0) * entity.getRightArmRotation().z();
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