package com.jgarcia.messageparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses emoticons from the chat message.
 */
public class EmoticonParser {

    /**
     * Pattern that matches the convention for custom emoticons:
     * <ul>
     *     <li>ASCII characters wrapped in parentheses</li>
     * </ul>
     *
     * Note: I am assuming that emoticons cannot contain parentheses within the wrapping parentheses.
     */
    private static final Pattern EMOTICON_PATTERN = Pattern.compile("\\((?<emoticon>[^()]{1,15})\\)");

    protected static final int MAX_LENGTH = 15;

    public List<String> parse(final String message) {
        final Matcher matcher = EMOTICON_PATTERN.matcher(message);
        final List<String> emoticons = new ArrayList<>();
        while (matcher.find()) {
            final String emoticon = matcher.group("emoticon");
            if (isValid(emoticon)) {
                emoticons.add(emoticon);
            }
        }
        return emoticons;
    }

    private boolean isValid(final String emoticon) {
        return true;
    }
}
