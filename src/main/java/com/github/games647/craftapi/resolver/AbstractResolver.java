package com.github.games647.craftapi.resolver;

import com.github.games647.craftapi.InstantAdapter;
import com.github.games647.craftapi.NamePredicate;
import com.github.games647.craftapi.UUIDAdapter;
import com.github.games647.craftapi.cache.Cache;
import com.github.games647.craftapi.cache.MemoryCache;
import com.github.games647.craftapi.model.skin.SkinModel;
import com.github.games647.craftapi.model.skin.SkinProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

import javax.net.ssl.HttpsURLConnection;

public abstract class AbstractResolver {

    private static final int TIMEOUT = 3_000;
    private static final String USER_AGENT = "CraftAPIClient";

    protected final Predicate<String> validNamePredicate = new NamePredicate();
    protected final BalancedSSLFactory sslFactory = new BalancedSSLFactory();

    protected Cache cache = new MemoryCache();

    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new UUIDAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .create();

    public SkinModel decodeSkin(SkinProperty property) {
        byte[] data = Base64.getDecoder().decode(property.getValue());
        String json = new String(data, StandardCharsets.UTF_8);
        return gson.fromJson(json, SkinModel.class);
    }

    public SkinProperty encodeSkin(SkinModel skinModel, byte[] signature) {
        String json = gson.toJson(skinModel);

        String encodedValue = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        String encodedSignature = Base64.getEncoder().encodeToString(signature);
        return new SkinProperty(encodedValue, encodedSignature);
    }

    protected <T> T readJson(InputStream inputStream, Class<T> classOfT) throws IOException {
        return readJson(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)), classOfT);
    }

    protected <T> T readJson(Reader reader, Class<T> classOfT) throws IOException {
        try {
            return gson.fromJson(reader, classOfT);
        } finally {
            reader.close();
        }
    }

    protected HttpURLConnection getConnection(String url) throws IOException {
        return getConnection(url, Proxy.NO_PROXY);
    }

    protected HttpURLConnection getConnection(String url, Proxy proxy) throws IOException {
        HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection(proxy);

        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(2 * TIMEOUT);

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("User-Agent", USER_AGENT);

        conn.setSSLSocketFactory(sslFactory);
        return conn;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public void setOutgoingAddresses(Collection<InetAddress> addresses) {
        sslFactory.setOutgoingAddresses(addresses);
    }
}
