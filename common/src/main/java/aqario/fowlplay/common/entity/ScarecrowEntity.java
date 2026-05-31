package aqario.fowlplay.common.entity;

import aqario.fowlplay.core.FPItems;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class ScarecrowEntity extends LivingEntity implements ContainerListener, MenuProvider {
  private static final Predicate<Entity> RIDEABLE_MINECART_PREDICATE =
      entity -> entity instanceof net.minecraft.world.entity.vehicle.AbstractMinecart;

  private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
  private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
  private static final Rotations DEFAULT_LEFT_ARM_ROTATION = new Rotations(0.0F, 0.0F, -90.0F);
  private static final Rotations DEFAULT_RIGHT_ARM_ROTATION = new Rotations(0.0F, 0.0F, 90.0F);

  public static final EntityDataAccessor<Rotations> HEAD_ROTATION =
      SynchedEntityData.defineId(ScarecrowEntity.class, EntityDataSerializers.ROTATIONS);
  public static final EntityDataAccessor<Rotations> BODY_ROTATION =
      SynchedEntityData.defineId(ScarecrowEntity.class, EntityDataSerializers.ROTATIONS);
  public static final EntityDataAccessor<Rotations> LEFT_ARM_ROTATION =
      SynchedEntityData.defineId(ScarecrowEntity.class, EntityDataSerializers.ROTATIONS);
  public static final EntityDataAccessor<Rotations> RIGHT_ARM_ROTATION =
      SynchedEntityData.defineId(ScarecrowEntity.class, EntityDataSerializers.ROTATIONS);

  private Rotations headRotation = DEFAULT_HEAD_ROTATION;
  private Rotations bodyRotation = DEFAULT_BODY_ROTATION;
  private Rotations leftArmRotation = DEFAULT_LEFT_ARM_ROTATION;
  private Rotations rightArmRotation = DEFAULT_RIGHT_ARM_ROTATION;

  private static final String POSE_KEY = "pose";
  private static final String HEAD_ROTATION_KEY = "head";
  private static final String BODY_ROTATION_KEY = "body";
  private static final String LEFT_ARM_ROTATION_KEY = "left_arm";
  private static final String RIGHT_ARM_ROTATION_KEY = "right_arm";
  private static final String ITEMS_KEY = "items";
  private static final int INVENTORY_SIZE = 4;
  private static final int HEAD_SLOT = 0;
  private static final int CHEST_SLOT = 1;
  private static final int MAINHAND_SLOT = 2;
  private static final int OFFHAND_SLOT = 3;

  protected SimpleContainer inventory;

  public ScarecrowEntity(EntityType<? extends ScarecrowEntity> entityType, Level world) {
    super(entityType, world);
    this.inventory = new SimpleContainer(INVENTORY_SIZE);
    this.inventory.addListener(this);
  }

  public static AttributeSupplier.Builder createScarecrowAttributes() {
    return createLivingAttributes()
        .add(Attributes.STEP_HEIGHT, 0.0)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
  }

  protected float tickHeadTurn(float bodyRotation, float headRotation) {
    this.yBodyRotO = this.yRotO;
    this.yBodyRot = this.getYRot();
    return 0.0F;
  }

  public void setYBodyRot(float bodyYaw) {
    this.yBodyRotO = this.yRotO = bodyYaw;
    this.yHeadRotO = this.yHeadRot = bodyYaw;
  }

  public void setYHeadRot(float headYaw) {
    this.yBodyRotO = this.yRotO = headYaw;
    this.yHeadRotO = this.yHeadRot = headYaw;
  }

  public void containerChanged(Container sender) {}

  protected void dropEquipment(ServerLevel level) {
    super.dropEquipment(level);
    if (this.inventory != null) {
      for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
        ItemStack itemStack = this.inventory.getItem(i);
        if (!itemStack.isEmpty()) this.spawnAtLocation(level, itemStack);
      }
    }
  }

  public Iterable<ItemStack> getHandSlots() {
    return ImmutableList.of(
        this.inventory.getItem(MAINHAND_SLOT), this.inventory.getItem(OFFHAND_SLOT));
  }

  public Iterable<ItemStack> getArmorSlots() {
    return ImmutableList.of(this.inventory.getItem(HEAD_SLOT), this.inventory.getItem(CHEST_SLOT));
  }

  public ItemStack getItemBySlot(EquipmentSlot slot) {
    return switch (slot) {
      case HEAD -> this.inventory.getItem(HEAD_SLOT);
      case CHEST -> this.inventory.getItem(CHEST_SLOT);
      case MAINHAND -> this.inventory.getItem(MAINHAND_SLOT);
      case OFFHAND -> this.inventory.getItem(OFFHAND_SLOT);
      default -> ItemStack.EMPTY;
    };
  }

  public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
    switch (slot) {
      case HEAD -> this.inventory.setItem(HEAD_SLOT, stack);
      case CHEST -> this.inventory.setItem(CHEST_SLOT, stack);
      case MAINHAND -> this.inventory.setItem(MAINHAND_SLOT, stack);
      case OFFHAND -> this.inventory.setItem(OFFHAND_SLOT, stack);
    }
  }

  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(HEAD_ROTATION, DEFAULT_HEAD_ROTATION);
    builder.define(BODY_ROTATION, DEFAULT_BODY_ROTATION);
    builder.define(LEFT_ARM_ROTATION, DEFAULT_LEFT_ARM_ROTATION);
    builder.define(RIGHT_ARM_ROTATION, DEFAULT_RIGHT_ARM_ROTATION);
  }

  @Override
  protected void addAdditionalSaveData(ValueOutput output) {
    super.addAdditionalSaveData(output);
    net.minecraft.world.ContainerHelper.saveAllItems(output, this.inventory.getItems());

    CompoundTag poseNbt = new CompoundTag();
    if (!DEFAULT_HEAD_ROTATION.equals(this.headRotation))
      poseNbt.put(HEAD_ROTATION_KEY, this.rotationsToList(this.headRotation));
    if (!DEFAULT_BODY_ROTATION.equals(this.bodyRotation))
      poseNbt.put(BODY_ROTATION_KEY, this.rotationsToList(this.bodyRotation));
    if (!DEFAULT_LEFT_ARM_ROTATION.equals(this.leftArmRotation))
      poseNbt.put(LEFT_ARM_ROTATION_KEY, this.rotationsToList(this.leftArmRotation));
    if (!DEFAULT_RIGHT_ARM_ROTATION.equals(this.rightArmRotation))
      poseNbt.put(RIGHT_ARM_ROTATION_KEY, this.rotationsToList(this.rightArmRotation));
    // Correct method in 1.21.11: output.writeNbt(String key, CompoundTag tag)
    output.writeNbt(POSE_KEY, poseNbt);
  }

  @Override
  protected void readAdditionalSaveData(ValueInput input) {
    super.readAdditionalSaveData(input);
    net.minecraft.world.ContainerHelper.loadAllItems(input, this.inventory.getItems());

    // Correct methods: input.hasNbt(key) and input.readNbt(key)
    if (input.hasNbt(POSE_KEY)) {
      CompoundTag poseNbt = input.readNbt(POSE_KEY).orElse(new CompoundTag());
      this.setHeadRotation(
          poseNbt.contains(HEAD_ROTATION_KEY)
              ? this.listToRotations(poseNbt.getList(HEAD_ROTATION_KEY).orElse(new ListTag()))
              : DEFAULT_HEAD_ROTATION);
      this.setBodyRotation(
          poseNbt.contains(BODY_ROTATION_KEY)
              ? this.listToRotations(poseNbt.getList(BODY_ROTATION_KEY).orElse(new ListTag()))
              : DEFAULT_BODY_ROTATION);
      this.setLeftArmRotation(
          poseNbt.contains(LEFT_ARM_ROTATION_KEY)
              ? this.listToRotations(poseNbt.getList(LEFT_ARM_ROTATION_KEY).orElse(new ListTag()))
              : DEFAULT_LEFT_ARM_ROTATION);
      this.setRightArmRotation(
          poseNbt.contains(RIGHT_ARM_ROTATION_KEY)
              ? this.listToRotations(poseNbt.getList(RIGHT_ARM_ROTATION_KEY).orElse(new ListTag()))
              : DEFAULT_RIGHT_ARM_ROTATION);
    }
  }

  private ListTag rotationsToList(Rotations rotations) {
    ListTag list = new ListTag();
    list.add(net.minecraft.nbt.FloatTag.valueOf(rotations.x()));
    list.add(net.minecraft.nbt.FloatTag.valueOf(rotations.y()));
    list.add(net.minecraft.nbt.FloatTag.valueOf(rotations.z()));
    return list;
  }

  private Rotations listToRotations(ListTag list) {
    return new Rotations(
        list.getFloat(0).orElse(0f), list.getFloat(1).orElse(0f), list.getFloat(2).orElse(0f));
  }

  public InteractionResult interact(Player player, InteractionHand hand) {
    if (!player.level().isClientSide()
        && player.getItemInHand(hand).isEmpty()
        && !player.isSecondaryUseActive()) {
      return InteractionResult.CONSUME;
    }
    return super.interact(player, hand);
  }

  @Nullable
  public AbstractContainerMenu createMenu(
      int syncId, Inventory playerInventory, Player playerEntity) {
    return null;
  }

  public boolean canBeCollidedWith() {
    return true;
  }

  public boolean isPushable() {
    return false;
  }

  protected void doPush(Entity entity) {}

  protected void pushEntities() {
    List<Entity> list =
        this.level().getEntities(this, this.getBoundingBox(), RIDEABLE_MINECART_PREDICATE);
    for (Entity entity : list) {
      if (this.distanceToSqr(entity) <= 0.2) entity.push(this);
    }
  }

  public boolean isInvulnerableTo(DamageSource damageSource) {
    return damageSource.is(DamageTypeTags.IS_FALL);
  }

  public void thunderHit(ServerLevel world, LightningBolt lightning) {}

  public void kill() {
    this.remove(RemovalReason.KILLED);
    this.gameEvent(GameEvent.ENTITY_DIE);
  }

  protected void spawnBreakParticles() {
    if (this.level() instanceof ServerLevel world) {
      world.sendParticles(
          this.getParticle(),
          this.getX(),
          this.getY(0.6666666666666666),
          this.getZ(),
          10,
          this.getBbWidth() / 4.0F,
          this.getBbHeight() / 4.0F,
          this.getBbWidth() / 4.0F,
          0.05);
    }
  }

  private void updateHealth(DamageSource damageSource, float amount) {
    float f = this.getHealth();
    f -= amount;
    if (f <= 0.5F) {
      this.onBreak(damageSource);
      this.kill();
    } else {
      this.setHealth(f);
      this.gameEvent(GameEvent.ENTITY_DAMAGE, damageSource.getEntity());
    }
  }

  private void breakAndDropThis(DamageSource damageSource) {
    Block.popResource(this.level(), this.blockPosition(), this.getItem());
    this.onBreak(damageSource);
  }

  private void onBreak(DamageSource damageSource) {
    this.playBreakSound();
    this.dropAllDeathLoot((ServerLevel) this.level(), damageSource);
  }

  private void playBreakSound() {
    this.level()
        .playSound(
            null,
            this.getX(),
            this.getY(),
            this.getZ(),
            this.getDeathSound(),
            this.getSoundSource(),
            1.0F,
            1.0F);
  }

  public Rotations getHeadRotation() {
    return this.headRotation;
  }

  public Rotations getBodyRotation() {
    return this.bodyRotation;
  }

  public Rotations getLeftArmRotation() {
    return this.leftArmRotation;
  }

  public Rotations getRightArmRotation() {
    return this.rightArmRotation;
  }

  public void setHeadRotation(Rotations angle) {
    this.headRotation = angle;
    this.entityData.set(HEAD_ROTATION, angle);
  }

  public void setBodyRotation(Rotations angle) {
    this.bodyRotation = angle;
    this.entityData.set(BODY_ROTATION, angle);
  }

  public void setLeftArmRotation(Rotations angle) {
    this.leftArmRotation = angle;
    this.entityData.set(LEFT_ARM_ROTATION, angle);
  }

  public void setRightArmRotation(Rotations angle) {
    this.rightArmRotation = angle;
    this.entityData.set(RIGHT_ARM_ROTATION, angle);
  }

  public HumanoidArm getMainArm() {
    return HumanoidArm.RIGHT;
  }

  public ItemStack getItem() {
    return FPItems.SCARECROW.get().getDefaultInstance();
  }

  public ParticleOptions getParticle() {
    return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.HAY_BLOCK.defaultBlockState());
  }

  public SoundEvent getPlaceSound() {
    return SoundEvents.WOOD_PLACE;
  }

  public Fallsounds getFallSounds() {
    return new LivingEntity.Fallsounds(SoundEvents.WOOD_FALL, SoundEvents.WOOD_FALL);
  }

  @Nullable
  protected SoundEvent getHurtSound(DamageSource source) {
    return SoundEvents.WOOD_HIT;
  }

  @Nullable
  protected SoundEvent getDeathSound() {
    return SoundEvents.WOOD_BREAK;
  }

  public boolean isAffectedByPotions() {
    return false;
  }

  public boolean attackable() {
    return false;
  }

  @Nullable
  public ItemStack getPickResult() {
    return this.getItem();
  }
}