package com.github.games647.craftapi.model.auth;

import com.github.games647.craftapi.model.Profile;

import java.util.Objects;

/**
 * Result after an authentication request.
 */
public class AuthResponse {

    private String accessToken;
    private Profile selectedProfile;

    /**
     * @return access token for authenticating without a password
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @return in game profile
     */
    public Profile getSelectedProfile() {
        return selectedProfile;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other instanceof AuthResponse) {
            AuthResponse that = (AuthResponse) other;
            return Objects.equals(accessToken, that.accessToken) &&
                    Objects.equals(selectedProfile, that.selectedProfile);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, selectedProfile);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' +
                "selectedProfile=" + selectedProfile +
                '}';
    }
}
