package com.jgarcia.messageparser;

import com.jgarcia.messageparser.exception.UserException;
import com.jgarcia.messageparser.model.*;
import com.jgarcia.messageparser.service.EmoticonService;
import com.jgarcia.messageparser.service.LinkService;
import com.jgarcia.messageparser.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final EmoticonService emoticonService;

    private final LinkService linkService;

    public MessageContentParser(final UserService userService,
                                final EmoticonService emoticonService,
                                final LinkService linkService) {
        this.userService = userService;
        this.emoticonService = emoticonService;
        this.linkService = linkService;
    }

    public MessageContent parse(final String message) {
        final List<UserMention> userMentions = new ArrayList<>();
        final List<Emoticon> emoticons = new ArrayList<>();
        final List<Link> links = new ArrayList<>();
        final List<MessageTag> tags = extractTags(message);
        for (final MessageTag tag : tags) {
            switch (tag.getType()) {
                case USER_MENTION:
                    final String handle = message.substring(tag.getStart() + 1, tag.getEnd()); // Remove prefix @ identifier.
                    final Optional<User> user;
                    try {
                        user = userService.findUserByHandle(handle);
                        if (user.isPresent()) {
                            userMentions.add(new UserMention(user.get(), tag));
                        } else {
                            LOG.debug("Mentioned user does not exist, ignoring mention. handle={}", handle);
                        }
                    } catch (UserException e) {
                        LOG.warn("Unable to retrieve mentioned user, ignoring mention! handle={}", handle, e);
                    }
                    break;
                case EMOTICON:
                    final String keyword = message.substring(tag.getStart() + 1, tag.getEnd() - 1); // Remove () wrappers.
                    final Optional<String> emoticonUrl = emoticonService.findEmoticonByKeyword(keyword);
                    if (emoticonUrl.isPresent()) {
                        emoticons.add(new Emoticon(keyword, emoticonUrl.get(), tag));
                    } else {
                        LOG.debug("Unable to find/retrieve emoticon: keyword={}", keyword);
                    }
                    break;
                case LINK:
                    final String url = message.substring(tag.getStart(), tag.getEnd());
                    final Optional<String> title = linkService.getDocumentTitle(url);
                    // Intentionally adding link despite the possible null title as the client
                    // should still treat the tag as a link.
                    links.add(new Link(url, title.orElse(null), tag));
                    break;
            }
        }
        return new MessageContent(
                (userMentions.isEmpty()) ? null : userMentions,
                (emoticons.isEmpty()) ? null : emoticons,
                (links.isEmpty()) ? null : links);
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
