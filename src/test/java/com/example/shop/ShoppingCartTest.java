package com.example.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ShoppingCartTest {

    private ShoppingCart cart;
    private ShopItems item;

    private static final ShopItems packageStandard = new ShopItems("packageStandard", 20000.00);
    private static final ShopItems sunscreen = new ShopItems("Sunscreen", 59.00);
    private static final ShopItems boatTicket = new ShopItems("Boat tickets", 800.00);

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
    }

    /**
 * Implementationen ska stödja:
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
        cart.addItem(packageStandard);

        assertThat(cart.containsItem(packageStandard)).isTrue();

        cart.deleteItem(packageStandard);

        assertThat(cart.containsItem(packageStandard)).isFalse();

    }

    /**
     * Verifies that totalCost adds the cost of all items in ShoppingCart.
     *
     * Tested scenarios:
     * - items put in shoppingcart adds to correct total cost
     */

    @ParameterizedTest (name = "{0}")
    @MethodSource ("shouldAddTotalCost")
    @DisplayName("Should add the cost of all items in shopping cart")
    void shouldAddToTotalCostOfAllItems(
            List<ShopItems> items,
            double expectedTotal
    ){
        items.forEach(cart::addItem);
        assertThat(cart.totalShoppingCartCost()).isEqualTo(expectedTotal);
    }
    static Stream<Arguments> shouldAddTotalCost() {
        return Stream.of(
                Arguments.of(List.of(packageStandard), 20000.00),
                Arguments.of(List.of(sunscreen, boatTicket),859.00),
                Arguments.of(List.of(packageStandard, sunscreen, boatTicket),2859.00)
        );
    }

    /**
     * Verifies that addDiscount applies a discount to the cart.
     *
     * Tested scenarios:
     * - Shopping cart is discounted
     */
    @Test
    @DisplayName("Should apply discount to shopping cart")
    void shouldApplyDiscount() {
        cart.addItem(packageStandard);

        assertThat(cart.totalShoppingCartCost()).isEqualTo(20000.00);
        cart.addDiscount(20.00);
        assertThat(cart.totalShoppingCartCost()).isEqualTo(16000.00);
    }


}