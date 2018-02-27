package com.github.games647.craftapi.cache;

import com.github.games647.craftapi.model.Profile;
import com.github.games647.craftapi.model.skin.SkinProperty;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableSet;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class MemoryCache implements Cache {

    private static final int CACHE_SIZE = 16_384;
    private static final int CACHE_TIME = 60;

    private static final int SKIN_CACHE_TIME = 5;

    private final ConcurrentMap<UUID, Profile> uuidToProfileCache = buildCache(CACHE_TIME, CACHE_SIZE);
    private final ConcurrentMap<String, Profile> nameToProfileCache = buildCache(CACHE_TIME, CACHE_SIZE);

    private final ConcurrentMap<UUID, SkinProperty> skinCache = buildCache(SKIN_CACHE_TIME, 0);

    @Override
    public void add(Profile profile) {
        uuidToProfileCache.put(profile.getId(), profile);
        nameToProfileCache.put(profile.getName(), profile);
    }

    @Override
    public void add(UUID uniqueId, SkinProperty property) {
        skinCache.put(uniqueId, property);
    }

    @Override
    public void remove(Profile profile) {
        uuidToProfileCache.remove(profile.getId(), profile);
        nameToProfileCache.remove(profile.getName(), profile);
    }

    @Override
    public void removeSkin(UUID uniqueId) {
        skinCache.remove(uniqueId);
    }

    @Override
    public void clear() {
        uuidToProfileCache.clear();
        nameToProfileCache.clear();
        skinCache.clear();
    }

    @Override
    public Optional<Profile> getByName(String playerName) {
        return Optional.ofNullable(nameToProfileCache.get(playerName));
    }

    @Override
    public Optional<Profile> getById(UUID uniqueId) {
        return Optional.ofNullable(uuidToProfileCache.get(uniqueId));
    }

    @Override
    public Optional<SkinProperty> getSkin(UUID uniqueId) {
        return Optional.ofNullable(skinCache.get(uniqueId));
    }

    @Override
    public ImmutableSet<Profile> getCachedProfiles() {
        return ImmutableSet.copyOf(uuidToProfileCache.values());
    }

    @Override
    public ImmutableSet<SkinProperty> getCachedSkins() {
        return ImmutableSet.copyOf(skinCache.values());
    }

    private <K, V> ConcurrentMap<K, V> buildCache(int expireAfterWrite, int maxSize) {
        CompatibleCacheBuilder<Object, Object> builder = CompatibleCacheBuilder.newBuilder();

        if (expireAfterWrite > 0) {
            builder.expireAfterWrite(expireAfterWrite, TimeUnit.MINUTES);
        }

        if (maxSize > 0) {
            builder.maximumSize(maxSize);
        }

        return builder.build(CacheLoader.from(() -> {
            throw new UnsupportedOperationException();
        }));
    }
}
