package com.jgarcia.messageparser.model;

import java.util.Objects;

/**
 * The link between a mention tag and the referenced user.
 */
public class UserMention {

    /**
     * The referenced user.
     */
    private final User user;

    /**
     * The message tag to identify the substring to wrap with mention markup.
     */
    private final MessageTag tag;

    public UserMention(final User user, final MessageTag tag) {
        this.user = user;
        this.tag = tag;
    }

    public User getUser() {
        return user;
    }

    public MessageTag getTag() {
        return tag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, tag);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserMention)) {
            return false;
        }
        final UserMention that = (UserMention) obj;
        return Objects.equals(this.user, that.user)
                && Objects.equals(this.tag, that.tag);
    }

    @Override
    public String toString() {
        return "UserMention{" +
                "user=" + user +
                ", tag=" + tag +
                '}';
    }
}
