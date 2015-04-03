package com.jgarcia.messageparser.exception;

/**
 * A user exception is thrown whenever a user-related operation fails unexpectedly.
 */
public class UserException extends Exception {

    /**
     * The user's id.
     */
    private final Long userId;

    /**
     * The user's handle.
     */
    private final String userHandle;

    public UserException(final long userId, final String message) {
        super(message);
        this.userId = userId;
        this.userHandle = null;
    }

    public UserException(final String userHandle, final String message) {
        super(message);
        this.userHandle = userHandle;
        this.userId = null;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserHandle() {
        return userHandle;
    }
}
