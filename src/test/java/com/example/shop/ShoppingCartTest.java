package com.example.shop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ShoppingCartTest {

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
        ShoppingCart cart = new ShoppingCart();

        cart.addItem(packageStandard);

        assertThat(cart.containsItem(packageStandard)).isTrue();
    }

}