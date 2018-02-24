package com.github.games647.craftapi.model.auth;

/**
 * Service agent for Minecraft's API at Mojang. This represents the API we support.
 */
public class Agent {

    private final String name = "Minecraft";
    private final int version = 1;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' +
                "name='" + name + '\'' +
                ", version=" + version +
                '}';
    }
}
