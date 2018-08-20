package com.luwojtaszek.j8.examples.java8;

import com.luwojtaszek.j8.examples.java8.stock.Cart;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    @NonFinal
    private Cart cart;
}
