package com.github.games647.craftapi.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Mojang game profile.
 */
public class Profile {

    private UUID id;
    private String name;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other instanceof Profile) {
            Profile that = (Profile) other;
            return Objects.equals(id, that.id);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
