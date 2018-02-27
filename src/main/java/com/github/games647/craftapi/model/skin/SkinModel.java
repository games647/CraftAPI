package com.github.games647.craftapi.model.skin;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SkinModel {

    //the order of these fields are relevant
    private final Instant timestamp;
    private final UUID profileId;
    private final String profileName;

    private final boolean signatureRequired = true;
    private final Map<TextureType, Texture> textures = new EnumMap<>(TextureType.class);

    public SkinModel(Instant timestamp, UUID uuid, String name, boolean slimModel, String skinURL, String capeURL) {
        this.timestamp = timestamp;
        this.profileId = uuid;
        this.profileName = name;

        if (skinURL != null && !skinURL.isEmpty()) {
            textures.put(TextureType.SKIN, new Texture(skinURL, slimModel));
        }

        if (capeURL != null && !capeURL.isEmpty()) {
            textures.put(TextureType.CAPE, new Texture(capeURL));
        }
    }

    /**
     * @return upload time
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
     * @return skin, cape, elytra data
     */
    public Texture getTexture(TextureType type) {
        return textures.get(type);
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
                ", signatureRequired=" + signatureRequired +
                ", textures=" + textures +
                '}';
    }
}
