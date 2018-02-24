package com.github.games647.craftapi.model.auth;

import com.github.games647.craftapi.model.Profile;

import java.util.Objects;

/**
 * Represents a logged in Mojang account.
 */
public class Account {

    private final Profile profile;
    private final String accessToken;

    public Account(Profile profile, String accessToken) {
        this.profile = profile;
        this.accessToken = accessToken;
    }

    /**
     * @return Minecraft profile that associated this account
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * @return access token for authentication against Mojang servers
     */
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other instanceof Account) {
            Account account = (Account) other;
            return Objects.equals(profile, account.profile) &&
                    Objects.equals(accessToken, account.accessToken);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile, accessToken);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' +
                "profile=" + profile +
                '}';
    }
}
