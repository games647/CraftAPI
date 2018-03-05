package com.github.games647.craftapi.cache;

import com.google.common.cache.CacheLoader;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

public class SafeCacheBuilderTest {

    @Test
    public void testReflection() {
        //check if the reflection access still works
        ConcurrentMap<String, String> cache = SafeCacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .maximumSize(42)
                .expireAfterAccess(42, TimeUnit.MINUTES)
                .build(CacheLoader.from(() -> {
                    throw new UnsupportedOperationException();
                }));

        assertThat(cache.getOrDefault("123", "default"), is("default"));
    }
}
