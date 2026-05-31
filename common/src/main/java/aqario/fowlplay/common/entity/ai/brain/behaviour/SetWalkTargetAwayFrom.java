package aqario.fowlplay.common.entity.ai.brain.behaviour;

import aqario.fowlplay.common.entity.ai.navigation.BirdRandomPos;
import aqario.fowlplay.common.entity.bird.BirdEntity;
import aqario.fowlplay.common.util.CylindricalRadius;
import aqario.fowlplay.common.util.MemoryList;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.BiPredicate;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.Vec3;

public class SetWalkTargetAwayFrom<E extends BirdEntity> extends SpeedModifiableBehaviour<E> {
  private static final MemoryList MEMORIES =
      MemoryList.create(1).absent(MemoryModuleType.WALK_TARGET);
  protected CylindricalRadius radius = new CylindricalRadius(32, 16);
  protected BiPredicate<E, Vec3> positionPredicate = (entity, pos) -> true;

  public SetWalkTargetAwayFrom<E> radius(int radius) {
    return this.radius(radius, radius);
  }

  public SetWalkTargetAwayFrom<E> radius(int xz, int y) {
    this.radius = new CylindricalRadius(xz, y);
    return this;
  }

  public SetWalkTargetAwayFrom<E> walkTargetPredicate(BiPredicate<E, Vec3> predicate) {
    this.positionPredicate = predicate;
    return this;
  }

  @Override
  protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
    return MEMORIES;
  }

  @Override
  protected void start(E entity) {
    Vec3 targetPos = this.getTargetPos(entity);
    if (!this.positionPredicate.test(entity, targetPos)) {
      targetPos = null;
    }
    if (targetPos != null) {
      Brain<?> brain = entity.getBrain();
      brain.setMemory(
          MemoryModuleType.WALK_TARGET,
          new WalkTarget(targetPos, this.speedModifier.apply(entity, targetPos), 0));
    } else {
      entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }
  }

  protected Vec3 getTargetPos(E entity) {
    // Fallback to BirdRandomPos.getGround if getAway not available
    Vec3 pos = BirdRandomPos.getGround(entity, this.radius);
    if (pos == null) pos = BirdRandomPos.getNonAir(entity, this.radius);
    return pos;
  }
}