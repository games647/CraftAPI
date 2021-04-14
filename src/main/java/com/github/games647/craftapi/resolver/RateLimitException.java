package com.github.games647.craftapi.resolver;

import java.util.UUID;

/**
 * Exception that occurs if we made too many requests against an online resolver.
 */
public class RateLimitException extends Exception {

    public static final int RATE_LIMIT_RESPONSE_CODE = 429;

    /**
     * Generic rate limitation
     */
    public RateLimitException() {
        super("Too many requests", null, true, false);
    }

    /**
     * Rate limitation for the given player name
     */
    public RateLimitException(String playerName) {
        super("Too many requests for the UUID of player " + playerName, null, true, false);
    }

    /**
     * Rate limit for skin download of the specified account UUID
     */
    public RateLimitException(UUID skinId) {
        super("Too many requests for skin " + skinId, null, true, false);
    }
}
