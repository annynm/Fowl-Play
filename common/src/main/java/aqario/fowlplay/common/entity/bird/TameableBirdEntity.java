package aqario.fowlplay.common.entity.bird;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class TameableBirdEntity extends BirdEntity {
  private static final EntityDataAccessor<String> OWNER_UUID =
      SynchedEntityData.defineId(TameableBirdEntity.class, EntityDataSerializers.STRING);
  private static final EntityDataAccessor<Boolean> SITTING =
      SynchedEntityData.defineId(TameableBirdEntity.class, EntityDataSerializers.BOOLEAN);

  protected TameableBirdEntity(EntityType<? extends BirdEntity> entityType, Level world) {
    super(entityType, world);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(OWNER_UUID, "");
    builder.define(SITTING, false);
  }

  @Override
  public void saveCustomDataToTag(
      CompoundTag nbt, net.minecraft.core.RegistryAccess registryAccess) {
    UUID owner = this.getOwnerUUID();
    if (owner != null) {
      nbt.putString("owner", owner.toString());
    }
    nbt.putBoolean("sitting", this.isSitting());
  }

  @Override
  public void loadCustomDataFromTag(
      CompoundTag nbt, net.minecraft.core.RegistryAccess registryAccess) {
    if (nbt.contains("owner")) {
      try {
        this.setOwnerUUID(UUID.fromString(nbt.getString("owner").orElse("")));
      } catch (IllegalArgumentException e) {
        this.setOwnerUUID(null);
      }
    }
    this.setSitting(nbt.getBoolean("sitting").orElse(false));
  }

  public UUID getOwnerUUID() {
    String uuidStr = this.entityData.get(OWNER_UUID);
    if (uuidStr.isEmpty()) return null;
    try {
      return UUID.fromString(uuidStr);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  public void setOwnerUUID(UUID owner) {
    this.entityData.set(OWNER_UUID, owner != null ? owner.toString() : "");
  }

  public boolean isSitting() {
    return this.entityData.get(SITTING);
  }

  public void setSitting(boolean sitting) {
    this.entityData.set(SITTING, sitting);
  }

  public boolean isTamed() {
    return this.getOwnerUUID() != null;
  }

  public boolean isOwner(Player player) {
    return player != null
        && this.getOwnerUUID() != null
        && this.getOwnerUUID().equals(player.getUUID());
  }

  public void setOwner(Player player) {
    this.setOwnerUUID(player != null ? player.getUUID() : null);
  }
}