package com.github.games647.craftapi;

import com.eatthepath.uuid.FastUUID;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Type adapter for converting UUIDs from Mojang's string representation into a Java version.
 *
 * It uses a faster UUID implementation for toString() (if < Java 9) outputs and dashed UUIDs parsing.
 */
public class UUIDAdapter extends TypeAdapter<UUID> {

    private static final Pattern UUID_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
    private static final Pattern DASH_PATTERN = Pattern.compile("-", Pattern.LITERAL);

    @Override
    public void write(JsonWriter out, UUID value) throws IOException {
        out.value(toMojangId(value));
    }

    @Override
    public UUID read(JsonReader in) throws IOException {
        return parseId(in.nextString());
    }

    /**
     * Converts a Mojang UUID (UUID without dashes) into an Java representation of the UUID.
     * @param withoutDashes uuid without dashes
     * @return parsed UUID
     */
    public static UUID parseId(String withoutDashes) {
        // TODO: Implement without dashes directly into FastUUID
        return parseDashedId(UUID_PATTERN.matcher(withoutDashes).replaceAll("$1-$2-$3-$4-$5"));
    }

    /**
     * Parses the string representation of UUIDs with dashes. This simulates the standard Java behavior, but with
     * performance improvements.
     *
     * @param uuidDashed uuid with dashes
     * @return parsed UUID
     */
    public static UUID parseDashedId(CharSequence uuidDashed) {
        return FastUUID.parseUUID(uuidDashed);
    }

    /**
     * Converts the UUID to a String representation without dashes.
     * @param uniqueId UUID that should be converted
     * @return UUID without dashes
     */
    public static String toMojangId(UUID uniqueId) {
        // TODO: Implement dash replace directly into FastUUID
        return DASH_PATTERN.matcher(toStringFast(uniqueId)).replaceAll("");
    }

    /**
     * Converts the UUID to a String representation with dashes.
     * @param uniqueId UUID that should be converted
     * @return UUID with dashes
     */
    public static String toStringFast(UUID uniqueId) {
        return FastUUID.toString(uniqueId);
    }

    /**
     * Generates the offline/cracked UUID based on the playername. This method is equal to the server side
     * implementation if the server is in offline mode.
     * @param playerName case-sensitive player name
     * @return offline/cracked UUID
     */
    public static UUID generateOfflineId(String playerName) {
        Objects.requireNonNull(playerName);
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8));
    }
}
