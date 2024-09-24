// This code implements the Strategy design pattern. It simulates a shopping cart system where different payment methods (CreditCardPayment, PayPalPayment, BitcoinPayment) can be selected at runtime.
// The PaymentStrategy interface defines a common method 'pay' which is implemented by different payment types.
// The ShoppingCart class uses a payment strategy to complete a transaction. The strategy can be changed at runtime, allowing flexible payment options during checkout.

// to run this file run below command
// javac com\designpatterns\behavioral\StrategyPatternDemo.java
// java com.designpatterns.behavioral.StrategyPatternDemo

package com.designpatterns.behavioral;

// Payment strategies
interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Credit Card ending with " + cardNumber.substring(cardNumber.length() - 4));
    }
}

class PayPalPayment implements PaymentStrategy {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using PayPal account: " + email);
    }
}

class BitcoinPayment implements PaymentStrategy {
    private String walletId;

    public BitcoinPayment(String walletId) {
        this.walletId = walletId;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " worth of Bitcoin from wallet: " + walletId);
    }
}

class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void checkout(double amount) {
        paymentStrategy.pay(amount);
    }
}

public class StrategyPatternDemo {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        
        cart.setPaymentStrategy(new CreditCardPayment("1234-5678-9012-3456"));
        cart.checkout(100.0);

        cart.setPaymentStrategy(new PayPalPayment("example@example.com"));
        cart.checkout(50.5);

        cart.setPaymentStrategy(new BitcoinPayment("1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2"));
        cart.checkout(75.0);
    }
}