package com.github.games647.craftapi.resolver;

import com.github.games647.craftapi.model.auth.Account;
import com.github.games647.craftapi.model.auth.Verification;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

/**
 *
 */
public interface AuthResolver {

    /**
     *
     * @param username
     * @param serverHash
     * @param hostIp
     * @return
     * @throws IOException
     */
    Optional<Verification> hasJoined(String username, String serverHash, InetAddress hostIp) throws IOException;

    /**
     *
     * @param email
     * @param password
     * @return
     * @throws IOException
     */
    Account authenticate(String email, String password) throws IOException;

    /**
     *
     * @param account
     * @param toUrl
     * @param slimModel
     * @throws IOException
     */
    void changeSkin(Account account, String toUrl, boolean slimModel) throws IOException;

    /**
     *
     * @param account
     * @param pngImage
     * @param slimModel
     * @throws IOException
     */
    void changeSkin(Account account, RenderedImage pngImage, boolean slimModel) throws IOException;

    /**
     *
     * @param account
     * @throws IOException
     */
    boolean resetSkin(Account account) throws IOException;

}
