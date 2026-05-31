package aqario.fowlplay.client.render.entity.layer;

import aqario.fowlplay.client.render.entity.BirdRenderState;
import aqario.fowlplay.client.render.entity.model.PigeonModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PigeonBundleLayer extends RenderLayer<BirdRenderState, PigeonModel> {
  private final ItemInHandRenderer itemRenderer;

  public PigeonBundleLayer(
      RenderLayerParent<BirdRenderState, PigeonModel> renderer, ItemInHandRenderer itemRenderer) {
    super(renderer);
    this.itemRenderer = itemRenderer;
  }

  @Override
  public void render(
      PoseStack matrices,
      MultiBufferSource vertexConsumers,
      int light,
      BirdRenderState state,
      float limbAngle,
      float limbDistance) {
    ItemStack heldItem = state.heldItem;
    if (heldItem.isEmpty()) return;

    matrices.pushPose();

    var rootPose = getParentModel().root.storePose();
    var leftLegPose = getParentModel().leftLeg.storePose();

    matrices.translate(
        getParentModel().root.x / 16.0F,
        getParentModel().root.y / 16.0F,
        getParentModel().root.z / 16.0F);
    matrices.mulPose(Axis.ZP.rotation(rootPose.zRot));
    matrices.mulPose(Axis.YP.rotation(rootPose.yRot));
    matrices.mulPose(Axis.XP.rotation(rootPose.xRot));

    matrices.translate(
        getParentModel().leftLeg.x / 16.0F,
        getParentModel().leftLeg.y / 16.0F,
        getParentModel().leftLeg.z / 16.0F);
    matrices.mulPose(Axis.ZP.rotation(leftLegPose.zRot));
    matrices.mulPose(Axis.YP.rotation(leftLegPose.yRot));
    matrices.mulPose(Axis.XP.rotation(leftLegPose.xRot));

    matrices.translate(0.03125F, 0.075F, 0.0F);
    matrices.mulPose(Axis.XP.rotationDegrees(180.0F));
    matrices.mulPose(Axis.YP.rotationDegrees(90.0F));
    matrices.scale(0.25F, 0.25F, 0.25F);

    this.itemRenderer.renderItem(
        null, heldItem, ItemDisplayContext.GROUND, false, matrices, vertexConsumers, light);
    matrices.popPose();
  }
}