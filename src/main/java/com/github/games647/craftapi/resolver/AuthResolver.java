package com.github.games647.craftapi.resolver;

import com.github.games647.craftapi.model.auth.Account;
import com.github.games647.craftapi.model.auth.Verification;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

/**
 * Resolver that handles authentication requests.
 */
public interface AuthResolver {

    /**
     * Verifies if a player requesting to join a online mode server is actually authenticated against Mojang.
     *
     * @param username the joining username
     * @param serverHash server id hash
     * @param hostIp the player connecting IP address
     * @return the verification response or empty if invalid
     * @throws IOException I/O exception contacting the server
     */
    Optional<Verification> hasJoined(String username, String serverHash, InetAddress hostIp) throws IOException;

    /**
     * Logs the given player account in.
     *
     * @param email email address
     * @param password plain text password
     * @return logged in account
     * @throws IOException I/O exception contacting the server
     */
    Account authenticate(String email, String password) throws IOException;

    /**
     * Changes the skin to the image that can be download from that URL. The URL have to be direct link without
     * things like HTML in it.
     *
     * @param account authenticated account
     * @param toUrl the URL that Mojang should use for downloading the skin from
     * @param slimModel true if slim model or false for steve
     * @throws IOException I/O exception contacting the server
     */
    void changeSkin(Account account, String toUrl, boolean slimModel) throws IOException;

    /**
     * Changes the skin to the given image.
     *
     * @param account authenticated account
     * @param pngImage png image
     * @param slimModel true if slim model or false for steve
     * @throws IOException I/O exception contacting the server
     */
    void changeSkin(Account account, RenderedImage pngImage, boolean slimModel) throws IOException;

    /**
     * Clears the uploaded skin.
     *
     * @param account authenticated account
     * @throws IOException I/O exception contacting the server
     */
    boolean resetSkin(Account account) throws IOException;

}
