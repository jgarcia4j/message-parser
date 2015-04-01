package com.jgarcia.messageparser;

import com.jgarcia.LinkParser;
import com.jgarcia.messageparser.model.Link;
import com.jgarcia.messageparser.model.MessageContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses a message for specialized content (mentions, emoticons, links).
 */
public class MessageContentParser {

    private static final Logger LOG = LoggerFactory.getLogger(MessageContentParser.class);

    public MessageContent parse(final String message) {
        final List<String> mentions = new MentionParser().parse(message);
        final List<String> emoticons = new EmoticonParser().parse(message);
        final List<String> urls = new LinkParser().parse(message);
        final List<Link> links = new ArrayList<>(urls.size());
        for (final String url : urls) {
            try {
                final Document document = Jsoup.connect(url).get();
                links.add(new Link(url, document.title()));
            } catch (IOException e) {
                LOG.warn("Unable to get document title: url={}", url);
            }
        }
        return new MessageContent(mentions, emoticons, links);
    }
}
