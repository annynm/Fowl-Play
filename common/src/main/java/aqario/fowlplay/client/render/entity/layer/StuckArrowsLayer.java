package aqario.fowlplay.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class StuckArrowsLayer<
        S extends LivingEntityRenderState, M extends net.minecraft.client.model.EntityModel<S>>
    extends RenderLayer<S, M> {

  public StuckArrowsLayer(RenderLayerParent<S, M> renderer) {
    super(renderer);
  }

  protected int getObjectCount(S renderState) {
    return renderState.arrowCount;
  }

  @Override
  public void render(
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      S renderState,
      float limbSwing,
      float limbSwingAmount) {
    int count = this.getObjectCount(renderState);
    if (count == 0) return;

    RandomSource random = RandomSource.create(renderState.hashCode());
    ItemStack arrowStack = new ItemStack(Items.ARROW);

    for (int i = 0; i < count; i++) {
      poseStack.pushPose();

      float r = (random.nextFloat() - 0.5F) * 0.6F;
      float s = (random.nextFloat() - 0.5F) * 0.6F;
      float t = (random.nextFloat() - 0.5F) * 0.6F;
      poseStack.translate(r, s, t);

      poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
      poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
      poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F));

      poseStack.scale(0.5F, 0.5F, 0.5F);

      Minecraft.getInstance()
          .getItemRenderer()
          .renderStatic(
              arrowStack,
              ItemDisplayContext.FIXED,
              packedLight,
              OverlayTexture.NO_OVERLAY,
              poseStack,
              buffer,
              null,
              0);

      poseStack.popPose();
    }
  }
}