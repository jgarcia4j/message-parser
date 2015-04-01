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

    public Link(final String url, final String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Link)) {
            return false;
        }
        final Link that = (Link) obj;
        return Objects.equals(this.url, that.url)
                && Objects.equals(this.title, that.title);
    }

    @Override
    public String toString() {
        return Objects.toString(this);
    }
}
