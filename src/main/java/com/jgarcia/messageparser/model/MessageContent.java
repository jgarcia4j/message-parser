package com.jgarcia.messageparser.model;

import java.util.List;
import java.util.Objects;

/**
 * Special content parsed from the user's original message.
 */
public class MessageContent {

    /**
     * The list of users mentioned.
     */
    private final List<UserMention> mentions;

    /**
     * The list of custom emoticons.
     */
    private final List<Emoticon> emoticons;

    /**
     * The list of links.
     */
    private final List<Link> links;

    public MessageContent(final List<UserMention> mentions, final List<Emoticon> emoticons, final List<Link> links) {
        this.mentions = mentions;
        this.emoticons = emoticons;
        this.links = links;
    }

    public List<UserMention> getMentions() {
        return mentions;
    }

    public List<Emoticon> getEmoticons() {
        return emoticons;
    }

    public List<Link> getLinks() {
        return links;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mentions, emoticons, links);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MessageContent)) {
            return false;
        }
        final MessageContent that = (MessageContent) obj;
        return Objects.equals(this.mentions, that.mentions)
                && Objects.equals(this.emoticons, that.emoticons)
                && Objects.equals(this.links, that.links);
    }

    @Override
    public String toString() {
        return "MessageContent{" +
                "mentions=" + mentions +
                ", emoticons=" + emoticons +
                ", links=" + links +
                '}';
    }
}
