package com.example.payment;

public interface PaymentApi {
    PaymentApiResponse charge(String key, double amount);

}
