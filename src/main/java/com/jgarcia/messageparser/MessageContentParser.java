package com.jgarcia.messageparser;

import com.jgarcia.messageparser.exception.UserException;
import com.jgarcia.messageparser.model.*;
import com.jgarcia.messageparser.service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a message for specialized content (mentions, emoticons, links).
 */
public class MessageContentParser {

    private static final Logger LOG = LoggerFactory.getLogger(MessageContentParser.class);

    private final UserService userService;

    public MessageContentParser(final UserService userService) {
        this.userService = userService;
    }

    public MessageContent parse(final String message) {
        final List<String> emoticons = new ArrayList<>();
        final List<String> urls = new ArrayList<>();
        final List<UserMention> userMentions = new ArrayList<>();
        final List<MessageTag> tags = extractTags(message);
        for (final MessageTag tag : tags) {
            switch (tag.getType()) {
                case USER_MENTION:
                    final String handle = message.substring(tag.getStart(), tag.getEnd()); // Remove prefix @ identifier.
                    final Optional<User> user;
                    try {
                        user = userService.findUserByHandle(handle);
                        if (user.isPresent()) {
                            userMentions.add(new UserMention(user.get(), tag));
                        }
                    } catch (UserException e) {
                        LOG.warn("Unable to retrieve mentioned user, ignoring mention! handle={}", handle, e);
                    }
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
        return new MessageContent((userMentions.isEmpty()) ? null : userMentions, (emoticons.isEmpty()) ? null : emoticons, (links.isEmpty()) ? null : links);
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
