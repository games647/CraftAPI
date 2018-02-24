package com.github.games647.craftapi.model.skin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

public class SkinPropertyTest {

    private static final String KEY_ALG = "RSA";
    private static final int KEY_SIZE = 4096;
    private static final String MOJANG_KEY_PATH = "/yggdrasil_session_pubkey.der";

    private static final String SLIM_VALUE = "eyJ0aW1lc3RhbXAiOjE1MTcwNTUyNjIxODgsInByb2ZpbGVJZCI6Ijc4YzNhNGU4MzdlNDQ" +
            "4MTg5ZGY4ZjljZTYxYzVlZmNjIiwicHJvZmlsZU5hbWUiOiJSYXNob21vbl8iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHV" +
            "yZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE3MzU2N2VhNzJhZDRhMjJiZjc" +
            "wYmNiYTVmZWQzYjhiOWVhMDI0NjM5MTMxYmRkODYzYzI1ZDIyZjg5IiwibWV0YWRhdGEiOnsibW9kZWwiOiJzbGltIn19fX0=";
    private static final String SLIM_SIGNATURE = "X3pND0qkG0IEPBIgeYem7OmghO0LPV3J39LKWd3HinZQeJjGcmRbkvFXBaeYV0Vl8CZ" +
            "ub8/dHQbgUtPnnWL5J0x8KLRFVp9p3uuWxbGbx8efN6SAE98uC7xbphTLxBtNNKbCTOwNDhI8WfRdf3LaKLNhdPi1qSMzQvTaV9q8eYK" +
            "wdnwJ7DPCQrcPHvlZwnxLH6iIn3nPXuMzTu7aWRKF2IwGZ72Pa3X1RWy4QHtOPuZY6DKJxQK1hkbD1YNhjWnQ/8o/OJaiTmlZM0rWrjM" +
            "YCZdYcCpeeFV+gsRHuhBG9LHz7hePJvysAo005py3ydr+3PUi3ISsVYFJ9ygJqIgbqKjov8+zVfnAZHusQMdBaoDlH05sae5gAGai5zM" +
            "Ta7UwObMfsqlHTNA+Ch9kAJQ2WmYqJZeAZgrGw8MkF23zMRKMZKqLtwCdiwJfiAgtJy148+HbtZuyi3obNcS+hn9gxn5LaGC+NUOCxXH" +
            "DbCF4xN+on1/kLgyje7TjTUQnMAs5CWRWulVnt3aOOon0mUk2xMkv6B6WwW1n0MAU2jbhhp3s/cEQFrdr1f5IMLQ/OEXo2u5PPbHzyUI" +
            "So0JhuZQLNNUG2ZuLpi+eo3DccfOM/HllaBPOuA5rHU46slTgxI4edTKsG+C2vbUSFo1+vq4TFyEkoY2G0I6aRWVpDQosAxw=";

    private static final String STEVE_VALUE = "eyJ0aW1lc3RhbXAiOjE1MTcwNTI0MzU2NjgsInByb2ZpbGVJZCI6IjBhYWEyYzEzOTIyYT" +
            "QxMWJiNjU1OWI4YzA4NDA0Njk1IiwicHJvZmlsZU5hbWUiOiJnYW1lczY0NyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dX" +
            "JlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJlNmEzZjhjYWVhNzkxM2FiND" +
            "gyMzdiZWVhNmQ2YTFhNmY3NjkzNmUzYjcxYWY0YzdhMDhiYjYxYzc4NzAifX19";
    private static final String STEVE_SIGNATURE = "JTfVSrd+glDHO2glPmsKdeTOXPgHw6mBnvFbZA75TBq7sQoIMoWjxOlvH7vPkPyTkM" +
            "sb5Vt4E6jsU73hi1FDYUaoGvzmTHzhN1scXluagx1jsye6jbAx64HK+0Iw5/8nwQUTVUP6ttxLC+2HZvIeNoYc6Dqd7HAIwcdxHFjDVb" +
            "MXAfMT33C0N1CTlvnEwbbK+Fx155Fg1nKU/PYoaSXWL9eEMwCLlpf/UTTegmDlpOwlo9zG2f8/YkhACE8gyJZOB+WJwf1+Vv3BUTuAnM" +
            "AKy7KztZDZE1119fBfVLblGykniAO63BATWTWqP/oTQFCSkmpPGMyznaAPJRt4/IfES4uxYAfXCxKWF4ZytdenAmbRo00ZVg77l6wdst" +
            "xsdGaZtYEB5nsdF6lehRWLWVYhUX5nHk2HCfkGboXjhmFgcCLzFcV+YSC//P0CN2GDBlVGUPybTxceRjg7UoA4O9mn+1bLvTD7C8/G8k" +
            "RpqLRNK9/Wm8cf2sMbNCP6gPSlGao1nIuZsg7+eRih1G1LilJwtOaFhFeH+Pu+CUMCIZPxLtjTwZopG8P0FAwCTpO0gJJrqyMT+pozGA" +
            "fJ3mbt4uzuq5Mg1XYjazqEz5Zg8n0JwdTP0ZkoiVy4VMDeQz+C31bUmPcSDLxpJYF3uKQCGlbL1UZshcnQHXEEUhwb3bqjPKA=";

    private PublicKey publicKey;

    @Before
    public void setUp() throws Exception {
        this.publicKey = loadPublicKey();
    }

    @Test
    public void testVerification() throws Exception {
        SkinProperty property = new SkinProperty(STEVE_VALUE, STEVE_SIGNATURE);
        assertThat(property.isValid(publicKey), is(true));
    }

    @Test
    public void testVerificationSlim() throws Exception {
        SkinProperty property = new SkinProperty(SLIM_VALUE, SLIM_SIGNATURE);
        assertThat(property.isValid(publicKey), is(true));
    }

    @Test
    public void testVerificationNegative() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEY_ALG);
        keyGen.initialize(KEY_SIZE);
        publicKey = keyGen.generateKeyPair().getPublic();

        SkinProperty property = new SkinProperty(STEVE_VALUE, STEVE_SIGNATURE);
        assertThat(property.isValid(publicKey), is(false));
    }

    private PublicKey loadPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        URL keyUrl = SkinPropertyTest.class.getResource(MOJANG_KEY_PATH);
        KeySpec spec = new X509EncodedKeySpec(readAllBytes(keyUrl));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALG);
        return keyFactory.generatePublic(spec);
    }

    private static byte[] readAllBytes(URL url) throws IOException {
        try (
                InputStream inputStream = url.openStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream()
        ) {
            int nRead;
            byte[] data = new byte[Short.MAX_VALUE];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            return buffer.toByteArray();
        }
    }
}
