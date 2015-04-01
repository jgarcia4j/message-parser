package com.jgarcia.messageparser;

import com.jgarcia.messageparser.model.MessageContent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a message for specialized content (mentions, emoticons, links).
 */
public class MessageContentParser {

    public MessageContent parse(final String message) {
        final MentionParser mentionParser = new MentionParser();
        final List<String> mentions = mentionParser.parseMentions(message);
        return new MessageContent(mentions, null, null);
    }
}
