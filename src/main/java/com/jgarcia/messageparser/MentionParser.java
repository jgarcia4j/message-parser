package com.jgarcia.messageparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses mentions from the chat message.
 */
public class MentionParser {

    /**
     * Pattern that matches the convention for mentions:
     * <ul>
     *     <li>Single word that begins with @</li>
     * </ul>
     */
    private static final Pattern MENTION_PATTERN = Pattern.compile("(^|\\W)@(?<mention>[\\S]+)");

    public List<String> parseMentions(final String message) {
        final Matcher matcher = MENTION_PATTERN.matcher(message);
        final List<String> mentions = new ArrayList<>();
        while (matcher.find()) {
            final String mention = matcher.group("mention");
            if (isValidMention(mention)) {
                mentions.add(mention);
            }
        }
        return mentions;
    }

    public boolean isValidMention(final String mention) {
        return true;
    }
}
