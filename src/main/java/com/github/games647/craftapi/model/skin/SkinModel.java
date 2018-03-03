package com.github.games647.craftapi.model.skin;

import com.github.games647.craftapi.model.skin.Texture.TextureType;
import com.google.common.base.Strings;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SkinModel {

    //the order of these fields are relevant
    private final Instant timestamp;
    private final UUID profileId;
    private final String profileName;

    private final boolean signatureRequired = true;
    private final Map<Texture.TextureType, Texture> textures = new EnumMap<>(Texture.TextureType.class);

    private byte[] signature;

    /**
     * Creates a new decoded skin model
     *
     * @param timestamp when was the skin fetched from Mojang
     * @param uuid premium UUID of the Owner
     * @param name case-sensitive name of the owner
     * @param slimModel true if slim model otherwise false for steve
     * @param skinURL short skin url or empty
     * @param capeURL short cape url or empty
     */
    public SkinModel(Instant timestamp, UUID uuid, String name, boolean slimModel, String skinURL, String capeURL) {
        this.timestamp = timestamp;
        this.profileId = uuid;
        this.profileName = name;

        if (!Strings.isNullOrEmpty(skinURL)) {
            textures.put(TextureType.SKIN, new Texture(skinURL, slimModel));
        }

        if (!Strings.isNullOrEmpty(capeURL)) {
            textures.put(TextureType.CAPE, new Texture(capeURL));
        }
    }

    /**
     * @return time at the skin was fetched from Mojang
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * @return user profile of the skin owner
     */
    public UUID getOwnerId() {
        return profileId;
    }

    /**
     * @return user name of the skin owner
     */
    public String getOwnerName() {
        return profileName;
    }

    /**
     * @return skin, cape, elytra data or Optional.empty if the player doesn't have this attribute
     */
    public Optional<Texture> getTexture(Texture.TextureType type) {
        return Optional.ofNullable(textures.get(type));
    }

    /**
     * @return the raw signature to verify that the skin is from Mojang
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * @param signature raw signature bytes
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other instanceof SkinModel) {
            SkinModel that = (SkinModel) other;
            return timestamp == that.timestamp &&
                    Objects.equals(profileId, that.profileId) &&
                    Objects.equals(profileName, that.profileName) &&
                    Objects.equals(textures, that.textures);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, profileId, profileName, textures);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' +
                "timestamp=" + timestamp +
                ", profileId=" + profileId +
                ", profileName='" + profileName + '\'' +
                ", textures=" + textures +
                '}';
    }
}
