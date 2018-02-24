package com.github.games647.craftapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

public class InstantAdapterTest {

    private Gson gson;

    @Before
    public void setUp() throws Exception {
        gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
    }

    @Test
    public void testReading() throws Exception {
        Instant time = gson.fromJson("1519487280972", Instant.class);
        assertThat(time, is(Instant.ofEpochMilli(1519487280972L)));
    }

    @Test
    public void testWriting() throws Exception {
        String json = gson.toJson(Instant.ofEpochMilli(1519487280972L));
        assertThat(json, is("1519487280972"));
    }
}
