package com.github.games647.craftapi.model.skin;

import java.util.Objects;
import java.util.Optional;

/**
 * Decoded data of one of the texture types.
 */
public class Texture {

    private static final String URL_PREFIX = "http://textures.minecraft.net/texture/";

    private final String url;
    private final Metadata metadata;

    public Texture(String shortUrl, boolean slimModel) {
        this.url = URL_PREFIX + shortUrl;

        if (slimModel) {
            metadata = new Metadata();
        } else {
            metadata = null;
        }
    }

    public Texture(String shortUrl) {
        this(shortUrl, false);
    }

    /**
     * @return can be null if not slim or this is not a skin
     */
    public Optional<Metadata> getMetadata() {
        return Optional.ofNullable(metadata);
    }

    /**
     * @return complete url where the client can find the skin
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return url without the Mojang prefix.
     *          Example: http://textures.minecraft.net/texture/SKIN_DATA to SKIN_DATA
     */
    public String getShortUrl() {
        return url.replace(URL_PREFIX, "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Texture)) return false;
        Texture that = (Texture) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, metadata);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' +
                "url='" + url + '\'' +
                ", metadata=" + metadata +
                '}';
    }

    /**
     * Texture types of skin properties
     */
    public static enum TextureType {

        SKIN,

        CAPE,

        ELYTRA
    }
}
