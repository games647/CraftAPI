package com.github.games647.craftapi.resolver;

import com.github.games647.craftapi.InstantAdapter;
import com.github.games647.craftapi.NamePredicate;
import com.github.games647.craftapi.UUIDAdapter;
import com.github.games647.craftapi.cache.Cache;
import com.github.games647.craftapi.cache.MemoryCache;
import com.github.games647.craftapi.model.skin.Skin;
import com.github.games647.craftapi.model.skin.Property;
import com.github.games647.craftapi.resolver.http.RotatingSourceFactory;
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

/**
 * Base class for fetching Minecraft related data.
 */
public abstract class AbstractResolver {

    private static final int TIMEOUT = 3_000;
    private static final String USER_AGENT = "CraftAPIClient";

    protected final Predicate<String> validNamePredicate = new NamePredicate();
    protected final RotatingSourceFactory sslFactory = new RotatingSourceFactory();

    protected Cache cache = new MemoryCache();

    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new UUIDAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .create();

    /**
     * Decodes the property from a skin request.
     *
     * @param property Base64 encoded skin property
     * @return decoded model
     */
    public Skin decodeSkin(Property property) {
        byte[] data = Base64.getDecoder().decode(property.getValue());
        String json = new String(data, StandardCharsets.UTF_8);

        Skin skinModel = gson.fromJson(json, Skin.class);
        skinModel.setSignature(Base64.getDecoder().decode(property.getSignature()));
        return skinModel;
    }

    /**
     * Decodes the skin for setting it in game.
     *
     * @param skinModel decoded skin model with signature
     * @return Base64 encoded skin property
     */
    public Property encodeSkin(Skin skinModel) {
        String json = gson.toJson(skinModel);

        String encodedValue = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        String encodedSignature = Base64.getEncoder().encodeToString(skinModel.getSignature());
        return new Property(encodedValue, encodedSignature);
    }

    /**
     * Buffers and parses all data in the input stream and closes it after it.
     *
     * @param inputStream unbuffered input stream
     * @param classOfT the class that should be parsed to
     * @param <T> type of the given class
     * @return the parsed representation
     * @throws IOException if an error occurs while reading the stream
     */
    protected <T> T readJson(InputStream inputStream, Class<T> classOfT) throws IOException {
        return readJson(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)), classOfT);
    }

    /**
     * Parses all data in the input stream and closes it after it.
     *
     * @param reader data reader
     * @param classOfT the class that should be parsed to
     * @param <T> type of the given class
     * @return the parsed representation
     * @throws IOException if an error occurs while reading the stream
     */
    protected <T> T readJson(Reader reader, Class<T> classOfT) throws IOException {
        try {
            return gson.fromJson(reader, classOfT);
        } finally {
            reader.close();
        }
    }

    /**
     * Create a new HTTPConnection with default timeout and HTTP-Headers for json accept and user-agent.
     *
     * @param url the complete url
     * @return an unestablished HTTPConnection
     * @throws IOException I/O exception on opening the data channel
     */
    protected HttpURLConnection getConnection(String url) throws IOException {
        return getConnection(url, Proxy.NO_PROXY);
    }

    /**
     * Create a new HTTPConnection with default timeout and HTTP-Headers for json accept and user-agent.
     *
     * @param url
     * @param proxy HTTP or SOCKS proxy through this connection
     * @return an unestablished HTTPConnection
     * @throws IOException I/O exception on opening the data channel
     */
    protected HttpURLConnection getConnection(String url, Proxy proxy) throws IOException {
        HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection(proxy);

        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(2 * TIMEOUT);

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("User-Agent", USER_AGENT);

        conn.setSSLSocketFactory(sslFactory);
        return conn;
    }

    /**
     * @return the current cache backend.
     */
    public Cache getCache() {
        return cache;
    }

    /**
     * Sets a new Mojang cache.
     *
     * @param cache cache implementation
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * Set the outgoing addresses. The rotating order will be the same as in the given collection.
     *
     * @param addresses all outgoing IPv4 addresses that are available or empty to disable it.
     */
    public void setOutgoingAddresses(Collection<InetAddress> addresses) {
        sslFactory.setOutgoingAddresses(addresses);
    }
}
