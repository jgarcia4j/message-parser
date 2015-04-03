package com.jgarcia.messageparser.model;

import java.util.Objects;

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

    public long getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, handle);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        final User that = (User) obj;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.handle, that.handle);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", handle='" + handle + '\'' +
                '}';
    }
}
