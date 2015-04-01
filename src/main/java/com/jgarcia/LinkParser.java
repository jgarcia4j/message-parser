package com.jgarcia;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkParser {

    /**
     * Pattern that matches the convention for links:
     * <ul>
     *     <li>Any word containing "http://" or "https://"</li>
     * </ul>
     */
    private static final Pattern LINK_PATTERN = Pattern.compile(
            "(?<protocol>https?://)(?<hostname>[^/\\p{Space}]+)(?<path>/[^\\p{Space}]*)?");

    public List<String> parse(final String message) {
        final Matcher matcher = LINK_PATTERN.matcher(message);
        final List<String> urls = new ArrayList<>();
        while (matcher.find()) {
            final String url = matcher.group();
            urls.add(url);
        }
        return urls;
    }
}
