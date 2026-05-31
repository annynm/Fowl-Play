package aqario.fowlplay.client.render.entity.model;

import aqario.fowlplay.client.render.entity.BirdRenderState;
import net.minecraft.client.model.EntityModel;

public record AdultBabyModelPair<S extends BirdRenderState, M extends net.minecraft.client.model.EntityModel<S>>(
    M adultModel, M babyModel) {

  public M getModel(boolean isBaby) {
    return isBaby ? this.babyModel : this.adultModel;
  }
}