package com.jgarcia.messageparser;

import com.jgarcia.messageparser.model.Link;
import com.jgarcia.messageparser.model.MessageContent;
import com.jgarcia.messageparser.model.MessageTag;
import com.jgarcia.messageparser.model.MessageTagType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a message for specialized content (mentions, emoticons, links).
 */
public class MessageContentParser {

    private static final Logger LOG = LoggerFactory.getLogger(MessageContentParser.class);

    public MessageContent parse(final String message) {
        final List<String> mentions = new ArrayList<>();
        final List<String> emoticons = new ArrayList<>();
        final List<String> urls = new ArrayList<>();
        final List<MessageTag> tags = extractTags(message);
        for (final MessageTag tag : tags) {
            switch (tag.getType()) {
                case USER_MENTION:
                    mentions.add(message.substring(tag.getStart(), tag.getEnd()));
                    break;
                case EMOTICON:
                    emoticons.add(message.substring(tag.getStart(), tag.getEnd()));
                    break;
                case LINK:
                    urls.add(message.substring(tag.getStart(), tag.getEnd()));
                    break;
            }
        }
        final List<Link> links = new ArrayList<>(urls.size());
        for (final String url : urls) {
            String title = null;
            try {
                final Document document = Jsoup.connect(url).get();
                title = document.title();
            } catch (IOException e) {
                LOG.warn("Unable to get document title: url={}", url);
            }

            links.add(new Link(url, title));
        }
        return new MessageContent((mentions.isEmpty()) ? null : mentions, (emoticons.isEmpty()) ? null : emoticons, (links.isEmpty()) ? null : links);
    }

    private static List<MessageTag> extractTags(final String message) {
        final List<MessageTag> tags = new ArrayList<>();
        for (final MessageTagType messageTagType : MessageTagType.values()) {
            final Pattern pattern = messageTagType.getPattern();
            final Matcher matcher = pattern.matcher(message);
            final String group = messageTagType.getGroup();
            while (matcher.find()) {
                tags.add(new MessageTag(messageTagType, matcher.start(group), matcher.end(group)));
            }
        }
        return tags;
    }
}
