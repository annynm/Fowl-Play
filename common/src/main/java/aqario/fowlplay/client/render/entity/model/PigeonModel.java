package aqario.fowlplay.client.render.entity.model;

import aqario.fowlplay.client.render.entity.BirdRenderState;
import aqario.fowlplay.client.render.entity.animation.PigeonAnimations;
import aqario.fowlplay.core.FowlPlay;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PigeonModel extends FlyingBirdModel<BirdRenderState> {
  public static final ModelLayerLocation MODEL_LAYER =
      new ModelLayerLocation(FowlPlay.id("pigeon"), "main");

  private final KeyframeAnimation walkingAnim;
  private final KeyframeAnimation standingAnim;
  private final KeyframeAnimation swimmingAnim;
  private final KeyframeAnimation glidingAnim;
  private final KeyframeAnimation flappingAnim;
  private final KeyframeAnimation sittingAnim;

  public PigeonModel(ModelPart root) {
    super(root);
    this.walkingAnim = PigeonAnimations.WALKING.bake(root);
    this.standingAnim = PigeonAnimations.STANDING.bake(root);
    this.swimmingAnim = PigeonAnimations.SWIMMING.bake(root);
    this.glidingAnim = PigeonAnimations.GLIDING.bake(root);
    this.flappingAnim = PigeonAnimations.FLAPPING.bake(root);
    this.sittingAnim = PigeonAnimations.SITTING.bake(root);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition modelData = new MeshDefinition();
    PartDefinition modelPartData = modelData.getRoot();
    PartDefinition root =
        modelPartData.addOrReplaceChild(
            "root", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, -1.0F));

    PartDefinition body =
        root.addOrReplaceChild(
            "body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.25F, 1.0F));

    PartDefinition neck =
        body.addOrReplaceChild(
            "neck",
            CubeListBuilder.create()
                .texOffs(0, 14)
                .addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.001F)),
            PartPose.offset(0.0F, -1.75F, -2.5F));

    PartDefinition head =
        neck.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 9)
                .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -3.0F, 0.0F));

    head.addOrReplaceChild(
        "beak",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, -0.5F, -1.0F));

    body.addOrReplaceChild(
        "torso",
        CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-1.5F, -2.0F, -4.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(0.0F, -0.25F, 0.0F, -0.6109F, 0.0F, 0.0F));

    body.addOrReplaceChild(
        "left_wing",
        CubeListBuilder.create()
            .texOffs(9, 0)
            .addBox(-1.5F, -1.0F, -0.5F, 2.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(1.5F, -3.0F, -2.0F, -0.4363F, 0.0F, 0.0F));

    body.addOrReplaceChild(
        "right_wing",
        CubeListBuilder.create()
            .texOffs(9, 0)
            .mirror()
            .addBox(-0.5F, -1.0F, -0.5F, 2.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
            .mirror(false),
        PartPose.offsetAndRotation(-1.5F, -3.0F, -2.0F, -0.4363F, 0.0F, 0.0F));

    PartDefinition left_wing_open =
        body.addOrReplaceChild(
            "left_wing_open",
            CubeListBuilder.create()
                .texOffs(8, 12)
                .addBox(-0.5F, 0.0F, -1.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(1.0F, -3.25F, -1.0F, -0.6109F, 0.0F, 0.0F));

    left_wing_open.addOrReplaceChild(
        "left_wing_outer",
        CubeListBuilder.create()
            .texOffs(2, 19)
            .addBox(0.0F, 0.0F, 0.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)),
        PartPose.offset(5.5F, 0.0F, -1.0F));

    PartDefinition right_wing_open =
        body.addOrReplaceChild(
            "right_wing_open",
            CubeListBuilder.create()
                .texOffs(8, 12)
                .mirror()
                .addBox(-5.5F, 0.0F, -1.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .mirror(false),
            PartPose.offsetAndRotation(-1.0F, -3.25F, -1.0F, -0.6109F, 0.0F, 0.0F));

    right_wing_open.addOrReplaceChild(
        "right_wing_outer",
        CubeListBuilder.create()
            .texOffs(2, 19)
            .mirror()
            .addBox(-6.0F, 0.0F, 0.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
            .mirror(false),
        PartPose.offset(-5.5F, 0.0F, -1.0F));

    PartDefinition tail =
        body.addOrReplaceChild(
            "tail",
            CubeListBuilder.create()
                .texOffs(22, 0)
                .addBox(-1.0F, -1.0F, 1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(0.0F, -0.25F, 1.0F, -0.5236F, 0.0F, 0.0F));

    tail.addOrReplaceChild(
        "cube_r1",
        CubeListBuilder.create()
            .texOffs(7, 0)
            .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-0.25F, -1.0F, 1.0F, 0.0F, -0.2182F, 0.0F));

    tail.addOrReplaceChild(
        "cube_r2",
        CubeListBuilder.create()
            .texOffs(7, 0)
            .mirror()
            .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F))
            .mirror(false),
        PartPose.offsetAndRotation(0.25F, -1.0F, 1.0F, 0.0F, 0.2182F, 0.0F));

    PartDefinition left_leg =
        root.addOrReplaceChild(
            "left_leg",
            CubeListBuilder.create()
                .texOffs(16, 3)
                .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(1.0F, 0.5F, 1.0F, -0.2618F, 0.0F, 0.0F));

    left_leg.addOrReplaceChild(
        "cube_r3",
        CubeListBuilder.create()
            .texOffs(15, 2)
            .addBox(-0.5F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.2618F, 0.0F, 0.0F));

    PartDefinition right_leg =
        root.addOrReplaceChild(
            "right_leg",
            CubeListBuilder.create()
                .texOffs(16, 3)
                .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(-1.0F, 0.5F, 1.0F, -0.2618F, 0.0F, 0.0F));

    right_leg.addOrReplaceChild(
        "cube_r4",
        CubeListBuilder.create()
            .texOffs(15, 2)
            .addBox(-0.5F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.2618F, 0.0F, 0.0F));

    return LayerDefinition.create(modelData, 32, 32);
  }

  @Override
  protected void setAnimations(BirdRenderState state) {
    if (!state.isFlying && !state.isInWaterOrBubble) {
      this.walkingAnim.applyWalk(state.limbSwing, state.limbSwingAmount, 5F, 5F);
    }
    this.standingAnim.apply(state.standingState, state.ageInTicks);
    this.swimmingAnim.apply(state.swimmingState, state.ageInTicks);
    this.glidingAnim.apply(state.glidingState, state.ageInTicks);
    this.flappingAnim.apply(state.flappingState, state.ageInTicks);
    this.sittingAnim.apply(state.sittingState, state.ageInTicks);
  }
}