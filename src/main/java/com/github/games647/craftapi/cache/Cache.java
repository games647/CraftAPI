package com.github.games647.craftapi.cache;

import com.github.games647.craftapi.model.Profile;
import com.github.games647.craftapi.model.skin.SkinProperty;
import com.google.common.collect.ImmutableSet;

import java.util.Optional;
import java.util.UUID;

/**
 * Cache for the skins and profiles.
 */
public interface Cache {

    void add(Profile profile);

    void add(UUID uniqueId, SkinProperty property);

    void remove(Profile profile);

    void removeSkin(UUID uniqueId);

    void clear();

    Optional<Profile> getByName(String playerName);

    Optional<Profile> getById(UUID uniqueId);

    Optional<SkinProperty> getSkin(UUID uniqueId);

    ImmutableSet<Profile> getCachedProfiles();

    ImmutableSet<SkinProperty> getCachedSkins();

}
