package aqario.fowlplay.common.entity.ai.brain;

import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrain;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.Nullable;

public class ExtendedBrain<E extends LivingEntity & SmartBrainOwner<E>> extends SmartBrain<E> {

  public ExtendedBrain(
      List<MemoryModuleType<?>> memories,
      List<? extends ExtendedSensor<E>> extendedSensors,
      @Nullable List<BrainActivityGroup<E>> taskList) {
    super(memories, extendedSensors, taskList);
  }

  @Override
  public void tick(ServerLevel level, E entity) {
    super.tick(level, entity);

    if (entity instanceof Mob mob) {
      mob.setAggressive(this.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }
  }
}