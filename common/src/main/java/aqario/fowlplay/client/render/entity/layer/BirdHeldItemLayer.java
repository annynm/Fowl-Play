package aqario.fowlplay.client.render.entity.layer;

import aqario.fowlplay.client.render.entity.BirdRenderState;
import aqario.fowlplay.client.render.entity.model.BirdModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BirdHeldItemLayer<S extends BirdRenderState, M extends BirdModel<S>>
    extends RenderLayer<S, M> {
  private final ItemInHandRenderer itemRenderer;
  private final Vec3 heldItemOffset;

  public BirdHeldItemLayer(
      RenderLayerParent<S, M> renderer, ItemInHandRenderer itemRenderer, Vec3 heldItemOffset) {
    super(renderer);
    this.itemRenderer = itemRenderer;
    this.heldItemOffset = heldItemOffset;
  }

  @Override
  public void render(
      PoseStack matrices,
      MultiBufferSource vertexConsumers,
      int light,
      S state,
      float limbAngle,
      float limbDistance) {
    ItemStack heldItem = state.heldItem; // Requires heldItem field in BirdRenderState
    if (heldItem.isEmpty()) return;

    matrices.pushPose();

    ModelPartProxy root = getModelPartProxy(getParentModel().root);
    ModelPartProxy body = getModelPartProxy(getParentModel().body);
    ModelPartProxy neck = getModelPartProxy(getParentModel().neck);
    ModelPartProxy head = getModelPartProxy(getParentModel().head);

    matrices.translate(root.x / 16.0F, root.y / 16.0F, root.z / 16.0F);
    matrices.mulPose(Axis.ZP.rotation(root.zRot));
    matrices.mulPose(Axis.YP.rotation(root.yRot));
    matrices.mulPose(Axis.XP.rotation(root.xRot));

    matrices.translate(body.x / 16.0F, body.y / 16.0F, body.z / 16.0F);
    matrices.mulPose(Axis.ZP.rotation(body.zRot));
    matrices.mulPose(Axis.YP.rotation(body.yRot));
    matrices.mulPose(Axis.XP.rotation(body.xRot));

    matrices.translate(neck.x / 16.0F, neck.y / 16.0F, neck.z / 16.0F);
    matrices.mulPose(Axis.ZP.rotation(neck.zRot));
    matrices.mulPose(Axis.YP.rotation(neck.yRot));
    matrices.mulPose(Axis.XP.rotation(neck.xRot));

    matrices.translate(head.x / 16.0F, head.y / 16.0F, head.z / 16.0F);
    matrices.mulPose(Axis.ZP.rotation(head.zRot));
    matrices.mulPose(Axis.YP.rotation(head.yRot));
    matrices.mulPose(Axis.XP.rotation(head.xRot));

    matrices.translate(this.heldItemOffset.x, this.heldItemOffset.y, this.heldItemOffset.z);
    matrices.mulPose(Axis.XN.rotationDegrees(90.0F));
    matrices.scale(0.5F, 0.5F, 0.5F);

    this.itemRenderer.renderItem(
        null, heldItem, ItemDisplayContext.GROUND, false, matrices, vertexConsumers, light);
    matrices.popPose();
  }

  private static ModelPartProxy getModelPartProxy(net.minecraft.client.model.geom.ModelPart part) {
    return new ModelPartProxy(part.x, part.y, part.z, part.xRot, part.yRot, part.zRot);
  }

  private record ModelPartProxy(float x, float y, float z, float xRot, float yRot, float zRot) {}
}