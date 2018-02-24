package com.github.games647.craftapi;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;

/**
 * Converts the milliseconds into Java 8's Instant objects.
 */
public class InstantAdapter extends TypeAdapter<Instant> {

    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        TypeAdapters.LONG.write(out, value.toEpochMilli());
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        Number epochMilli = TypeAdapters.LONG.read(in);
        return Instant.ofEpochMilli(epochMilli.longValue());
    }
}
