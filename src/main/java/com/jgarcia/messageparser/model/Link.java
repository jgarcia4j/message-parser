package com.jgarcia.messageparser.model;

import java.util.Objects;

/**
 * A web link.
 */
public class Link {

    /**
     * The well-formed url to a web document.
     */
    private final String url;

    /**
     * The document's title.
     */
    private final String title;

    /**
     * The message tag used to identify the substring of the message to be wrapped in an anchor tag.
     */
    private final MessageTag messageTag;

    public Link(final String url, final String title, final MessageTag messageTag) {
        this.url = url;
        this.title = title;
        this.messageTag = messageTag;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public MessageTag getMessageTag() {
        return messageTag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title, messageTag);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Link)) {
            return false;
        }
        final Link that = (Link) obj;
        return Objects.equals(this.url, that.url)
                && Objects.equals(this.title, that.title)
                && Objects.equals(this.messageTag, that.messageTag);
    }

    @Override
    public String toString() {
        return "Link{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", messageTag=" + messageTag +
                '}';
    }
}
