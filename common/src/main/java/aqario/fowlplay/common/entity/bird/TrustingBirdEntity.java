package aqario.fowlplay.common.entity.bird;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class TrustingBirdEntity extends FlyingBirdEntity {
  private static final EntityDataAccessor<String> TRUSTER_UUID =
      SynchedEntityData.defineId(TrustingBirdEntity.class, EntityDataSerializers.STRING);

  protected TrustingBirdEntity(EntityType<? extends BirdEntity> entityType, Level world) {
    super(entityType, world);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(TRUSTER_UUID, "");
  }

  public void addAdditionalSaveData(CompoundTag nbt) {
    UUID truster = this.getTrusterUUID();
    if (truster != null) {
      nbt.putString("truster", truster.toString());
    }
  }

  public void readAdditionalSaveData(CompoundTag nbt) {
    if (nbt.contains("truster")) {
      try {
        this.setTrusterUUID(UUID.fromString(nbt.getString("truster").orElse("")));
      } catch (IllegalArgumentException e) {
        this.setTrusterUUID(null);
      }
    }
  }

  @Nullable
  public UUID getTrusterUUID() {
    String uuidStr = this.entityData.get(TRUSTER_UUID);
    if (uuidStr.isEmpty()) return null;
    try {
      return UUID.fromString(uuidStr);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  public void setTrusterUUID(@Nullable UUID truster) {
    this.entityData.set(TRUSTER_UUID, truster != null ? truster.toString() : "");
  }

  public boolean isTrustingPlayer(java.util.UUID playerUUID) {
    return this.getTrusterUUID() != null && this.getTrusterUUID().equals(playerUUID);
  }

  public boolean trusts(Player player) {
    return this.isTrustingPlayer(player.getUUID());
  }

  public void stopTrusting(Player player) {
    if (this.isTrustingPlayer(player.getUUID())) {
      this.setTrusterUUID(null);
    }
  }

  // Added to fix PigeonSpecificSensor and DeliverBundle errors
  public UUID getOwner() {
    return this.getTrusterUUID();
  }

  // Added to fix PigeonEntity behavior errors
  public boolean isSitting() {
    return false;
  }
}