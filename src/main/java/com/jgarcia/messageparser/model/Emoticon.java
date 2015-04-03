package com.jgarcia.messageparser.model;

import java.util.Objects;

/**
 * An emoticon.
 */
public class Emoticon {

    /**
     * The emoticon keyword.
     */
    private final String keyword;

    /**
     * The image url for the emoticon.
     */
    private final String imageUrl;

    /**
     * The message tag used to identify the substring of the message to be replaced by the emoticon.
     */
    private final MessageTag messageTag;

    public Emoticon(final String keyword, final String imageUrl, final MessageTag messageTag) {
        this.keyword = keyword;
        this.imageUrl = imageUrl;
        this.messageTag = messageTag;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MessageTag getMessageTag() {
        return messageTag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword, imageUrl, messageTag);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Emoticon)) {
            return false;
        }
        final Emoticon that = (Emoticon) obj;
        return Objects.equals(this.keyword, that.keyword)
                && Objects.equals(this.imageUrl, that.imageUrl)
                && Objects.equals(this.messageTag, that.messageTag);
    }

    @Override
    public String toString() {
        return Objects.toString(this);
    }
}
