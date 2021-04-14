package com.github.games647.craftapi.resolver;

import java.util.UUID;

/**
 * Exception that occurs if we made too many requests against an online resolver.
 */
public class RateLimitException extends Exception {

    public static final int RATE_LIMIT_RESPONSE_CODE = 429;

    public RateLimitException() {
        super("Too many requests", null, true, false);
    }

    public RateLimitException(String playerName) {
        super("Too many requests for the UUID of player " + playerName, null, true, false);
    }

    public RateLimitException(UUID skinId) {
        super("Too many requests for skin " + skinId, null, true, false);
    }
}
