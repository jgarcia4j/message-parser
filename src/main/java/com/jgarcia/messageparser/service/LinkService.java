package com.jgarcia.messageparser.service;

import com.jgarcia.messageparser.exception.UserException;
import com.jgarcia.messageparser.model.User;

import java.util.Optional;

/**
 * A service to handle interactions with web links.
 */
public interface LinkService {

    /**
     * Returns title of the document at the provided {@code url}.
     *
     * @param url the non-null url.
     * @return the title of the document at the provided {@code url} if it exists and has a valid "title" element.
     */
    Optional<String> getDocumentTitle(String url);

}
