// This code demonstrates the Decorator design pattern, which allows for adding new functionality to an object dynamically at runtime without altering its structure.
// The Coffee interface defines the basic methods for a coffee object. SimpleCoffee is the core coffee class, while CoffeeDecorator serves as the base class for all decorators.
// Different decorators like MilkDecorator, SugarDecorator, and WhippedCreamDecorator extend the functionality of a coffee by adding ingredients and updating the cost.
// The DecoratorPatternDemo shows how to wrap a simple coffee with multiple decorators to add features like milk, sugar, and whipped cream while calculating the total cost.



package com.designpatterns.structural;

interface Coffee {
    String getDescription();
    double getCost();
}

class SimpleCoffee implements Coffee {
    @Override
    public String getDescription() {
        return "Simple Coffee";
    }

    @Override
    public double getCost() {
        return 1.0;
    }
}

abstract class CoffeeDecorator implements Coffee {
    protected Coffee decoratedCoffee;

    public CoffeeDecorator(Coffee coffee) {
        this.decoratedCoffee = coffee;
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription();
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost();
    }
}

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Milk";
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.5;
    }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Sugar";
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.2;
    }
}

class WhippedCreamDecorator extends CoffeeDecorator {
    public WhippedCreamDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Whipped Cream";
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.7;
    }
}

public class DecoratorPatternDemo {
    public static void main(String[] args) {
        // Order a simple coffee
        Coffee coffee = new SimpleCoffee();
        printCoffeeDetails(coffee);

        // Decorate it with milk
        coffee = new MilkDecorator(coffee);
        printCoffeeDetails(coffee);

        // Add sugar
        coffee = new SugarDecorator(coffee);
        printCoffeeDetails(coffee);

        // Add whipped cream
        coffee = new WhippedCreamDecorator(coffee);
        printCoffeeDetails(coffee);

        // Create a different combination
        Coffee anotherCoffee = new WhippedCreamDecorator(new MilkDecorator(new SimpleCoffee()));
        printCoffeeDetails(anotherCoffee);
    }

    private static void printCoffeeDetails(Coffee coffee) {
        System.out.println("Description: " + coffee.getDescription() + " | Cost: $" + String.format("%.2f", coffee.getCost()));
    }
}