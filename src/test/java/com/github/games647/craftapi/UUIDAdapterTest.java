package com.github.games647.craftapi;

import java.util.UUID;

import org.junit.Test;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

public class UUIDAdapterTest {

    private static final String DINNERBONE_OFFLINE_ID = "4d258a81-2358-3084-8166-05b9faccad80";
    private static final String DINNERBONE_PREMIUM_ID = "61699b2e-d327-4a01-9f1e-0ea8c3f06bc6";
    private static final String DINNERBONE_MOJANG_ID = "61699b2ed3274a019f1e0ea8c3f06bc6";

    private static final String JEB_OFFLINE_ID = "68f45697-675f-33db-abc6-9adc644d11aa";
    private static final String JEB_PREMIUM_ID = "853c80ef-3c37-49fd-aa49-938b674adae6";
    private static final String JEB_MOJANG_ID = "853c80ef3c3749fdaa49938b674adae6";

    @Test
    public void testOfflineUUID() throws Exception {
        UUID boneId = UUIDAdapter.generateOfflineUUID("Dinnerbone");
        assertThat(boneId, is(UUID.fromString(DINNERBONE_OFFLINE_ID)));

        UUID jebId = UUIDAdapter.generateOfflineUUID("Jeb_");
        assertThat(jebId, is(UUID.fromString(JEB_OFFLINE_ID)));
    }

    @Test
    public void testStripping() throws Exception {
        UUID boneId = UUID.fromString(DINNERBONE_PREMIUM_ID);
        assertThat(UUIDAdapter.toMojangId(boneId), is(DINNERBONE_MOJANG_ID));

        UUID jebId = UUID.fromString(JEB_PREMIUM_ID);
        assertThat(UUIDAdapter.toMojangId(jebId), is(JEB_MOJANG_ID));
    }

    @Test
    public void testParsing() throws Exception {
        UUID boneId = UUID.fromString(DINNERBONE_PREMIUM_ID);
        assertThat(UUIDAdapter.parseId(DINNERBONE_MOJANG_ID), is(boneId));

        UUID jebId = UUID.fromString(JEB_PREMIUM_ID);
        assertThat(UUIDAdapter.parseId(JEB_MOJANG_ID), is(jebId));
    }
}
