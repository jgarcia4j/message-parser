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
                    getMentionedUser(handle).ifPresent(user -> userMentions.add(new UserMention(user, tag)));
                    break;
                case EMOTICON:
                    final String keyword = message.substring(tag.getStart() + 1, tag.getEnd() - 1); // Remove () wrappers.
                    getEmoticonImageUrl(keyword)
                            .ifPresent(imageUrl -> emoticons.add(new Emoticon(keyword, imageUrl, tag)));
                    break;
                case LINK:
                    final String url = message.substring(tag.getStart(), tag.getEnd());
                    // Intentionally adding link despite the possible null title as the client
                    // should still treat the tag as a link.
                    links.add(new Link(url, linkService.getDocumentTitle(url).orElse(null), tag));
                    break;
            }
        }
        // Set empty lists as null so Jackson doesn't serialize the empty JSON array as "[]"
        return new MessageContent(
                (userMentions.isEmpty()) ? null : userMentions,
                (emoticons.isEmpty()) ? null : emoticons,
                (links.isEmpty()) ? null : links);
    }

    /**
     * Returns the list of extracted {@link MessageTag}s which were parsed from the given {@code message}.
     *
     * @param message the non-null message
     * @return the list of extracted {@link MessageTag}s which were parsed from the given {@code message}.
     *         The extraction is accomplished by using regular expression pattern matching based on the
     *         pattern defined for each {@link MessageTagType}.
     */
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

    private Optional<User> getMentionedUser(final String handle) {
        User user = null;
        try {
            user = userService.findUserByHandle(handle).orElse(null);
            if (user == null) {
                LOG.debug("Mentioned user does not exist, ignoring mention. handle={}", handle);
            }
        } catch (UserException e) {
            LOG.warn("Unable to retrieve mentioned user, ignoring mention! handle={}", handle, e);
        }
        return Optional.ofNullable(user);
    }

    private Optional<String> getEmoticonImageUrl(final String keyword) {
        final Optional<String> imageUrl = emoticonService.findEmoticonByKeyword(keyword);
        if (!imageUrl.isPresent()) {
            LOG.debug("Unable to find/retrieve emoticon: keyword={}", keyword);
        }
        return imageUrl;
    }
}
