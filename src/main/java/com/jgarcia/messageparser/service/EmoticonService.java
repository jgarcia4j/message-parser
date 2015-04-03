package com.jgarcia.messageparser.service;

import java.util.Optional;

/**
 * A service that exposes emoticon-related operations.
 */
public interface EmoticonService {

    /**
     * Returns the image url for the given emoticon {@code keyword}.
     *
     * @param keyword the non-null emoticon keyword.
     * @return the image url for the given emoticon {@code keyword} if such an emoticon exists.
     */
    Optional<String> findEmoticonByKeyword(final String keyword);
}
