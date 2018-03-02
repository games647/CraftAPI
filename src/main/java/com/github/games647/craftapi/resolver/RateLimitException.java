package com.github.games647.craftapi.resolver;

/**
 * Exception that occurs if we made too many requests against an online resolver.
 */
public class RateLimitException extends Exception {

    public static final int RATE_LIMIT_RESPONSE_CODE = 429;

    public RateLimitException() {
        super("Too many requests");
    }

}
