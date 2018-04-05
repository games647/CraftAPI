package com.github.games647.craftapi.cache;

import com.github.games647.craftapi.model.Profile;
import com.github.games647.craftapi.model.skin.Property;
import com.github.games647.craftapi.model.skin.PropertyTest;

import java.time.Duration;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

public class MemoryCacheTest {

    private Cache cache;

    @Before
    public void setUp() throws Exception {
        cache = new MemoryCache(Duration.ofSeconds(1), 1, Duration.ofSeconds(1), 1);
    }

    @Test
    public void maxSizeProfile() throws Exception {
        assertThat(cache.getCachedProfiles().size(), is(0));

        cache.add(new Profile(UUID.randomUUID(), "1"));
        cache.add(new Profile(UUID.randomUUID(), "2"));

        assertThat(cache.getCachedProfiles().size(), is(1));
    }

    @Test
    public void maxSizeSkin() throws Exception {
        assertThat(cache.getCachedSkins().size(), is(0));

        Property property1 = new Property(PropertyTest.STEVE_VALUE, PropertyTest.STEVE_SIGNATURE);
        Property property2 = new Property(PropertyTest.SLIM_VALUE, PropertyTest.SLIM_SIGNATURE);
        cache.addSkin(UUID.randomUUID(), property1);
        cache.addSkin(UUID.randomUUID(), property2);

        assertThat(cache.getCachedSkins().size(), is(1));
    }

    @Test
    public void addSkin() throws Exception {
        UUID profileId = UUID.randomUUID();

        assertThat(cache.getSkin(profileId).isPresent(), is(false));

        Property property = new Property(PropertyTest.STEVE_VALUE, PropertyTest.STEVE_SIGNATURE);
        cache.addSkin(profileId, property);

        assertThat(cache.getSkin(profileId).get(), is(property));
    }

    @Test
    public void addProfile() throws Exception {
        Profile profile = new Profile(UUID.randomUUID(), "abc");

        assertThat(cache.getById(profile.getId()).isPresent(), is(false));
        cache.add(profile);

        assertThat(cache.getById(profile.getId()).get(), is(profile));
    }

    @Test
    public void profileCaseInsensitive() throws Exception {
        Profile profile = new Profile(UUID.randomUUID(), "123ABC_abc");
        cache.add(profile);

        //all lower case + all upper case + mixed
        assertThat(cache.getByName("123abc_abc").orElse(null), is(profile));
        assertThat(cache.getByName("123ABC_ABC").orElse(null), is(profile));
        assertThat(cache.getByName("123abc_ABC").orElse(null), is(profile));
    }

    @Test
    public void clear() throws Exception {
        Profile profile = new Profile(UUID.randomUUID(), "123ABC_abc");
        cache.add(profile);

        Property property = new Property(PropertyTest.STEVE_VALUE, PropertyTest.STEVE_SIGNATURE);
        cache.addSkin(profile.getId(), property);

        cache.clear();
        assertThat(cache.getByName(profile.getName()).isPresent(), is(false));
        assertThat(cache.getById(profile.getId()).isPresent(), is(false));
        assertThat(cache.getSkin(profile.getId()).isPresent(), is(false));
    }

    @Test
    public void removeProfile() throws Exception {
        Profile testProfile = new Profile(UUID.randomUUID(), "123ABC_abc");
        cache.add(testProfile);

        cache.remove(new Profile(UUID.randomUUID(), "123"));
        assertThat(cache.getById(testProfile.getId()).isPresent(), is(true));
        assertThat(cache.getByName(testProfile.getName()).isPresent(), is(true));

        cache.remove(testProfile);
        assertThat(cache.getById(testProfile.getId()).isPresent(), is(false));
        assertThat(cache.getByName(testProfile.getName()).isPresent(), is(false));
    }

    @Test
    public void removeSkin() throws Exception {
        UUID profileId = UUID.randomUUID();
        Property property = new Property(PropertyTest.STEVE_VALUE, PropertyTest.STEVE_SIGNATURE);
        cache.addSkin(profileId, property);

        cache.removeSkin(UUID.randomUUID());
        assertThat(cache.getSkin(profileId).isPresent(), is(true));

        cache.removeSkin(profileId);
        assertThat(cache.getSkin(profileId).isPresent(), is(false));
    }
}
