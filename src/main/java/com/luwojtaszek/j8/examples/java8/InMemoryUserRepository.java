package com.luwojtaszek.j8.examples.java8;

import com.google.common.collect.ImmutableSet;
import com.luwojtaszek.j8.examples.java8.stock.Cart;
import com.luwojtaszek.j8.examples.java8.stock.CartItem;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> map = new ConcurrentHashMap<>();

    public InMemoryUserRepository() {
        ImmutableSet.of(user1(), user2(), user3()).forEach(user -> map.put(user.getId(), user));

    }

    private static User user1() {
        final Cart cart = new Cart();
        cart.addItem(new CartItem("coca-cola", "drink", BigDecimal.valueOf(2), 16));
        cart.addItem(new CartItem("sprite", "drink", BigDecimal.valueOf(1.55), 4));
        cart.addItem(new CartItem("pizza", "food", BigDecimal.valueOf(10), 2));

        return new User(1L, "Lukasz", "Wojtaszek", 26, cart);
    }

    private static User user2() {
        final Cart cart = new Cart();
        cart.addItem(new CartItem("pianino", "instrument", BigDecimal.valueOf(2000), 1));
        cart.addItem(new CartItem("wiolonczela", "instrument", BigDecimal.valueOf(1500), 3));
        cart.addItem(new CartItem("gitara", "instrument", BigDecimal.valueOf(1000), 4));

        return new User(2L, "Jan", "Kowalski", 23, cart);
    }

    private static User user3() {
        final Cart cart = new Cart();
        cart.addItem(new CartItem("coca-cola", "drink", BigDecimal.valueOf(2), 1));

        return new User(3L, "Lukasz", "jakis", 45, cart);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public Collection<User> findUsers() {
        return map.values();
    }
}
