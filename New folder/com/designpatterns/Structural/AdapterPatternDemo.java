// This code demonstrates the Adapter design pattern, which allows incompatible interfaces to work together. 
// The LegacyPaymentSystem has methods for processing payments in an old system, but doesn't implement the new PaymentProcessor interface.
// The PaymentSystemAdapter acts as a bridge by converting calls from the PaymentProcessor interface into methods that the legacy system understands.
// The PaymentGateway class interacts with the new PaymentProcessor interface, allowing it to execute payments and refunds using both modern and legacy systems.


package com.designpatterns.structural;

interface PaymentProcessor {
    void processPayment(double amount);
    void refundPayment(double amount);
}

class LegacyPaymentSystem {
    public void makePayment(float amount) {
        System.out.println("Legacy system processing payment of $" + amount);
    }

    public void reverseTransaction(float amount) {
        System.out.println("Legacy system reversing transaction of $" + amount);
    }
}

class PaymentSystemAdapter implements PaymentProcessor {
    private LegacyPaymentSystem legacySystem;

    public PaymentSystemAdapter(LegacyPaymentSystem legacySystem) {
        this.legacySystem = legacySystem;
    }

    @Override
    public void processPayment(double amount) {
        System.out.println("Adapter: Converting processPayment call to legacy system");
        legacySystem.makePayment((float) amount);
    }

    @Override
    public void refundPayment(double amount) {
        System.out.println("Adapter: Converting refundPayment call to legacy system");
        legacySystem.reverseTransaction((float) amount);
    }
}

class PaymentGateway {
    private PaymentProcessor paymentProcessor;

    public PaymentGateway(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public void executePayment(double amount) {
        System.out.println("Payment Gateway: Executing payment of $" + amount);
        paymentProcessor.processPayment(amount);
    }

    public void executeRefund(double amount) {
        System.out.println("Payment Gateway: Executing refund of $" + amount);
        paymentProcessor.refundPayment(amount);
    }
}

public class AdapterPatternDemo {
    public static void main(String[] args) {
        LegacyPaymentSystem legacySystem = new LegacyPaymentSystem();
        PaymentProcessor adapter = new PaymentSystemAdapter(legacySystem);
        PaymentGateway gateway = new PaymentGateway(adapter);

        gateway.executePayment(100.00);
        gateway.executeRefund(50.00);
    }
}