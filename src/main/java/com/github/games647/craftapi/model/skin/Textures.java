package com.github.games647.craftapi.model.skin;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * All texture data that can be accessed from a Mojang request.a
 */
public class Textures {

    private final UUID id;
    private final String name;
    private final Property[] properties;

    public Textures(UUID id, String name, Property[] properties) {
        this.id = id;
        this.name = name;
        this.properties = properties;
    }

    /**
     * @return premium UUID of the owner
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return case-senstive player name of the owner
     */
    public String getName() {
        return name;
    }

    /**
     * @return copy of the skin properties
     */
    public Property[] getProperties() {
        return Arrays.copyOf(properties, properties.length);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other instanceof Textures) {
            Textures that = (Textures) other;
            return Objects.equals(id, that.id) &&
                    Objects.equals(name, that.name) &&
                    Arrays.equals(properties, that.properties);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name);
        result = 31 * result + Arrays.hashCode(properties);
        return result;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' +
                "id=" + id +
                ", name='" + name + '\'' +
                ", properties=" + Arrays.toString(properties) +
                '}';
    }
}
