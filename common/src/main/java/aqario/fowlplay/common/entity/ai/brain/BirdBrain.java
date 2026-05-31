package aqario.fowlplay.common.entity.ai.brain;

import aqario.fowlplay.common.entity.bird.BirdEntity;
import aqario.fowlplay.common.util.ActivityListBuilder;
import aqario.fowlplay.core.FPActivities;
import aqario.fowlplay.core.FPMemoryTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;

public interface BirdBrain<E extends BirdEntity & BirdBrain<E>> extends SmartBrainOwner<E> {

  default void addActivities(final ActivityListBuilder<E> builder) {
    builder.add(this.avoidActivity());
    builder.add(this.deliverActivity());
    builder.add(this.followActivity());
    builder.add(this.forageActivity());
    builder.add(this.huntActivity());
    builder.add(this.perchActivity());
    builder.add(this.pickUpActivity());
    builder.add(this.restActivity());
    builder.add(this.soarActivity());
  }

  default BrainActivityGroup<? extends E> coreActivity() {
    return BrainActivityGroup.empty();
  }

  default BrainActivityGroup<? extends E> avoidActivity() {
    return BrainActivityGroup.empty();
  }

  default BrainActivityGroup<? extends E> deliverActivity() {
    return BrainActivityGroup.empty();
  }

  default BrainActivityGroup<? extends E> fightActivity() {
    return BrainActivityGroup.empty();
  }

  default BrainActivityGroup<? extends E> followActivity() {
    return BrainActivityGroup.empty();
  }

  default BrainActivityGroup<? extends E> forageActivity() {
    return BrainActivityGroup.empty();
  }

  default BrainActivityGroup<? extends E> huntActivity() {
    return BrainActivityGroup.empty();
  }

  default BrainActivityGroup<? extends E> idleActivity() {
    return BrainActivityGroup.empty();
  }

  default BrainActivityGroup<? extends E> perchActivity() {
    return BrainActivityGroup.empty();
  }

  default BrainActivityGroup<? extends E> pickUpActivity() {
    return BrainActivityGroup.empty();
  }

  default BrainActivityGroup<? extends E> restActivity() {
    return BrainActivityGroup.empty();
  }

  default BrainActivityGroup<? extends E> soarActivity() {
    return BrainActivityGroup.empty();
  }

  @SafeVarargs
  @SuppressWarnings({"unchecked", "rawtypes"})
  static <T extends BirdEntity & BirdBrain<T>> BrainActivityGroup<T> core(
      Behavior<?>... behaviours) {
    return new BrainActivityGroup<T>(Activity.CORE)
        .priority(0)
        .behaviours((Behavior<? super T>[]) behaviours);
  }

  @SafeVarargs
  @SuppressWarnings({"unchecked", "rawtypes"})
  static <T extends BirdEntity & BirdBrain<T>> BrainActivityGroup<T> avoid(
      Behavior<?>... behaviours) {
    return new BrainActivityGroup<T>(Activity.AVOID)
        .priority(10)
        .behaviours((Behavior<? super T>[]) behaviours)
        .requireAndWipeMemoriesOnUse(FPMemoryTypes.IS_AVOIDING.get());
  }

  @SafeVarargs
  @SuppressWarnings({"unchecked", "rawtypes"})
  static <T extends BirdEntity & BirdBrain<T>> BrainActivityGroup<T> deliver(
      Behavior<?>... behaviours) {
    return new BrainActivityGroup<T>(FPActivities.DELIVER.get())
        .priority(10)
        .behaviours((Behavior<? super T>[]) behaviours)
        .requireAndWipeMemoriesOnUse(FPMemoryTypes.RECIPIENT.get());
  }

  @SafeVarargs
  @SuppressWarnings({"unchecked", "rawtypes"})
  static <T extends BirdEntity & BirdBrain<T>> BrainActivityGroup<T> fight(
      Behavior<?>... behaviours) {
    return new BrainActivityGroup<T>(Activity.FIGHT)
        .priority(10)
        .behaviours((Behavior<? super T>[]) behaviours)
        .requireAndWipeMemoriesOnUse(MemoryModuleType.ATTACK_TARGET);
  }

  @SafeVarargs
  @SuppressWarnings({"unchecked", "rawtypes"})
  static <T extends BirdEntity & BirdBrain<T>> BrainActivityGroup<T> follow(
      Behavior<?>... behaviours) {
    return new BrainActivityGroup<T>(FPActivities.FOLLOW.get())
        .priority(10)
        .behaviours((Behavior<? super T>[]) behaviours)
        .requireAndWipeMemoriesOnUse(FPMemoryTypes.IS_FOLLOWING.get());
  }

  @SafeVarargs
  @SuppressWarnings({"unchecked", "rawtypes"})
  static <T extends BirdEntity & BirdBrain<T>> BrainActivityGroup<T> forage(
      Behavior<?>... behaviours) {
    return new BrainActivityGroup<T>(FPActivities.FORAGE.get())
        .priority(10)
        .behaviours((Behavior<? super T>[]) behaviours);
  }

  @SafeVarargs
  @SuppressWarnings({"unchecked", "rawtypes"})
  static <T extends BirdEntity & BirdBrain<T>> BrainActivityGroup<T> hunt(
      Behavior<?>... behaviours) {
    return new BrainActivityGroup<T>(FPActivities.HUNT.get())
        .priority(10)
        .behaviours((Behavior<? super T>[]) behaviours);
  }

  @SafeVarargs
  @SuppressWarnings({"unchecked", "rawtypes"})
  static <T extends BirdEntity & BirdBrain<T>> BrainActivityGroup<T> idle(
      Behavior<?>... behaviours) {
    return new BrainActivityGroup<T>(Activity.IDLE)
        .priority(10)
        .behaviours((Behavior<? super T>[]) behaviours);
  }

  @SafeVarargs
  @SuppressWarnings({"unchecked", "rawtypes"})
  static <T extends BirdEntity & BirdBrain<T>> BrainActivityGroup<T> pickUp(
      Behavior<?>... behaviours) {
    return new BrainActivityGroup<T>(FPActivities.PICK_UP.get())
        .priority(10)
        .behaviours((Behavior<? super T>[]) behaviours)
        .requireAndWipeMemoriesOnUse(FPMemoryTypes.SEES_FOOD.get());
  }

  @SafeVarargs
  @SuppressWarnings({"unchecked", "rawtypes"})
  static <T extends BirdEntity & BirdBrain<T>> BrainActivityGroup<T> rest(
      Behavior<?>... behaviours) {
    return new BrainActivityGroup<T>(Activity.REST)
        .priority(10)
        .behaviours((Behavior<? super T>[]) behaviours);
  }

  @SafeVarargs
  @SuppressWarnings({"unchecked", "rawtypes"})
  static <T extends BirdEntity & BirdBrain<T>> BrainActivityGroup<T> soar(
      Behavior<?>... behaviours) {
    return new BrainActivityGroup<T>(FPActivities.SOAR.get())
        .priority(10)
        .behaviours((Behavior<? super T>[]) behaviours);
  }

  @Override
  default BrainActivityGroup<? extends E> getCoreTasks() {
    return this.coreActivity();
  }

  @Override
  default BrainActivityGroup<? extends E> getIdleTasks() {
    return this.idleActivity();
  }

  @Override
  default BrainActivityGroup<? extends E> getFightTasks() {
    return this.fightActivity();
  }

  @Override
  default Map<Activity, BrainActivityGroup<? extends E>> getAdditionalTasks() {
    ActivityListBuilder<E> builder = new ActivityListBuilder<>();
    this.addActivities(builder);
    return builder.build();
  }

  @Override
  default List<Activity> getActivityPriorities() {
    return ObjectArrayList.of(
        FPActivities.DELIVER.get(),
        Activity.AVOID,
        Activity.FIGHT,
        FPActivities.FOLLOW.get(),
        FPActivities.PICK_UP.get(),
        FPActivities.HUNT.get(),
        FPActivities.FORAGE.get(),
        FPActivities.SOAR.get(),
        Activity.IDLE,
        Activity.REST);
  }

  @Override
  default Set<Activity> getScheduleIgnoringActivities() {
    return ObjectArraySet.of(
        FPActivities.DELIVER.get(),
        Activity.AVOID,
        Activity.FIGHT,
        FPActivities.FOLLOW.get(),
        FPActivities.PICK_UP.get());
  }

  @Override
  default Activity getDefaultActivity() {
    return Activity.REST;
  }
}