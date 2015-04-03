package com.jgarcia.messageparser.service;

import com.jgarcia.messageparser.exception.UserException;
import com.jgarcia.messageparser.model.User;

import java.util.Optional;

/**
 * A service that exposes {@link User}-related operations.
 */
public interface UserService {

    /**
     * Returns the {@link User} with the given unique {@code handle}.
     *
     * @param handle the non-null handle.
     * @return the {@link User} with the given unique {@code handle} if such a {@code User} exists.
     * @throws UserException if an unexpected error occurs and the user could not be retrieved.
     */
    Optional<User> findUserByHandle(String handle) throws UserException;
}
