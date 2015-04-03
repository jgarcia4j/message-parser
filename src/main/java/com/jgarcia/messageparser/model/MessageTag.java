package com.jgarcia.messageparser.model;

import java.util.Objects;

/**
 * A tag represents a portion of a {@code String} message with special semantic meaning.
 *
 * @see MessageTagType
 */
public class MessageTag {

    /**
     * The message tag type.
     */
    private final MessageTagType type;

    /**
     * The substring start index.
     */
    private final int start;

    /**
     * The substring end index (exclusive).
     */
    private final int end;

    public MessageTag(final MessageTagType type, final int start, final int end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public MessageTagType getType() {
        return type;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MessageTag)) {
            return false;
        }
        final MessageTag that = (MessageTag) obj;
        return Objects.equals(this.type, that.type)
                && Objects.equals(this.start, that.start)
                && Objects.equals(this.end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, start, end);
    }

    @Override
    public String toString() {
        return "MessageTag{" +
                "type=" + type +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
