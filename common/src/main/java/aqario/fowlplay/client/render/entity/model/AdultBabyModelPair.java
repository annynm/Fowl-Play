package aqario.fowlplay.client.render.entity.model;

import aqario.fowlplay.client.render.entity.state.BirdRenderState;
import net.minecraft.client.model.EntityModel;

public record AdultBabyModelPair<S extends BirdRenderState, M extends EntityModel<S>>(
    M adultModel, M babyModel) {

  public M getModel(boolean isBaby) {
    return isBaby ? this.babyModel : this.adultModel;
  }
}