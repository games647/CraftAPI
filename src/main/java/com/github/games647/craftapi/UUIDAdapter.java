package com.github.games647.craftapi;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Type adapter for converting UUIDs from Mojang's string representation into a Java version.
 */
public class UUIDAdapter extends TypeAdapter<UUID> {

    private static final Pattern UUID_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    public void write(JsonWriter out, UUID value) throws IOException {
        TypeAdapters.STRING.write(out, toMojangId(value));
    }

    public UUID read(JsonReader in) throws IOException {
        return parseId(TypeAdapters.STRING.read(in));
    }

    /**
     * Converts a Mojang UUID (UUID without dashes) into an Java representation of the UUID.
     * @param withoutDashes uuid without dashes
     * @return java representation with dashes
     */
    public static UUID parseId(String withoutDashes) {
        return UUID.fromString(UUID_PATTERN.matcher(withoutDashes).replaceAll("$1-$2-$3-$4-$5"));
    }

    /**
     * Converts the UUID to a String representation without dashes.
     * @param uuid UUID that should be converted
     * @return UUID without dashes
     */
    public static String toMojangId(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    /**
     * Generates the offline/cracked UUID based on the playername. This method is equal to the server side
     * implementation if the server is in offline mode.
     * @param playerName case-sensitive player name
     * @return offline/cracked UUID
     */
    public static UUID generateOfflineId(String playerName) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8));
    }
}
