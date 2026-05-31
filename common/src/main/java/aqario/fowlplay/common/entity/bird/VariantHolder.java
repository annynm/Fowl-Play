package aqario.fowlplay.common.entity.bird;

import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;

public interface VariantHolder<T> {
  String VARIANT_KEY = "variant";

  Registry<T> variantRegistry();

  ResourceKey<Registry<T>> variantRegistryKey();

  ResourceKey<T> defaultVariant();

  Holder<T> getVariant();

  void setVariant(Holder<T> variant);

  default void defineVariant(
      SynchedEntityData.Builder builder, EntityDataAccessor<Holder<T>> accessor) {
    builder.define(accessor, this.variantRegistry().getOrThrow(this.defaultVariant()));
  }

  default void writeVariant(CompoundTag nbt) {
    // Fixed: ResourceKey.location() removed in 1.21.2+. Use key().location() instead.
    nbt.putString(VARIANT_KEY, this.getVariantKey().identifier().toString());
  }

  default void readVariant(CompoundTag nbt) {
    Optional.ofNullable(Identifier.tryParse(nbt.getString(VARIANT_KEY).orElse("")))
        .map(variant -> ResourceKey.create(this.variantRegistryKey(), variant))
        .flatMap(this::toHolder)
        .ifPresent(this::setVariant);
  }

  default void withRandomVariant(RandomSource random, Consumer<Holder.Reference<T>> consumer) {
    this.getRandomVariant(random).ifPresent(consumer);
  }

  default Optional<Holder.Reference<T>> getRandomVariant(RandomSource random) {
    return this.variantRegistry().getRandom(random);
  }

  default Optional<Holder.Reference<T>> toHolder(ResourceKey<T> variant) {
    return this.variantRegistry().get(variant);
  }

  default ResourceKey<T> getVariantKey() {
    return this.getVariant().unwrapKey().orElse(this.defaultVariant());
  }
}