package com.luwojtaszek.j8.examples.java8;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SomeService<T> {

    private final T source;

    public void doLogic(BusinessNameProvider<T> provider) {
        // doing some logic here
        log.info("Business name: {}", provider.provideName(source));
    }
}
