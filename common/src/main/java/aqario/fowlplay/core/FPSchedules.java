package aqario.fowlplay.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.schedule.SmartBrainSchedule;

public class FPSchedules {
  private static final Map<String, FowlPlaySchedule> SCHEDULES = new HashMap<>();

  public static final Supplier<FowlPlaySchedule> FORAGER =
      register(
          "forager",
          new FowlPlaySchedule()
              .activityAt(0, Activity.IDLE)
              .activityAt(1000, FPActivities.FORAGE.get())
              .activityAt(6000, Activity.IDLE)
              .activityAt(8000, FPActivities.FORAGE.get())
              .activityAt(11000, Activity.IDLE)
              .activityAt(13000, Activity.REST)
              .activityAt(23000, Activity.IDLE));

  public static final Supplier<FowlPlaySchedule> PENGUIN =
      register(
          "penguin",
          new FowlPlaySchedule()
              .activityAt(0, Activity.IDLE)
              .activityAt(4000, FPActivities.HUNT.get())
              .activityAt(8000, Activity.IDLE)
              .activityAt(13000, Activity.REST)
              .activityAt(23000, Activity.IDLE));

  public static final Supplier<FowlPlaySchedule> RAPTOR =
      register(
          "raptor",
          new FowlPlaySchedule()
              .activityAt(0, Activity.IDLE)
              .activityAt(1000, FPActivities.HUNT.get())
              .activityAt(6000, Activity.IDLE)
              .activityAt(8000, FPActivities.HUNT.get())
              .activityAt(11000, Activity.IDLE)
              .activityAt(13000, Activity.REST)
              .activityAt(23000, Activity.IDLE));

  public static final Supplier<FowlPlaySchedule> SEABIRD =
      register(
          "seabird",
          new FowlPlaySchedule()
              .activityAt(0, Activity.IDLE)
              .activityAt(1000, FPActivities.SOAR.get())
              .activityAt(6000, FPActivities.FORAGE.get())
              .activityAt(8000, FPActivities.SOAR.get())
              .activityAt(11000, Activity.IDLE)
              .activityAt(13000, Activity.REST)
              .activityAt(23000, Activity.IDLE));

  public static final Supplier<FowlPlaySchedule> WATERFOWL =
      register(
          "waterfowl",
          new FowlPlaySchedule()
              .activityAt(0, Activity.IDLE)
              .activityAt(1000, FPActivities.FORAGE.get())
              .activityAt(6000, Activity.IDLE)
              .activityAt(8000, FPActivities.FORAGE.get())
              .activityAt(11000, Activity.IDLE)
              .activityAt(13000, Activity.REST)
              .activityAt(23000, Activity.IDLE));

  public static final Supplier<FowlPlaySchedule> DOMESTIC =
      register(
          "domestic",
          new FowlPlaySchedule()
              .activityAt(0, Activity.IDLE)
              .activityAt(12500, Activity.REST)
              .activityAt(23000, Activity.IDLE));

  private static Supplier<FowlPlaySchedule> register(String id, FowlPlaySchedule schedule) {
    SCHEDULES.put(id, schedule);
    return () -> schedule;
  }

  public static FowlPlaySchedule getSchedule(String id) {
    return SCHEDULES.get(id);
  }

  public static class FowlPlaySchedule extends SmartBrainSchedule {
    private final java.util.NavigableMap<Integer, Activity> activityMap = new java.util.TreeMap<>();

    public FowlPlaySchedule activityAt(int tick, Activity activity) {
      activityMap.put(tick, activity);
      return this;
    }

    public Activity getActivity(long gameTime) {
      var entry = activityMap.floorEntry((int) (gameTime % 24000));
      if (entry != null) {
        return entry.getValue();
      }
      return Activity.IDLE;
    }

    public long getNextTransition(long gameTime) {
      var entry = activityMap.higherEntry((int) (gameTime % 24000));
      if (entry != null) {
        return entry.getKey();
      }
      return activityMap.firstKey() + 24000L;
    }
  }
}