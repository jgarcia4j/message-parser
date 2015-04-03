package com.jgarcia.messageparser.model;

/**
 * A (ridiculously simple) registered chat user. Modeled to provide referential integrity on mentions.
 */
public class User {

    private final long id;

    private final String handle;

    public User(final long id, final String handle) {
        this.id = id;
        this.handle = handle;
    }
}
