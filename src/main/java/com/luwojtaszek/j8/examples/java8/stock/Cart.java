package com.luwojtaszek.j8.examples.java8.stock;

import lombok.NonNull;
import lombok.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

@Value
public class Cart {
    private Map<String, CartItem> items = new HashMap<>();

    public void addItem(@NonNull CartItem item) {
        final CartItem existingItem = items.get(item.getId());
        if (nonNull(existingItem)) {
            existingItem.increaseQuantity(item.getQuantity());
        } else {
            items.put(item.getId(), item);
        }
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }
}
