package com.jgarcia.messageparser.exception;

/**
 * A user exception is thrown whenever a user-related operation fails unexpectedly.
 */
public class UserException extends Exception {

    private final long userId;

    public UserException(final long userId, final String message) {
        super(message);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
