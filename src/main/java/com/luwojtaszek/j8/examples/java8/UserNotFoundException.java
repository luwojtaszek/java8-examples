package com.luwojtaszek.j8.examples.java8;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final Long userId;

    public UserNotFoundException(Long userId) {
        super("Not found user by id: " + userId);
        this.userId = userId;
    }
}
