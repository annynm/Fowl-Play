package aqario.fowlplay.common.worldgen;

import aqario.fowlplay.common.config.FowlPlayConfig;
import aqario.fowlplay.common.entity.bird.passerine.SparrowEntity;
import aqario.fowlplay.core.FPEntityTypes;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gamerule.GameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.phys.AABB;

public class SparrowSpawner implements CustomSpawner {
  private static final int SPAWN_COOLDOWN = 2400;
  private static final int MAX_SPARROWS = 12;
  private int ticksUntilNextSpawn;

  @Override
  public void tick(ServerLevel world) {
    if (!world.getGameRules().get(GameRules.SPAWN_MOBS)
        || FowlPlayConfig.getInstance().sparrowSpawnWeight <= 0) {
      return;
    }
    this.ticksUntilNextSpawn--;
    if (this.ticksUntilNextSpawn > 0) {
      return;
    }
    this.ticksUntilNextSpawn = SPAWN_COOLDOWN;
    Player player = world.getRandomPlayer();
    if (player == null) {
      return;
    }
    RandomSource random = world.random;
    int x = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
    int z = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
    BlockPos pos = player.blockPosition().offset(x, 0, z);
    if (!world.hasChunksAt(pos.getX() - 10, pos.getZ() - 10, pos.getX() + 10, pos.getZ() + 10)) {
      return;
    }
    if (world.isCloseToVillage(pos, 2)) {
      this.spawnNearPoi(world, pos);
    }
  }

  private void spawnNearPoi(ServerLevel world, BlockPos pos) {
    if (world
            .getPoiManager()
            .getCountInRange(
                holder -> holder.is(PoiTypes.HOME), pos, 48, PoiManager.Occupancy.IS_OCCUPIED)
        > 4L) {
      List<SparrowEntity> nearbySparrows =
          world.getEntitiesOfClass(SparrowEntity.class, new AABB(pos).inflate(48.0, 8.0, 48.0));
      if (nearbySparrows.size() < MAX_SPARROWS
          && SpawnPredicates.canSpawnPasserines(
              FPEntityTypes.SPARROW.get(),
              world,
              EntitySpawnReason.NATURAL,
              pos,
              world.getRandom())) {
        this.spawn(pos, world);
      }
    }
  }

  private void spawn(BlockPos pos, ServerLevel world) {
    SparrowEntity sparrow =
        FPEntityTypes.SPARROW
            .get()
            .create(world, net.minecraft.world.entity.EntitySpawnReason.NATURAL);
    if (sparrow == null) {
      return;
    }
    sparrow.finalizeSpawn(
        world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.NATURAL, null);
    sparrow.setPos(pos.getX(), pos.getY(), pos.getZ());
    sparrow.setYRot(0.0F);
    sparrow.setXRot(0.0F);
    world.addFreshEntityWithPassengers(sparrow);
  }
}