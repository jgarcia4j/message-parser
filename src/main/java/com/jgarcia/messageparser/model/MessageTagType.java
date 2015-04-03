package com.jgarcia.messageparser.model;

import java.util.regex.Pattern;

/**
 * The special message tag types.
 */
public enum MessageTagType {
    /**
     * A user or group mentioned in a message. Denoted by an "@" directly preceding the username or group name.
     */
    USER_MENTION(Pattern.compile("(?:^|\\W)(?<mention>@([\\S]+))"), "mention"),
    /**
     * A custom emoticon. Denoted by wrapping the 15-character limited ASCII keyword with open and closed parenthesis.
     *
     * Note: I am assuming that emoticons cannot contain parentheses within the wrapping parentheses.
     */
    EMOTICON(Pattern.compile("(?<emoticon>\\([^()]{1,15}\\))"), "emoticon"),
    /**
     * A link/url. Denoted by the "http://" or "https://" protocol prefix.
     */
    LINK(Pattern.compile("(?<url>(?<protocol>https?://)(?<hostname>[^/\\p{Space}]+)(?<path>/[^\\p{Space}]*)?)"), "url");

    /**
     * The regular expression.
     */
    private final Pattern pattern;

    /**
     * The capture group name.
     */
    private final String group;

    MessageTagType(final Pattern pattern, final String group) {
        this.pattern = pattern;
        this.group = group;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getGroup() {
        return group;
    }
}
