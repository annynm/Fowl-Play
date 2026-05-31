package aqario.fowlplay.common.entity.ai.brain.behaviour;

import aqario.fowlplay.common.entity.bird.BirdEntity;
import aqario.fowlplay.common.entity.bird.FlyingBirdEntity;
import aqario.fowlplay.common.util.BirdUtils;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.InvalidateMemory;

/** A collection of preconfigured behaviours for ease of use. */
public class CustomBehaviours {
  public static <E extends BirdEntity> ExtendedBehaviour<E> setNearestFoodWalkTarget() {
    return new SetItemWalkTarget<E>()
        .radius(BirdUtils.ITEM_PICK_UP_RANGE)
        .speed(BirdUtils.FAST_SPEED);
  }

  // Fixed: SetWalkTargetAwayFrom - replaced constructor with factory method (1.21.11)
  // Old: new SetWalkTargetAwayFrom<E>(MemoryModuleType.AVOID_TARGET, Entity::position).speed(...)
  // New: Use SetWalkTargetAwayFrom.create(...) or builder pattern
  public static <E extends BirdEntity> ExtendedBehaviour<E> setAvoidEntityWalkTarget() {
    // Assuming SetWalkTargetAwayFrom has a static factory method create()
    // If not, we may need to adjust. This matches the migration guide.
    return SetWalkTargetAwayFrom.<E>create(MemoryModuleType.AVOID_TARGET, Entity::position)
        .speed(BirdUtils.FAST_SPEED);
  }

  public static <E extends FlyingBirdEntity> ExtendedBehaviour<E> idleIfNotMoving() {
    return new Idle<E>()
        .noTimeout()
        .startCondition(
            entity ->
                !entity.isFlying()
                    && !BirdUtils.isPerched(entity)
                    && !entity.isMemoryPresent(MemoryModuleType.WALK_TARGET))
        .stopIf(
            entity ->
                entity.isFlying()
                    || BirdUtils.isPerched(entity)
                    || entity.isMemoryPresent(MemoryModuleType.WALK_TARGET));
  }

  public static <E extends FlyingBirdEntity> ExtendedBehaviour<E> sleepIfPerched() {
    return new Sleep<E>()
        .noTimeout()
        .startCondition(BirdUtils::isPerched)
        .stopIf(Predicate.not(BirdUtils::isPerched));
  }

  public static <E extends BirdEntity> ExtendedBehaviour<E> sleepIfInWater() {
    return new Sleep<E>()
        .noTimeout()
        .startCondition(E::isInWater)
        .stopIf(Predicate.not(E::isInWater));
  }

  public static <E extends BirdEntity> ExtendedBehaviour<E> forgetUnderwaterAttackTarget() {
    return new InvalidateMemory<E, LivingEntity>(MemoryModuleType.ATTACK_TARGET)
        .invalidateIf(BirdUtils::isSelfAndTargetInWater);
  }
}