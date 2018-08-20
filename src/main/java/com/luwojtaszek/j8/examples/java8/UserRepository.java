package com.luwojtaszek.j8.examples.java8;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.nonNull;

public interface UserRepository {
    Optional<User> findById(Long id);

    Collection<User> findUsers();

    default Optional<User> findByUser(User user) {
        return nonNull(user) ? findById(user.getId()) : Optional.empty();
    }

    default User findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    static String generateUserTextId() {
        return "US__" + UUID.randomUUID().toString();
    }

}
