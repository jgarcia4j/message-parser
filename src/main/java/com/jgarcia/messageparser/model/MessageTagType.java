package com.jgarcia.messageparser.model;

/**
 * The special message tag types.
 */
public enum MessageTagType {
    /**
     * A user or group mentioned in a message. Denoted by an "@" directly preceding the username or group name.
     */
    USER_MENTION,
    /**
     * A custom emoticon. Denoted by wrapping the 15-character limited ASCII keyword with open and closed parenthesis.
     */
    EMOTICON,
    /**
     * A link/url. Denoted by the "http://" or "https://" protocol prefix.
     */
    LINK
}
