package com.jgarcia.messageparser.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * A link service that primarily delegates to the JSoup library.
 */
public class JsoupLinkService implements LinkService {

    private static final Logger LOG = LoggerFactory.getLogger(JsoupLinkService.class);

    @Override
    public Optional<String> getDocumentTitle(final String url) {
        String title = null;
        try {
            final Document document = Jsoup.connect(url).get();
            title = document.title();
        } catch (IOException e) {
            LOG.warn("Unable to get document title: url={}", url);
        }
        return Optional.ofNullable(title);
    }
}
