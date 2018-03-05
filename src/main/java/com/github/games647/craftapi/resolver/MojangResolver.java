package com.github.games647.craftapi.resolver;

import com.github.games647.craftapi.UUIDAdapter;
import com.github.games647.craftapi.cache.SafeCacheBuilder;
import com.github.games647.craftapi.model.NameHistory;
import com.github.games647.craftapi.model.Profile;
import com.github.games647.craftapi.model.auth.Account;
import com.github.games647.craftapi.model.auth.AuthRequest;
import com.github.games647.craftapi.model.auth.AuthResponse;
import com.github.games647.craftapi.model.auth.Verification;
import com.github.games647.craftapi.model.skin.SkinProperty;
import com.github.games647.craftapi.model.skin.Textures;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Resolver that contacts Mojang.
 */
public class MojangResolver extends AbstractResolver implements AuthResolver, ProfileResolver {

    //profile
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/";

    //skin
    private static final String CHANGE_SKIN_URL = "https://api.mojang.com/user/profile/%s/skin";
    private static final String RESET_SKIN_URL = "https://api.mojang.com/user/profile/%s/skin";
    private static final String SKIN_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s" +
            "?unsigned=false";

    //authentication
    private static final String AUTH_URL = "https://authserver.mojang.com/authenticate";
    private static final String HAS_JOINED_URL = "https://sessionserver.mojang.com/session/minecraft/hasJoined?" +
            "username=%s&serverId=%s&ip=%s";

    private ProxySelector proxySelector = ProxySelector.getDefault();

    private int maxNameRequests = 600;
    private final Map<Object, Object> requests = SafeCacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(CacheLoader.from(() -> {
                throw new UnsupportedOperationException();
            }));

    @Override
    public Optional<Verification> hasJoined(String username, String serverHash, InetAddress hostIp)
            throws IOException {
        String encodedIp = URLEncoder.encode(hostIp.getHostAddress(), StandardCharsets.UTF_8.name());
        String url = String.format(HAS_JOINED_URL, username, serverHash, encodedIp);

        HttpURLConnection conn = getConnection(url);
        if (conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
            return Optional.empty();
        }

        return Optional.of(readJson(conn.getInputStream(), Verification.class));
    }

    @Override
    public Account authenticate(String email, String password) throws IOException {
        HttpURLConnection conn = getConnection(AUTH_URL);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.append(gson.toJson(new AuthRequest(email, password)));
        }

        AuthResponse authResponse = readJson(conn.getInputStream(), AuthResponse.class);
        return new Account(authResponse.getSelectedProfile(), authResponse.getAccessToken());
    }

    @Override
    public void changeSkin(Account account, String toUrl, boolean slimModel) throws IOException {
        String url = String.format(CHANGE_SKIN_URL, UUIDAdapter.toMojangId(account.getProfile().getId()));

        HttpURLConnection conn = getConnection(url);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        conn.addRequestProperty("Authorization", "Bearer " + account.getAccessToken());
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.write("model=");
            if (slimModel) {
                writer.write("slim");
            }

            writer.write("&url=" + URLEncoder.encode(toUrl, StandardCharsets.UTF_8.name()));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Response code is not Ok: " + responseCode);
        }
    }

    @Override
    public void changeSkin(Account account, RenderedImage pngImage, boolean slimModel) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean resetSkin(Account account) throws IOException {
        String url = String.format(RESET_SKIN_URL, account.getProfile().getId());

        HttpURLConnection conn = getConnection(url);
        conn.setRequestMethod("DELETE");
        conn.addRequestProperty("Authorization", "Bearer " + account.getAccessToken());

        int responseCode = conn.getResponseCode();
        return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT;
    }

    @Override
    public ImmutableSet<Profile> findProfiles(String... names) throws IOException, RateLimitException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ImmutableList<NameHistory> findNames(UUID uuid) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Profile> findProfile(String name) throws IOException, RateLimitException {
        Optional<Profile> optProfile = cache.getByName(name);
        if (optProfile.isPresent() || !validNamePredicate.test(name)) {
            return optProfile;
        }

        String url = UUID_URL + name;
        HttpURLConnection conn;

        if (requests.size() >= maxNameRequests) {
            conn = getProxyConnection(url);
        } else {
            conn = getConnection(url);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == RateLimitException.RATE_LIMIT_RESPONSE_CODE) {
            if (conn.usingProxy()) {
                throw new RateLimitException();
            }

            conn = getProxyConnection(url);
            responseCode = conn.getResponseCode();
        }

        if (!conn.usingProxy()) {
            requests.put(new Object(), new Object());
        }

        if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
            return Optional.empty();
        }

        Profile profile = readJson(conn.getInputStream(), Profile.class);
        cache.add(profile);
        return Optional.of(profile);
    }

    @Override
    public Optional<Profile> findProfile(String name, Instant time) throws IOException, RateLimitException {
        Optional<Profile> optProfile = cache.getByName(name);
        if (optProfile.isPresent() || !validNamePredicate.test(name)) {
            return optProfile;
        }

        requests.put(new Object(), new Object());
        if (requests.size() >= maxNameRequests) {
            throw new RateLimitException();
        }

        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<SkinProperty> downloadSkin(UUID uuid) throws IOException, RateLimitException {
        Optional<SkinProperty> optSkin = cache.getSkin(uuid);
        if (optSkin.isPresent()) {
            return optSkin;
        }

        String url = String.format(SKIN_URL, UUIDAdapter.toMojangId(uuid));
        HttpURLConnection conn = getConnection(url);

        int responseCode = conn.getResponseCode();
        if (responseCode == RateLimitException.RATE_LIMIT_RESPONSE_CODE) {
            throw new RateLimitException();
        }

        if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
            return Optional.empty();
        }

        Textures texturesModel = readJson(conn.getInputStream(), Textures.class);
        SkinProperty property = texturesModel.getProperties()[0];

        cache.addSkin(uuid, property);
        return Optional.of(property);
    }

    private HttpURLConnection getProxyConnection(String url) throws RateLimitException, IOException {
        Proxy proxy = proxySelector.select(URI.create(url)).get(0);
        if (proxy.type() == Type.DIRECT || proxy == Proxy.NO_PROXY) {
            throw new RateLimitException();
        }

        HttpURLConnection conn = getConnection(UUID_URL, proxy);
        int responseCode = conn.getResponseCode();
        if (responseCode == RateLimitException.RATE_LIMIT_RESPONSE_CODE) {
            throw new RateLimitException();
        }

        return conn;
    }

    /**
     * @return maximum amount of name to UUID requests that will be established to Mojang directly without proxies.
     * (Between 0 and 600 within 10 minutes). Default is 600.
     */
    public int getMaxNameRequests() {
        return maxNameRequests;
    }

    /**
     * @return the current proxy selector or {@link ProxySelector#getDefault()} if none
     */
    public ProxySelector getProxySelector() {
        return proxySelector;
    }

    /**
     * @param proxySelector proxy selector that should be used
     */
    public void setProxySelector(ProxySelector proxySelector) {
        this.proxySelector = proxySelector;
    }

    /**
     * @param maxNameRequests maximum amount of name to UUID requests that will be established to Mojang directly
     *                        without proxies. (Between 0 and 600 within 10 minutes)
     */
    public void setMaxNameRequests(int maxNameRequests) {
        this.maxNameRequests = Math.min(0, Math.max(600, maxNameRequests));
    }
}
