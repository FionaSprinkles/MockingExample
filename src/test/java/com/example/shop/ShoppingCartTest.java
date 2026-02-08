package com.example.shop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ShoppingCartTest {

    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
    }

    /**
 * Implementationen ska stödja:
 *  Lägga till varor
 *  Ta bort varor
 *  Beräkna totalpris
 *  Applicera rabatter
 *  Hantera kvantitetsuppdateringar
 */

    /**
     * Verifies that addItem puts item holidayPackageStandard in cart.
     *
     * Tested scenarios:
     * - item is put in cart.
     */
    @Test
    @DisplayName("Should add item to cart")
    void shouldAddItemToCart() {
        String packageStandard = "holidayPackageStandard";

        cart.addItem(packageStandard);

        assertThat(cart.containsItem(packageStandard)).isTrue();
    }

    /**
     * Verifies that deleteItem removes item from cart.
     *
     * Tested scenarios:
     * - item is removed from cart
     */
    @Test
    @DisplayName("Item should be deleted from cart")
    void shouldDeleteItemFromCart() {
        String packageStandard = "holidayPackageStandard";

        cart.addItem(packageStandard);

        assertThat(cart.containsItem(packageStandard)).isTrue();

        cart.deleteItem(packageStandard);

        assertThat(cart.containsItem(packageStandard)).isFalse();

    }

}