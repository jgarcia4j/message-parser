package com.jgarcia.messageparser.model;

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
     * The substring end index (inclusive).
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
}
