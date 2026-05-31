package aqario.fowlplay.common.entity.ai.brain;

import aqario.fowlplay.core.FPSchedules;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.schedule.SmartBrainSchedule;
import org.jetbrains.annotations.Nullable;

/**
 * An extension of SmartBrainSchedule that optionally takes a supplier instead of the activity
 * object. Compatible with Fowl Play's custom schedule system (FPSchedules.FowlPlaySchedule).
 */
public class ExtendedSchedule extends FPSchedules.FowlPlaySchedule {
  private final SmartBrainSchedule.Type type;
  private final Int2ObjectArrayMap<Supplier<Activity>> timeline = new Int2ObjectArrayMap<>(0);
  private final ListMultimap<Integer, Consumer<LivingEntity>> callbacks =
      MultimapBuilder.hashKeys(0).arrayListValues().build();
  private boolean sortedTimeline = true;

  public ExtendedSchedule() {
    this(SmartBrainSchedule.Type.DAYTIME);
  }

  public ExtendedSchedule(SmartBrainSchedule.Type type) {
    super(); // Call parent constructor
    this.type = type;
  }

  @Override
  public ExtendedSchedule activityAt(int tick, Activity activity) {
    return this.activityAt(tick, () -> activity);
  }

  public ExtendedSchedule activityAt(int tick, Supplier<Activity> activity) {
    this.timeline.put(tick, activity);
    this.sortedTimeline = false;
    return this;
  }

  @Override
  public ExtendedSchedule doAt(int tick, Consumer<LivingEntity> callback) {
    this.callbacks.put(tick, callback);
    return this;
  }

  @Override
  public void scheduleTask(LivingEntity brainOwner, int delay, Consumer<LivingEntity> task) {
    this.callbacks.put(
        this.type.resolveDelay(brainOwner, delay), entity -> task.accept(brainOwner));
  }

  @Override
  public void clearSchedule() {
    this.callbacks.clear();
    this.timeline.clear();
  }

  @Nullable
  public Activity tick(LivingEntity brainOwner) {
    int tick = this.type.resolve(brainOwner);

    // Execute callbacks at this tick
    if (!this.callbacks.isEmpty()) {
      this.callbacks.get(tick).forEach(consumer -> consumer.accept(brainOwner));

      if (this.type == SmartBrainSchedule.Type.AGE) {
        this.callbacks.removeAll(tick);
      }
    }

    // Get activity from timeline
    if (!this.timeline.isEmpty()) {
      if (!this.sortedTimeline) {
        this.sortTimeline();
      }

      int index = -1;
      Activity activity = null;

      for (Int2ObjectMap.Entry<Supplier<Activity>> entry : this.timeline.int2ObjectEntrySet()) {
        index++;

        if (entry.getIntKey() >= tick) {
          if (entry.getIntKey() == tick) {
            activity = entry.getValue().get();
          }
          break;
        }

        activity = entry.getValue().get();
      }

      if (this.type == SmartBrainSchedule.Type.AGE && index + 1 >= this.timeline.size()) {
        this.timeline.clear();
      }

      return activity;
    }

    return null;
  }

  private void sortTimeline() {
    Int2ObjectArrayMap<Supplier<Activity>> copy = new Int2ObjectArrayMap<>(this.timeline);
    int[] keys = copy.keySet().toArray(new int[0]);
    Arrays.sort(keys);
    this.timeline.clear();

    for (int key : keys) {
      this.timeline.put(key, copy.get(key));
    }

    this.sortedTimeline = true;
  }

  // Override parent methods to use our custom timeline logic
  @Override
  public Activity getActivity(long gameTime) {
    // For compatibility with parent class, but our tick() method is the primary entry point
    return super.getActivity(gameTime);
  }

  @Override
  public long getNextTransition(long gameTime) {
    // For compatibility with parent class
    return super.getNextTransition(gameTime);
  }
}