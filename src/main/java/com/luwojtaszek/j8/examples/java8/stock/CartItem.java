package com.luwojtaszek.j8.examples.java8.stock;

import lombok.Value;
import lombok.experimental.NonFinal;

import java.math.BigDecimal;

@Value
public class CartItem {
    private String id;
    private String type;
    private BigDecimal price;
    @NonFinal
    private int quantity = 1;

    public void increaseQuantity(int value) {
        this.quantity += value;
    }

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

}
