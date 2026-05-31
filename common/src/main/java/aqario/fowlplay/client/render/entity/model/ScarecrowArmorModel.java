package aqario.fowlplay.client.render.entity.model;

import aqario.fowlplay.client.render.entity.BirdRenderState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ScarecrowArmorModel extends HumanoidModel<BirdRenderState> {
  public ScarecrowArmorModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer(CubeDeformation dilation) {
    MeshDefinition modelData = HumanoidModel.createMesh(dilation, 0.0F);
    PartDefinition modelPartData = modelData.getRoot();
    modelPartData.addOrReplaceChild(
        PartNames.HAT,
        CubeListBuilder.create()
            .texOffs(32, 0)
            .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.extend(0.5F)),
        PartPose.offset(0.0F, 1.0F, 0.0F));
    modelPartData.addOrReplaceChild(
        PartNames.BODY,
        CubeListBuilder.create()
            .texOffs(16, 16)
            .addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation.extend(0.1F)),
        PartPose.offset(0.0F, 10.0F, 0.0F));
    modelPartData.addOrReplaceChild(
        PartNames.HEAD,
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation),
        PartPose.offset(0.0F, 1.0F, 0.0F));
    modelPartData.addOrReplaceChild(
        PartNames.LEFT_LEG,
        CubeListBuilder.create()
            .texOffs(0, 16)
            .mirror()
            .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation),
        PartPose.offset(1.9F, 11.0F, 0.0F));
    modelPartData.addOrReplaceChild(
        PartNames.RIGHT_LEG,
        CubeListBuilder.create()
            .texOffs(0, 16)
            .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation),
        PartPose.offset(-1.9F, 11.0F, 0.0F));
    return LayerDefinition.create(modelData, 64, 32);
  }

  @Override
  public void setupAnim(BirdRenderState state) {
    this.head.xRot = state.headRotX;
    this.head.yRot = state.headRotY;
    this.head.zRot = state.headRotZ;
    this.body.xRot = state.bodyRotX;
    this.body.yRot = state.bodyRotY;
    this.body.zRot = state.bodyRotZ;
    this.leftArm.xRot = state.leftArmRotX;
    this.leftArm.yRot = state.leftArmRotY;
    this.leftArm.zRot = state.leftArmRotZ;
    this.rightArm.xRot = state.rightArmRotX;
    this.rightArm.yRot = state.rightArmRotY;
    this.rightArm.zRot = state.rightArmRotZ;
    this.leftLeg.xRot = 0;
    this.leftLeg.yRot = 0;
    this.leftLeg.zRot = 0;
    this.rightLeg.xRot = 0;
    this.rightLeg.yRot = 0;
    this.rightLeg.zRot = 0;
    this.hat.copyFrom(this.head);
  }
}