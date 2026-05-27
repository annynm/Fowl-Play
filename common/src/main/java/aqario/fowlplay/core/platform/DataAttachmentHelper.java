package aqario.fowlplay.core.platform;

import aqario.fowlplay.common.entity.variant.ChickenVariant;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.ChickenEntity;

public class DataAttachmentHelper {
  @ExpectPlatform
  public static Holder<ChickenVariant> getChickenVariant(ChickenEntity entity) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void setChickenVariant(ChickenEntity entity, Holder<ChickenVariant> variant) {
    throw new AssertionError();
  }
}