package com.github.games647.craftapi.resolver;

import com.github.games647.craftapi.model.NameHistory;
import com.github.games647.craftapi.model.Profile;
import com.github.games647.craftapi.model.skin.SkinProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 *
 */
public interface ProfileResolver {

    /**
     *
     * @param names
     * @return
     * @throws IOException
     * @throws RateLimitException
     */
    ImmutableSet<Profile> findProfiles(String... names) throws IOException, RateLimitException;

    /**
     *
     * @param uuid
     * @return
     * @throws IOException
     */
    ImmutableList<NameHistory> findNames(UUID uuid) throws IOException;

    /**
     *
     * @param name
     * @return
     * @throws IOException
     * @throws RateLimitException
     */
    Optional<Profile> findProfile(String name) throws IOException, RateLimitException;

    /**
     *
     * @param name
     * @param time
     * @return
     * @throws IOException
     * @throws RateLimitException
     */
    Optional<Profile> findProfile(String name, Instant time) throws IOException, RateLimitException;

    /**
     *
     * @param uuid
     * @return
     * @throws IOException
     * @throws RateLimitException
     */
    Optional<SkinProperty> downloadSkin(UUID uuid) throws IOException, RateLimitException;
}
