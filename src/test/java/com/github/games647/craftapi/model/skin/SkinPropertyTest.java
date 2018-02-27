package com.github.games647.craftapi.model.skin;

import com.google.common.io.ByteStreams;

import java.io.BufferedInputStream;
import java.io.IOException;
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

    public static final String SLIM_VALUE = "eyJ0aW1lc3RhbXAiOjE1MTk1NTI3OTgyMzIsInByb2ZpbGVJZCI6Ijc4YzNhNGU4MzdlNDQ4" +
            "MTg5ZGY4ZjljZTYxYzVlZmNjIiwicHJvZmlsZU5hbWUiOiJGMGdneU1vbnN0M3IiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4" +
            "dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUyODQ3YmEzZWI2NTZlN2Fj" +
            "NjlmMmFmOWNlYzU4ZDRlYzJmNWEyZWE3ZTE4OTY4Yzk3OTA3ZTg3ZWZhOWNjNCIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19";
    public static final String SLIM_SIGNATURE = "cTReAPaFI7iikV5L1w8HQUDlOfiWrne0UZ3Chn+HAIIzKD7KH83mgISzmFQB0k3lAvSp" +
            "Yx1DlGFlrVYKqLWsPOr0Az9HAmYJ+Jwq656YRiwdYqmAKBS63uoV0ZijtCZJCN8MexOmz+ScmFd75/WwbYz3g60hk35kHpFlGPxVcXJM" +
            "8+8bmVl3fUS4oTUYEFbFvyOufBFVekEgmC2xL7kSrkpAO6BT+F5bmJig41/JC2X5wb3eKO/DowwpYg/EKc162PI0i/9nKXBhTm6l7zhd" +
            "2tx/N8vQjg3ydQO6H7UG/Q0LIevCo88kQle2s40HWYpsUbx6GLLwG/3lCUvhPLyCOxLfNjgaEhjpxGIAVUKylluwxbosuNDXUsGaMlR6" +
            "zY6JqN1gl1syh4Alem3U0hjuFDD7QWMUnuG/utefmygd/DO8zyXUqFIFmvMFroblIeDW/cmKQTDDWdHdGZ5UG6xKrkuv1zDUa6uq/RfY" +
            "eX5D8Vt+VQf53MZuZMzW4E4sSa3vzEIqyzCHo7zgxQT8pQElpkw/zuIZG6E3J9I7xJ/l9TIvnoEb8TxlWPtLjaiC6v7xU8bjRQzhHVhS" +
            "qUP5Q+CkSJ/yFZHxF6ySqbn4Fyga2fXSW+6NGWBWKotfrnpRCSNnN2an/BGlb9dzSyRJoS0ObYJiQepFKWLUNdPfbZXiQQA=";

    public static final String STEVE_VALUE = "eyJ0aW1lc3RhbXAiOjE1MTcwNTI0MzU2NjgsInByb2ZpbGVJZCI6IjBhYWEyYzEzOTIyYT" +
            "QxMWJiNjU1OWI4YzA4NDA0Njk1IiwicHJvZmlsZU5hbWUiOiJnYW1lczY0NyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dX" +
            "JlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJlNmEzZjhjYWVhNzkxM2FiND" +
            "gyMzdiZWVhNmQ2YTFhNmY3NjkzNmUzYjcxYWY0YzdhMDhiYjYxYzc4NzAifX19";
    public static final String STEVE_SIGNATURE = "JTfVSrd+glDHO2glPmsKdeTOXPgHw6mBnvFbZA75TBq7sQoIMoWjxOlvH7vPkPyTkM" +
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
    public void testVerify() throws Exception {
        SkinProperty property = new SkinProperty(STEVE_VALUE, STEVE_SIGNATURE);
        assertThat(property.isValid(publicKey), is(true));
    }

    @Test
    public void testVerifySlim() throws Exception {
        SkinProperty property = new SkinProperty(SLIM_VALUE, SLIM_SIGNATURE);
        assertThat(property.isValid(publicKey), is(true));
    }

    @Test
    public void testVerifyCustomPublic() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEY_ALG);
        keyGen.initialize(KEY_SIZE);
        publicKey = keyGen.generateKeyPair().getPublic();

        SkinProperty property = new SkinProperty(STEVE_VALUE, STEVE_SIGNATURE);
        assertThat(property.isValid(publicKey), is(false));
    }

    @Test
    public void testVerifyInvalid() throws Exception {
        SkinProperty property = new SkinProperty(STEVE_VALUE, SLIM_SIGNATURE);
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
                BufferedInputStream in = new BufferedInputStream(url.openStream())
        ) {
            return ByteStreams.toByteArray(in);
        }
    }
}
