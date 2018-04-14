package com.github.games647.craftapi.model.skin;

import com.github.games647.craftapi.model.skin.Texture.Type;

import java.net.URL;

import org.junit.Test;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

public class TextureTest {

    private static final String URL_PREFIX = "http://textures.minecraft.net/texture/";

    @Test
    public void testLongUrl() throws Exception {
        Texture model = new Texture(Type.SKIN,"52847ba3eb656e7ac69f2af9cec58d4ec2f5a2ea7e18968c97907e87efa9cc4");
        URL url = new URL(URL_PREFIX + "52847ba3eb656e7ac69f2af9cec58d4ec2f5a2ea7e18968c97907e87efa9cc4");
        assertThat(model.getURL(), is(url));
    }
}
