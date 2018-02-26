package com.github.games647.craftapi;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

public class NamePredicateTest {

    private NamePredicate predicate;

    @Before
    public void setUp() throws Exception {
        predicate = new NamePredicate();
    }

    @Test
    public void testValidSimple() throws Exception {
        assertThat(predicate.test("abcdwef"), is(true));
        assertThat(predicate.test("zezvw"), is(true));
    }

    @Test
    public void testValidUnderscore() throws Exception {
        assertThat(predicate.test("rashomon_"), is(true));
    }

    @Test
    public void testDifferentCasing() throws Exception {
        assertThat(predicate.test("FoggyMonster"), is(true));
    }

    @Test
    public void testNumbers() throws Exception {
        assertThat(predicate.test("F0ggyMonst3r"), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void testNull() throws Exception {
        assertThat(predicate.test(null), is(false));
    }

    @Test
    public void testValidLength() throws Exception {
        assertThat(predicate.test("12"), is(true));
        assertThat(predicate.test("1234567890123456"), is(true));
    }

    @Test
    public void testInvalidLength() throws Exception {
        assertThat(predicate.test(""), is(false));
        assertThat(predicate.test("12345678901234567"), is(false));
    }

    @Test
    public void testInvalidCharacters() throws Exception {
        assertThat(predicate.test("abc$"), is(false));
        assertThat(predicate.test("fdwadaä"), is(false));
    }
}
