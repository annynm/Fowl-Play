package aqario.fowlplay.client;

import aqario.fowlplay.client.render.entity.layer.*;
import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class RenderRegistry {

  /**
   * Registers all entity model layers for every bird entity. Call this during client
   * initialization.
   */
  public static void registerAllLayers() {
    // Penguins
    modelLayer(PenguinLayer.MODEL_LAYER, PenguinLayer::createLayerDefinition);
    modelLayer(BabyPenguinLayer.MODEL_LAYER, BabyPenguinLayer::createLayerDefinition);

    // TODO: Add remaining bird layers as they are migrated:
    // modelLayer(DuckLayer.MODEL_LAYER, DuckLayer::createLayerDefinition);
    // modelLayer(GooseLayer.MODEL_LAYER, GooseLayer::createLayerDefinition);
    // modelLayer(GullLayer.MODEL_LAYER, GullLayer::createLayerDefinition);
    // modelLayer(SparrowLayer.MODEL_LAYER, SparrowLayer::createLayerDefinition);
    // modelLayer(PigeonLayer.MODEL_LAYER, PigeonLayer::createLayerDefinition);
    // modelLayer(RobinLayer.MODEL_LAYER, RobinLayer::createLayerDefinition);
    // modelLayer(CardinalLayer.MODEL_LAYER, CardinalLayer::createLayerDefinition);
    // modelLayer(BlueJayLayer.MODEL_LAYER, BlueJayLayer::createLayerDefinition);
    // modelLayer(CrowLayer.MODEL_LAYER, CrowLayer::createLayerDefinition);
    // modelLayer(RavenLayer.MODEL_LAYER, RavenLayer::createLayerDefinition);
    // modelLayer(HawkLayer.MODEL_LAYER, HawkLayer::createLayerDefinition);
    // modelLayer(VultureLayer.MODEL_LAYER, VultureLayer::createLayerDefinition);
    // modelLayer(ChickadeeLayer.MODEL_LAYER, ChickadeeLayer::createLayerDefinition);
    // modelLayer(ChickenLayer.MODEL_LAYER, ChickenLayer::createLayerDefinition);
    // modelLayer(BabyChickenLayer.MODEL_LAYER, BabyChickenLayer::createLayerDefinition);
    // modelLayer(ScarecrowLayer.MODEL_LAYER, ScarecrowLayer::createLayerDefinition);
  }

  @ExpectPlatform
  public static <T extends Entity> void entityRenderer(
      Supplier<EntityType<T>> type, EntityRendererProvider<T> provider) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static void modelLayer(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static <T extends ParticleOptions, P extends ParticleType<T>> void particleFactory(
      Supplier<P> supplier, WrappedParticleProvider<T> provider) {
    throw new AssertionError();
  }

  @FunctionalInterface
  public interface WrappedParticleProvider<T extends ParticleOptions> {
    ParticleProvider<T> create(SpriteSet spriteSet);
  }
}