// This code implements the Observer design pattern. It simulates a stock market system where the StockMarket class (Subject) maintains a list of investors (Observers).
// When stock information (stock symbol and price) changes, it notifies all registered observers. The Investor class listens for updates and receives stock price changes.
// Observers can be added or removed, and when the stock price is updated, all current observers are notified of the change.

// to run this file run below command
// javac com\designpatterns\behavioral\ObserverPatternDemo.java
// java com.designpatterns.behavioral.ObserverPatternDemo

package com.designpatterns.behavioral;

import java.util.ArrayList;
import java.util.List;

interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

interface Observer {
    void update(String stockSymbol, double price);
}

class StockMarket implements Subject {
    private List<Observer> observers;
    private String stockSymbol;
    private double price;

    public StockMarket() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Observer registered: " + observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observers.remove(observer)) {
            System.out.println("Observer removed: " + observer);
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(stockSymbol, price);
        }
    }

    public void setStockInfo(String stockSymbol, double price) {
        this.stockSymbol = stockSymbol;
        this.price = price;
        System.out.println("Stock price updated: " + stockSymbol + " - $" + price);
        notifyObservers();
    }
}

class Investor implements Observer {
    private String name;

    public Investor(String name) {
        this.name = name;
    }

    @Override
    public void update(String stockSymbol, double price) {
        System.out.println(name + " received update: " + stockSymbol + " now at $" + price);
    }

    @Override
    public String toString() {
        return name;
    }
}

public class ObserverPatternDemo {
    public static void main(String[] args) {
        StockMarket nasdaq = new StockMarket();

        Investor warren = new Investor("Warren Buffett");
        Investor george = new Investor("George Soros");

        nasdaq.registerObserver(warren);
        nasdaq.registerObserver(george);

        nasdaq.setStockInfo("AAPL", 150.50);
        nasdaq.setStockInfo("GOOGL", 2750.75);

        nasdaq.removeObserver(george);

        nasdaq.setStockInfo("MSFT", 305.25);
    }
}