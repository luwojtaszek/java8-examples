package com.luwojtaszek.j8.examples.java8;

@FunctionalInterface
public interface BusinessNameProvider<T> {
    String provideName(T object);
//    String secondProvideName(T object); // cannot specify more than one method

    default String provideDefaultName(T object) {
        return object.getClass().getName();
    }
}
