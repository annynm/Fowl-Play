package aqario.fowlplay.common.entity.ai.brain.sensor;

import aqario.fowlplay.common.entity.bird.BirdEntity;
import aqario.fowlplay.common.util.BirdUtils;
import aqario.fowlplay.core.FPMemoryTypes;
import aqario.fowlplay.core.FPSensorTypes;
import java.util.List;
import java.util.function.BiPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.api.core.sensor.EntityFilteringSensor;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.Nullable;

public class AvoidTargetSensor<E extends BirdEntity>
    extends EntityFilteringSensor<LivingEntity, E> {
  public AvoidTargetSensor() {
    this.setScanRate(bird -> 10);
  }

  @Override
  protected MemoryModuleType<LivingEntity> getMemory() {
    return MemoryModuleType.AVOID_TARGET;
  }

  @Override
  public List<MemoryModuleType<?>> memoriesUsed() {
    return List.of(
        this.getMemory(),
        MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
        FPMemoryTypes.IS_AVOIDING.get());
  }

  @Override
  public SensorType<? extends ExtendedSensor<?>> type() {
    return FPSensorTypes.AVOID_TARGETS.get();
  }

  @Override
  protected BiPredicate<LivingEntity, E> predicate() {
    return (target, self) -> BirdUtils.shouldAvoid(self, target);
  }

  @Override
  protected @Nullable LivingEntity findMatches(E bird, NearestVisibleLivingEntities matcher) {
    return matcher.findClosest(target -> this.predicate().test(target, bird)).orElse(null);
  }

  @Override
  protected void doTick(ServerLevel level, E bird) {
    LivingEntity avoidTarget = this.testForEntity(bird);
    if (avoidTarget != null) {
      bird.getBrain().setMemory(this.getMemory(), avoidTarget);
    } else {
      bird.getBrain().eraseMemory(this.getMemory());
    }
    if (avoidTarget != null && avoidTarget.closerThan(bird, bird.getFleeRange(avoidTarget))) {
      bird.getBrain().setMemory(FPMemoryTypes.IS_AVOIDING.get(), Unit.INSTANCE);
    } else {
      bird.getBrain().eraseMemory(FPMemoryTypes.IS_AVOIDING.get());
    }
  }
}